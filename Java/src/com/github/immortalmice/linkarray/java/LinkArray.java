package com.github.immortalmice.linkarray.java;

import java.util.Iterator;
import javax.annotation.Nullable;

/**
 * <p>A new array-like data structure.<br>
 * Aims on better balance performance on array operations.</p>
 *
 * Since
 * <li>Native array is slow when "shift & unshift", fast on random "get".
 * <li>Doubly linked list fast on "shift & unshift" but slow on random "get".<br>
 * 
 * So this data structure trying to merge the advantages of native array and doubly linked list.<br><br>
 *
 * <p>The main structure is an native array with nodes that record negative allowed index
 *     , and every node linked to other nodes like doubly linked list does.<br><br>
 *
 * @author Immortalmice
 *
 * @param <T> The type to store in
 */
@SuppressWarnings("unchecked")
public class LinkArray<T> implements Iterable<T>{
	/**
	 * The initial & extra capacity when allocate {@link LinkArray#array}.
	 */
	protected int reservedCapacity;
	/**
	 * The cursor points to the last element in {@link LinkArray#array} that has data. 
	 */
	protected int cursor = -1;
	/**
	 * The main array to store data.
	 */
	protected LinkArrayNode<T>[] array;
	/**
	 * Pointer points to the first elements.
	 */
	protected LinkArrayNode<T> head = null;
	/**
	 * Pointer points to the last elements.
	 */
	protected LinkArrayNode<T> tail = null;
	/**
	 * The minimum INNER index currently store in this.
	 */
	protected int lowerBound = 0;
	/**
	 * The maximum INNER index currently store in this.
	 */
	protected int upperBound = -1;
	/**
	 * The {@link LinkArray#upperBound}'s value when last time refactored.
	 */
	protected int lastRefactorUpperBound = -1;

	/**
	 * Get the length of this.
	 * 
	 * @return The length
	 */
	public int length(){ return this.upperBound - this.lowerBound + 1; }

	/**
	 * Maps INTERNAL index [{@link LinkArray#lowerBound}, {@link LinkArray#upperBound}] to the EXTERNAL index [0, {@link LinkArray#length()}).
	 * 
	 * @param index The INTERNAL index 
	 * @return The EXTERNAL index
	 */
	protected int getMappedIndex(int index){ return index - this.lowerBound; }
	
	/**
	 * Maps EXTERNAL index [0, {@link LinkArray#length()}) to the INTERNAL index [{@link LinkArray#lowerBound}, {@link LinkArray#upperBound}].
	 * 
	 * @param index The EXTERNAL index 
	 * @return The INTERNAL index
	 */
	protected int getReverseMappedIndex(int index){ return index + this.lowerBound; }
	
	/**
	 * @param reservedCapacityIn {@link LinkArray#reservedCapacity}
	 */
	public LinkArray(int reservedCapacityIn){
		this.reservedCapacity = reservedCapacityIn;
		this.array = (LinkArrayNode<T>[]) new LinkArrayNode[this.reservedCapacity];
	}
	
	/**
	 * Default {@link LinkArray#reservedCapacity} is 1000.
	 * 
	 * @see #LinkArray(int)
	 */
	public LinkArray(){
		this(1000);
	}

	/**
	 * Pushes a element to the back of this.
	 * 
	 * @param val The value to push
	 */
	public void push(T val){
		// Construct a node with increased {@link LinkArray#upperBound} value.
		LinkArrayNode<T> elementToPush = new LinkArrayNode<>(++ this.upperBound, val);
		elementToPush.pre = this.tail;

		// Do doubly linked list stuffs.
		if(this.tail != null){
			this.tail.next = elementToPush;
		}
		if(this.head == null){
			this.head = elementToPush;
		}
		this.tail = elementToPush;

		// Put the node into the container {@link LinkArray#array} by replacing deleted refactored node or push operation.
		if(this.upperBound >= 0 && this.upperBound <= this.lastRefactorUpperBound){
			this.array[this.upperBound] = elementToPush;
		}else{
			this.addToEnd(elementToPush);
		}

		return;
	}
	
