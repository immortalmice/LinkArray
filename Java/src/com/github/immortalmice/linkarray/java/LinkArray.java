package com.github.immortalmice.linkarray.java;

import java.util.Iterator;

import com.sun.istack.internal.Nullable;

@SuppressWarnings("unchecked")
public class LinkArray<T> implements Iterable<T>{
	protected int reservedCapacity;
	protected int cursor = -1;
	protected LinkArrayNode<T>[] array;
	protected LinkArrayNode<T> head = null;
	protected LinkArrayNode<T> tail = null;
	protected int lowerBound = 0;
	protected int upperBound = -1;
	protected int lastRefactorUpperBound = -1;

	public int length(){ return this.upperBound - this.lowerBound + 1; }

	protected int getMappedIndex(int index){ return index - this.lowerBound; }
	protected int getReverseMappedIndex(int index){ return index + this.lowerBound; }

	public LinkArray(){
		this(1000);
	}

	public LinkArray(int reservedCapacityIn){
		this.reservedCapacity = reservedCapacityIn;
		this.array = (LinkArrayNode<T>[]) new LinkArrayNode[this.reservedCapacity];
	}

	public boolean push(T val){
		LinkArrayNode<T> elementToPush = new LinkArrayNode<>(++ this.upperBound, val);
		elementToPush.pre = this.tail;

		if(this.tail != null){
			this.tail.next = elementToPush;
		}
		if(this.head == null){
			this.head = elementToPush;
		}
		this.tail = elementToPush;

		if(this.upperBound >= 0 && this.upperBound <= this.lastRefactorUpperBound){
			this.array[this.upperBound] = elementToPush;
		}else{
			this.addToEnd(elementToPush);
		}

		return true;
	}

	public void unshift(T val){
		LinkArrayNode<T> elementToUnshift = new LinkArrayNode<>(-- this.lowerBound, val);
		elementToUnshift.next = this.head;

		if(this.head != null){
			this.head.pre = elementToUnshift;
		}
		if(this.tail == null){
			this.tail = elementToUnshift;
		}
		this.head = elementToUnshift;

		if(this.lowerBound >= 0){
			this.array[this.lowerBound] = elementToUnshift;
		}else{
			this.addToEnd(elementToUnshift);
		}

		return;
	}

	public T get(int index){
		if(index < 0 || index > this.length()-1) throw new IndexOutOfBoundsException();

		int target = this.getReverseMappedIndex(index);
		if(target >= 0 && target <= this.lastRefactorUpperBound)
			return this.array[target].value;

		int start = target;
		if(start < 0)
			start = Math.abs(start) + this.lastRefactorUpperBound;

		for(int i = start; i <= this.cursor; i ++){
			LinkArrayNode<T> node = this.array[i];
			if(node.index == target && node.value != null)
				return node.value;
		}

		return null;
	}

	public void refactor(){
		LinkArrayNode<T>[] newArray = (LinkArrayNode<T>[]) new LinkArrayNode[this.array.length];

		LinkArrayNode<T> current = this.head;
		int i = 0;

		while(current != null){
			current.index = i;
			newArray[i] = current;
			current = current.next;
			i ++;
		}

		this.array = newArray;

		this.lowerBound = 0;
		this.upperBound = i - 1;
		this.cursor = this.upperBound + 1;
		this.lastRefactorUpperBound = this.upperBound;

		return;
	}

	@Nullable
	public T pop(){
		if(this.tail != null){
			T value = this.tail.value;
			this.tail.value = null;
			
			this.tail = this.tail.pre;
			if(this.tail != null){
				this.tail.next = null;
			}else{
				this.head = null;
			}

			this.upperBound --;

			return value;
		}
		return null;
	}

	@Nullable
	public T shift(){
		if(this.head != null){
			T value = this.head.value;
			this.head.value = null;

			this.head = this.head.next;
			if(this.head != null){
				this.head.pre = null;
			}else{
				this.tail = null;
			}

			this.lowerBound ++;

			return value;
		}
		return null;
	}

	private void addToEnd(LinkArrayNode<T> node){
		if(++ this.cursor >= this.array.length){
			this.allocateNewArray(this.cursor);
		}
		this.array[cursor] = node;
	}

	protected void allocateNewArray(int minCapacity){
		LinkArrayNode<T>[] newArray = (LinkArrayNode<T>[]) new LinkArrayNode[minCapacity + this.reservedCapacity];
		for(int i = 0; i <= this.array.length-1; i ++){
			newArray[i] = this.array[i];
		}
		this.array = newArray;
	}

	public void clear() {
		this.array = (LinkArrayNode<T>[]) new LinkArrayNode[this.reservedCapacity];

		this.head = null;
		this.tail = null;

		this.lowerBound = 0;
		this.upperBound = -1;
		this.lastRefactorUpperBound = -1;
	}

	@Override
	public Iterator<T> iterator() {
		return new LinkArrayIterator();
	}

	public void devPrint(){
		System.out.printf("==========================\n");
		System.out.printf("LowerBound: %d\n", this.lowerBound);
		System.out.printf("UpperBound: %d\n", this.upperBound);
		System.out.printf("LastRefactorUpperBound: %d\n", this.lastRefactorUpperBound);

		System.out.printf("==========================");
		for(LinkArrayNode<T> node : this.array){
			if(node != null){
				System.out.printf("\nIndex: %d\n", node.index);
				System.out.printf("Value: %d\n", node.value);
			}
		}
		System.out.printf("==========================\n");
	}

	protected class LinkArrayNode<E>{
		public int index;
		public E value;
		public LinkArrayNode<E> next = null;
		public LinkArrayNode<E> pre = null;

		public LinkArrayNode(int indexIn, E valueIn){
			this.index = indexIn;
			this.value = valueIn;
		}
	}

	private class LinkArrayIterator implements Iterator<T>{
		private int cursor = -1;

		@Override
		public boolean hasNext(){
			int newCursor = cursor + 1;
			if(newCursor >= 0 && newCursor <= LinkArray.this.length()-1)
				return true;
			return false;
		}

		@Override
		public T next(){
			return LinkArray.this.get(++ cursor);
		}
		
		@Override
		public void remove(){
			if(cursor == 0){
				LinkArray.this.shift();
			}else if(cursor == LinkArray.this.length()-1){
				LinkArray.this.pop();
			}else{
				throw new UnsupportedOperationException();
			}
		}
	}
}