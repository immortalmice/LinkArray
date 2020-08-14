/**
 * A new array-like data structure.
 * Aims on better balance performance on array operations.
 *
 * Since native array is slow when "shift & unshift", fast on random "get"
 *     , and doubly linked list fast on "shift & unshift" but slow on random "get".
 * So this data structor trying to merge the advantages of native array and doubly linked list.
 *
 * The main structure is an native array with nodes that record negative allowed index
 *     , and every node linked to other nodes like doubly linked list does.
 * 
 * @author Immortalmice
 */

/**
 * @typedef {Object} Node
 * @property {Integer|undefined} index - a INTERNAL index of this node, undefined means this node is marked as removed.
 * @property {Object} value - the data stored in this node.
 * @property {Node|null} next - a reference to the next node.
 * @property {Node|null} pre - a reference to the previous node.
 */
module.exports = class LinkArray{
	/**
	 * @constructors
	 * @see {@link LinkArray#_init}
	 */
	constructor(){
		this._init();
	}

	/**
	 * The total length, works like native array's length property.
	 * @return {Non-Negative Integer} The total length.
	 *
	 * @getter
	 */
	get length(){ return this.upper_bound - this.lower_bound + 1; }

	/**
	 * Helper method to covert INTERNAL index into EXTERNAL index.
	 * 
	 * @param {Integer} i - The internal index stored in node.
	 * @return {Non-Negative Integer} The index of this node if this is a native array.
	 */
	getMappedIndex(i){ return i - this.lower_bound; }

	/**
	 * Helper method to covert EXTERNAL index into INTERNAL index.
	 * 
	 * @param {Non-Negative Integer} i - The external index.
	 * @return {Integer} The corresponding index that actually the node stored.
	 */
	getReverseMappedIndex(i){ return i + this.lower_bound; }

	/**
	 * Append data at the end of LinkArray.
	 *
	 * @param {Object} val - Data to push.
	 * @return {Non-Negative Integer} The length of this LinkArray after push.
	 */
	push(val){
		// Construct a node with increased {@link LinkArray#upper_bound} value.
		let elementToPush = {
			index: ++ this.upper_bound,
			value: val,
			next: null,
			pre: this.tail
		};

		// Do doubly linked list stuffs.
		if(this.tail){
			this.tail.next = elementToPush;
		}
		if(!this.head){
			this.head = elementToPush;
		}
		this.tail = elementToPush;

		// Put the node into the container {@link LinkArray#array} by replacing deleted refactored node or push operation.
		if(this.upper_bound >= 0 && this.upper_bound <= this.lastRefactorUpperBound){
			this.array[this.upper_bound] = elementToPush;
		}else{
			this.array.push(elementToPush);
		}

		// Return length of this LinkArray as native array does.
		return this.length;
	}

	/**
	 * Insert data at the start of LinkArray.
	 *
	 * @param {Object} val - Data to unshift.
	 * @return {Non-Negative Integer} The length of this LinkArray after unshift.
	 */
	unshift(val){
		// Construct a node with decreased {@link LinkArray#lower_bound} value.
		let elementToUnshift = {
			index: -- this.lower_bound,
			value: val,
			next: this.head,
			pre: null
		}

		// Do doubly linked list stuffs.
		if(this.head){
			this.head.pre = elementToUnshift;
		}
		if(!this.tail){
			this.tail = elementToUnshift;
		}
		this.head = elementToUnshift;

		// Put the node into the container {@link LinkArray#array} by replacing deleted refactored node or push operation.
		// Push operation is used instead of unshift, since unshift operation has bad performance in native array.
		if(this.lower_bound >= 0){
			this.array[this.lower_bound] = elementToUnshift;
		}else{
			this.array.push(elementToUnshift);
		}

		// Return length of LinkArray as native array does
		return this.length;
	}

	/**
	 * Delete the last data in LinkArray.
	 * @return {Object|undefined} The deleted data or undefined if this LinkArray is empty.
	 */
	pop(){
		if(this.tail){
			let value = this.tail.value;

			// Mark node as removed.
			this.tail.index = undefined;

			// Do doubly linked list stuffs.
			this.tail = this.tail.pre;
			if(this.tail){
				this.tail.next = null;
			}else{
				this.head = null;
			}

			// Decrease {@link LinkArray#upper_bound} since last data is removed.
			this.upper_bound --;

			// Return the deleted data.
			return value;
		}

		// Return undefined when LinkArray is empty.
		return undefined;
	}

	/**
	 * Delete the first data in LinkArray.
	 * @return {Object|undefined} The deleted data or undefined if this LinkArray is empty.
	 */
	shift(){
		if(this.head){
			let value = this.head.value;

			// Mark node as removed.
			this.head.index = undefined;

			// Do doubly linked list stuffs.
			this.head = this.head.next;
			if(this.head){
				this.head.pre = null;
			}else{
				this.tail = null;
			}

			// Increase {@link LinkArray#lower_bound} since first data is removed.
			this.lower_bound ++;

			// Return the deleted data.
			return value;
		}

		// Return undefined when LinkArray is empty.
		return undefined;
	}

	/**
	 * Get the data at the specific index.
	 * @param {Non-Negative Integer} index - The EXTERNAL index.
	 * @return {Object|undefined} The queried data at the index.
	 */
	get(index){
		// Return undefined when param is out of bound.
		if(index < 0 || index > this.length-1) return undefined;

		// Get the INTERNAL index from EXTERNAL index.
		let target = this.getReverseMappedIndex(index);

		// If the queried data is in refactored area, directly return the data by using native array's get.
		// In this case, time complexity will be O(1).
		if(target >= 0 && target <= this.lastRefactorUpperBound){
			return this.array[target].value;
		}

		// If the queried data is NOT in refactored area, do searching in unrefactored area.
		// In this case, time complexity will be O(n).

		// Shink the searching range
		let start = target;
		if(start < 0){
			start = Math.abs(start) + this.lastRefactorUpperBound;
		}

		// Do searching and return the queried data.
		for(let i = start; i <= this.array.length-1; i ++){
			if(this.array[i].index === target){
				return this.array[i].value;
			}
		}

		// Should NEVER go here, except index is not a integer.
		return "NOT FOUND";
	}

	/**
	 * Rearrange the node stored in container {@link LinkArray#array}.
	 * After refactoring, all index values stored in each node will match the node's actual index in {@link LinkArray#array}
	 *     , util next inert|remove operation is applied.
	 * The length of refactored area MAJOR the performance of "get" operation
	 *     , but keep in mind this refactor operation cost time too.
	 *
	 * @see {@link LinkArray#get}.
	 */
	refactor(){
		let newArray = [];

		let current = this.head;
		let i = 0;

		// Visit each node, put them into new native array in order with modified index in the node. 
		while(current){
			current.index = i;
			newArray.push(current);

			current = current.next;
			i ++;
		}

		// Replace the original container.
		this.array = newArray;

		// Update properties.
		this.lower_bound = 0;
		this.upper_bound = this.array.length-1;
		this.lastRefactorUpperBound = this.upper_bound;
	}

	/**
	 * Get the native array version of this LinkArray.
	 * Shallow-Copy on every data.
	 * @return {Array} Data in native array form.
	 */
	asArray(){
		let newArray = [];

		// Visit each node, put every data(not node) into new native array in order.
		let current = this.head;
		while(current){
			newArray.push(current.value);
			current = current.next;
		}

		return newArray;
	}

	/**
	 * Is this LinkArray containing data or not.
	 * @return {Boolean} Is this LinkArray containing data or not.
	 */
	isEmpty(){
		return this.head === null;
	}

	/**
	 * Remove all the data in this LinkArray.
	 */
	clear(){
		this._init();
	}

	/**
	 * Concat anther LinkArray to the end of this.
	 * NOT-IN-PLACE
	 * @return {LinkArray} A new LinkArray that is the result of this concat operation.
	 */
	concat(anotherLinkArray){
		let newLinkArray = this.copy();

		anotherLinkArray.forEach((value) => {
			newLinkArray.push(value);
		});

		return newLinkArray;
	}

	/**
	 * Reverse this LinkArray IN-PLACE.
	 * @return {LinkArray} Itself, after the reverse operation.
	 */
	reverse(){
		let newArray = [];

		// Visit each node from the end of this LinkArray and put the node into new native array(container) in order.
		let current = this.tail;
		let i = 0;
		while(current){
			// Swap {@link Node#pre} and {@link Node#next}, since order is now reversed.
			[current.pre, current.next] = [current.next, current.pre];
			// Update {@link Node#index} value.
			current.index = i ++;

			newArray.push(current);
			current = current.next;
		}
		// Replace the original {@link LinkArray#array}, since this is a IN-PLACE operation.
		this.array = newArray;

		// Update properties.
		this.head = this.array[0];
		this.tail = this.array[this.array.length-1];

		this.lower_bound = 0;
		this.upper_bound = this.array.length-1;
		this.lastRefactorUpperBound = this.upper_bound;

		return this;
	}

	/**
	 * Excute the function on all data in order.
	 * @param {Function.<Object, Non-Negative Integer, LinkArray>} fun - The function need to be excute.
	 *     @param {Object} The current data.
	 *     @param {Non-Negative Integer} The current EXTERNAL index.
	 *     @param {LinkArray} This LinkArray instance.
	 */
	forEach(fun){
		let current = this.head;
		let i = 0;

		// Visit every node and excute the function in order.
		while(current){
			fun(current.value, i, this);
			current = current.next;
			i ++;
		}
	}

	/**
	 * Get a new copy of this LinkArray.
	 * Shallow-Copy on every data.
	 * @return {LinkArray} A new copy of this LinkArray.
	 */
	copy(){
		let newLinkArray = new LinkArray();
		this.forEach((value) => {
			newLinkArray.push(value);
		});
		return newLinkArray;
	}

	/**
	 * Convert a native array to a new LinkArray.
	 * Shallow-Copy on every data.
	 * @param {Array} array - The native array to be converted.
	 * @return {LinkArray} The new LinkArray been converted from the native array.
	 * 
	 * @static
	 */
	static fromArray(array){
		let linkArray = new LinkArray();

		array.forEach((value) => {
			linkArray.push(value);
		});

		return linkArray;
	}

	/**
	 * Initialize this LinkArray, can used in {@link LinkArray#constructor} and {@link LinkArray#clear}.
	 */
	_init(){
		/**
		 * A reference to the first node.
		 * @type {Node|null}.
		 */
		this.head = null;
		/**
		 * A reference to the last node.
		 * @type {Node|null}.
		 */
		this.tail = this.head;
		/**
		 * The container used in store nodes.
		 * @type {Array.<Node>}.
		 */
		this.array = [];
		/**
		 * The smallest INTERNAL index value.
		 * @type {Integer}.
		 */
		this.lower_bound = 0;
		/**
		 * The biggest INTERNAL index value.
		 * @type {Integer}.
		 */
		this.upper_bound = -1;
		/**
		 * The {@link LinkArray#upperbound} value when last time refactor operation finished.
		 * @type {Interger >= -1}.
		 */
		this.lastRefactorUpperBound = -1;
	}
}