	/**
	 * Unshifts a element to the front of this.
	 * 
	 * @param val The value to unshift
	 */
	public void unshift(T val){
		// Construct a node with decreased {@link LinkArray#lowerBound} value.
		LinkArrayNode<T> elementToUnshift = new LinkArrayNode<>(-- this.lowerBound, val);
		elementToUnshift.next = this.head;

		// Do doubly linked list stuffs.
		if(this.head != null){
			this.head.pre = elementToUnshift;
		}
		if(this.tail == null){
			this.tail = elementToUnshift;
		}
		this.head = elementToUnshift;

		// Put the node into the container {@link LinkArray#array} by replacing deleted refactored node or push operation.
		// Push operation is used instead of unshift, since we don't have to move all elements to its next position.
		if(this.lowerBound >= 0){
			this.array[this.lowerBound] = elementToUnshift;
		}else{
			this.addToEnd(elementToUnshift);
		}

		return;
	}
	
	/**
	 * Pops out the last element.
	 * 
	 * @return The element at the end of this, {@code null} if this is empty
	 */
	@Nullable
	public T pop(){
		if(this.tail != null){
			T value = this.tail.value;

			// Mark node as removed.
			this.tail.value = null;
			
			// Do doubly linked list stuffs.
			this.tail = this.tail.pre;
			if(this.tail != null){
				this.tail.next = null;
			}else{
				this.head = null;
			}

			// Decrease {@link LinkArray#upperBound} since last data is removed.
			this.upperBound --;

			// Return the deleted data.
			return value;
		}
		// Return undefined when LinkArray is empty.
		return null;
	}

	/**
	 * Shifts out the first element.
	 * 
	 * @return The element at the start of this, {@code null} if this is empty
	 */
	@Nullable
	public T shift(){
		if(this.head != null){
			T value = this.head.value;

			// Mark node as removed.
			this.head.value = null;

			// Do doubly linked list stuffs.
			this.head = this.head.next;
			if(this.head != null){
				this.head.pre = null;
			}else{
				this.tail = null;
			}

			// Increase {@link LinkArray#lowerBound} since first data is removed.
			this.lowerBound ++;

			// Return the deleted data.
			return value;
		}
		// Return undefined when LinkArray is empty.
		return null;
	}

	/**
	 * Gets the value at the index.
	 * 
	 * @param index The index to query
	 * @return The value at the specific index
	 * @throws IndexOutOfBoundsException when the parameter index is out of range
	 */
	public T get(int index){
		// Throw exception when param is out of bound.
		if(index < 0 || index > this.length()-1) throw new IndexOutOfBoundsException();

		// Get the INTERNAL index from EXTERNAL index.
		int target = this.getReverseMappedIndex(index);

		// If the queried data is in refactored area, directly return the data by using native array's get.
		// In this case, time complexity will be O(1).
		if(target >= 0 && target <= this.lastRefactorUpperBound)
			return this.array[target].value;

		// If the queried data is NOT in refactored area, do searching in unrefactored area.
		// In this case, time complexity will be O(n).

		// Shink the searching range
		int start = target;
		if(start < 0)
			start = Math.abs(start) + this.lastRefactorUpperBound;

		// Do searching and return the queried data.
		for(int i = start; i <= this.cursor; i ++){
			LinkArrayNode<T> node = this.array[i];
			if(node.index == target && node.value != null)
				return node.value;
		}

		// Should NEVER go here, since range is already checked.
		return null;
	}

	/**
	 * Refactor the inner structure of this.
	 * After refactor, the inner order and index will match what it should be if this is a native array.
	 */
	public void refactor(){
		this.refactor((LinkArrayNode<T>[]) new LinkArrayNode[this.length()]);
	}

