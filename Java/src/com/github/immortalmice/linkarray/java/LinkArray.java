package com.github.immortalmice.linkarray.java;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.sun.istack.internal.Nullable;

public class LinkArray<T> extends AbstractCollection<T>{
	protected ArrayList<LinkArrayNode> array = new ArrayList<>();
	protected LinkArrayNode head = null;
	protected LinkArrayNode tail = null;
	protected int lowerBound = 0;
	protected int upperBound = -1;
	protected int lastRefactorUpperBound = -1;

	public int length(){ return this.upperBound - this.lowerBound + 1; }

	protected int getMappedIndex(int index){ return index - this.lowerBound; }
	protected int getReverseMappedIndex(int index){ return index + this.lowerBound; }

	public boolean push(T val){
		LinkArrayNode elementToPush = new LinkArrayNode(++ this.upperBound, val);
		elementToPush.pre = this.tail;

		if(this.tail != null){
			this.tail.next = elementToPush;
		}
		if(this.head == null){
			this.head = elementToPush;
		}
		this.tail = elementToPush;

		if(this.upperBound >= 0 && this.upperBound <= this.lastRefactorUpperBound){
			this.array.set(this.upperBound, elementToPush);
		}else{
			this.array.add(elementToPush);
		}

		return true;
	}

	public void unshift(T val){
		LinkArrayNode elementToUnshift = new LinkArrayNode(-- this.lowerBound, val);
		elementToUnshift.next = this.head;

		if(this.head != null){
			this.head.pre = elementToUnshift;
		}
		if(this.tail == null){
			this.tail = elementToUnshift;
		}
		this.head = elementToUnshift;

		if(this.lowerBound >= 0){
			this.array.set(this.lowerBound, elementToUnshift);
		}else{
			this.array.add(elementToUnshift);
		}

		return;
	}

	public T get(int index){
		if(index < 0 || index > this.length()-1) throw new IndexOutOfBoundsException();

		int target = this.getReverseMappedIndex(index);
		if(target >= 0 && target <= this.lastRefactorUpperBound)
			return this.array.get(target).value;

		int start = target;
		if(start < 0)
			start = Math.abs(start) + this.lastRefactorUpperBound;

		for(int i = start; i <= this.array.size()-1; i ++){
			LinkArrayNode node = this.array.get(i);
			if(node.index == target && node.value != null)
				return node.value;
		}

		return null;
	}

	public void refactor(){
		ArrayList<LinkArrayNode> newArray = new ArrayList<>();

		LinkArrayNode current = this.head;
		int i = 0;

		while(current != null){
			current.index = i ++;
			newArray.add(current);
			current = current.next;
		}

		this.array = newArray;

		this.lowerBound = 0;
		this.upperBound = this.array.size()-1;
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

	@Override
	public boolean add(T val) {
		return this.push(val);
	}

	@Override
	public void clear() {
		this.array = new ArrayList<>();

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

	@Override
	public boolean remove(Object object) {
		if(this.head != null && this.head.value == object){
			this.shift();
			return true;
		}
		if(this.tail != null && this.tail.value == object){
			this.pop();
			return true;
		}
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeAll(Collection<?> collection){
		Iterator<?> iterator = collection.iterator();
		while(iterator.hasNext()){
			this.remove(iterator.next());
		}
		return true;
	}

	@Override
	public boolean retainAll(Collection<?> collection){
		throw new UnsupportedOperationException();
	}

	@Override
	public int size(){
		return this.length();
	}

	public void devPrint(){
		System.out.printf("==========================\n");
		System.out.printf("LowerBound: %d\n", this.lowerBound);
		System.out.printf("UpperBound: %d\n", this.upperBound);
		System.out.printf("LastRefactorUpperBound: %d\n", this.lastRefactorUpperBound);

		System.out.printf("==========================");
		this.array.forEach((node) -> {
			System.out.printf("\nIndex: %d\n", node.index);
			System.out.printf("Value: %d\n", node.value);
		});
		System.out.printf("==========================\n");
	}

	protected class LinkArrayNode{
		public int index;
		public T value;
		public LinkArrayNode next = null;
		public LinkArrayNode pre = null;

		public LinkArrayNode(int indexIn, T valueIn){
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