package com.github.immortalmice.linkarray.java;

import java.util.ArrayList;

import com.sun.istack.internal.Nullable;

public class LinkArray<T>{
	protected ArrayList<LinkArrayNode<T>> array = new ArrayList<>();
	protected LinkArrayNode<T> head = null;
	protected LinkArrayNode<T> tail = null;
	protected int lowerBound = 0;
	protected int upperBound = -1;
	protected int lastRefactorUpperBound = -1;

	public int length(){ return this.upperBound - this.lowerBound + 1; }

	protected int getMappedIndex(int index){ return index - this.lowerBound; }
	protected int getReverseMappedIndex(int index){ return index + this.lowerBound; }

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
			this.array.set(this.upperBound, elementToPush);
		}else{
			this.array.add(elementToPush);
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
			LinkArrayNode<T> node = this.array.get(i);
			if(node.index == target && node.value != null)
				return node.value;
		}

		return null;
	}

	public void refactor(){
		ArrayList<LinkArrayNode<T>> newArray = new ArrayList<>();

		LinkArrayNode<T> current = this.head;
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

	protected class LinkArrayNode<F>{
		public int index;
		public F value;
		public LinkArrayNode<F> next = null;
		public LinkArrayNode<F> pre = null;

		public LinkArrayNode(int indexIn, F valueIn){
			this.index = indexIn;
			this.value = valueIn;
		}
	}
}