	/**
	 * @param newArray A array used in store the refactored data & replace original {@link LinkArray#array}
	 * @see LinkArray#refactor()
	 */
	protected void refactor(LinkArrayNode<T>[] newArray){
		LinkArrayNode<T> current = this.head;
		int i = 0;

		// Visit each node, put them into new native array in order with modified index in the node.
		while(current != null){
			current.index = i;
			newArray[i] = current;
			current = current.next;
			i ++;
		}

		// Replace the original {@link LinkArray#array}
		this.array = newArray;

		// Update properties.
		this.lowerBound = 0;
		this.upperBound = i - 1;
		this.cursor = this.upperBound + 1;
		this.lastRefactorUpperBound = this.upperBound;

		return;
	}

	/**
	 * Adds a node to the end of this.
	 * Will reallocate {@link LinkArray#array} if capacity is not enough to add.
	 * 
	 * @param node The node to add
	 */
	private void addToEnd(LinkArrayNode<T> node){
		if(++ this.cursor >= this.array.length){
			this.allocateNewArray(this.cursor);
		}
		this.array[cursor] = node;
	}

	/**
	 * Reallocates {@link LinkArray#array}.
	 * 
	 * @param minCapacity The minimum capacity should support
	 */
	protected void allocateNewArray(int minCapacity){
		LinkArrayNode<T>[] newArray = (LinkArrayNode<T>[]) new LinkArrayNode[minCapacity + this.reservedCapacity];
		for(int i = 0; i <= this.array.length-1; i ++){
			newArray[i] = this.array[i];
		}
		this.array = newArray;
	}

	/**
	 * Empties this.
	 */
	public void clear() {
		this.array = (LinkArrayNode<T>[]) new LinkArrayNode[this.reservedCapacity];

		this.head = null;
		this.tail = null;

		this.lowerBound = 0;
		this.upperBound = -1;
		this.lastRefactorUpperBound = -1;
	}

	/**
	 * Gets the iterator can walk through this.
	 * 
	 * @return An Iterator
	 */
	@Override
	public Iterator<T> iterator() {
		return new LinkArrayIterator();
	}
	
	/**
	 * Appends another iterable to this back.
	 * 
	 * @param iterable An iterable to append
	 */
	public void concat(Iterable<? extends T> iterable){
		Iterator<? extends T> iterator = iterable.iterator();

		while(iterator.hasNext()){
			this.push(iterator.next());
		}
	}
	
	/**
	 * Reverses this.
	 */
	public void reverse(){
		LinkArrayNode<T>[] newArray = (LinkArrayNode<T>[]) new LinkArrayNode[this.length()];

		// Visit each node from the end of this LinkArray and put the node into new native array(container) in order.
		LinkArrayNode<T> current = this.tail;
		int i = 0;
		while(current != null){
			// Swap {@link Node#pre} and {@link Node#next}, since order is now reversed.
			LinkArrayNode<T> temp = current.pre;
			current.pre = current.next;
			current.next = temp;
			// Update {@link Node#index} value.
			current.index = i;

			newArray[i ++] = current;
			current = current.next;
		}

		// Replace the original {@link LinkArray#array}.
		this.array = newArray;

		// Update properties.
		this.head = this.array[0];
		this.tail = this.array[this.array.length-1];

		this.lowerBound = 0;
		this.upperBound = this.array.length-1;
		this.lastRefactorUpperBound = this.upperBound;
	}

	/**
	 * Prints the information for debugging.
	 */
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

	/**
	 * A node store an INNER index, also work as a linked list node.
	 * 
	 * @author Immortalmice
	 *
	 * @param <E> The type of stored value
	 */
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

	/**
	 * An implementation of iterator for {@link LinkArray} to use.
	 * 
	 * @author Immortalmice
	 *
	 */
	private class LinkArrayIterator implements Iterator<T>{
		private int cursor = -1;

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean hasNext(){
			int newCursor = cursor + 1;
			if(newCursor >= 0 && newCursor <= LinkArray.this.length()-1)
				return true;
			return false;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public T next(){
			return LinkArray.this.get(++ cursor);
		}
		
		/**
		 * Only support when this points to the first or last element.
		 *  
		 * @throws UnsupportedOperationException when this is not pointing to valid position
		 * {@inheritDoc}
		 */
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