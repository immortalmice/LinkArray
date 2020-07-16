module.exports = class LinkArray{
	constructor(){
		this._init();
	}

	get length(){ return this.upper_bound - this.lower_bound + 1; }

	getMappedIndex(i){ return i - this.lower_bound; }
	getReverseMappedIndex(i){ return i + this.lower_bound; }

	push(val){
		let elementToPush = {
			index: ++ this.upper_bound,
			value: val,
			next: null,
			pre: this.tail
		};

		if(this.tail){
			this.tail.next = elementToPush;
		}
		if(!this.head){
			this.head = elementToPush;
		}
		this.tail = elementToPush;
		if(this.upper_bound >= 0 && this.upper_bound <= this.lastRefactorUpperBound){
			this.array[this.upper_bound] = elementToPush;
		}else{
			this.array.push(elementToPush);
		}
		return this.length;
	}

	unshift(val){
		let elementToUnshift = {
			index: -- this.lower_bound,
			value: val,
			next: this.head,
			pre: null
		}

		if(this.head){
			this.head.pre = elementToUnshift;
		}
		if(!this.tail){
			this.tail = elementToUnshift;
		}
		this.head = elementToUnshift;
		if(this.lower_bound >= 0){
			this.array[this.lower_bound] = elementToUnshift;
		}else{
			this.array.push(elementToUnshift);
		}
		return this.length;
	}

	pop(){
		if(this.tail){
			let value = this.tail.value;
			this.tail.index = undefined;
			this.tail = this.tail.pre;
			if(this.tail){
				this.tail.next = null;
			}else{
				this.head = null;
			}
			this.upper_bound --;
			return value;
		}
		return undefined;
	}

	shift(){
		if(this.head){
			let value = this.head.value;
			this.head.index = undefined;
			this.head = this.head.next;
			if(this.head){
				this.head.pre = null;
			}else{
				this.tail = null;
			}
			this.lower_bound ++;
			return value;
		}
		return undefined;
	}

	get(index){
		if(index < 0 || index > this.length-1) return undefined;

		let target = this.getReverseMappedIndex(index);
		if(target >= 0 && target <= this.lastRefactorUpperBound){
			return this.array[target].value;
		}

		let start = target;
		if(start < 0){
			start = Math.abs(start) + this.lastRefactorUpperBound;
		}
		for(let i = start; i <= this.array.length-1; i ++){
			if(this.array[i].index === target){
				return this.array[i].value;
			}
		}
		/* Should never go here, except index is not integer */
		return "NOT FOUND";
	}

	refactor(){
		let newArray = [];
		let current = this.head;
		let i = 0;
		while(current){
			current.index = i;
			newArray.push(current);
			current = current.next;
			i ++;
		}
		this.array = newArray;
		this.lower_bound = 0;
		this.upper_bound = this.array.length-1;
		this.lastRefactorUpperBound = this.upper_bound;
	}

	asArray(){
		let newArray = [];
		let current = this.head;
		while(current){
			newArray.push(current.value);
			current = current.next;
		}
		return newArray;
	}

	isEmpty(){
		return this.head === null;
	}

	clear(){
		this._init();
	}

	concat(anotherLinkArray){
		let current = anotherLinkArray.head;
		while(current){
			this.push(current.value);
			current = current.next;
		}
	}

	reverse(){
		let current = this.tail;
		let i = 0;
		let newArray = [];
		while(current){
			[current.pre, current.next] = [current.next, current.pre];
			current.index = i ++;
			newArray.push(current);
			current = current.next;
		}
		this.array = newArray;

		this.head = this.array[0];
		this.tail = this.array[this.array.length-1];

		this.lower_bound = 0;
		this.upper_bound = this.array.length-1;
		this.lastRefactorUpperBound = this.upper_bound;
		return this;
	}

	forEach(fun){
		let current = this.head;
		let i = 0;
		while(current){
			fun(current.value, i, this);
			current = current.next;
			i ++;
		}
	}

	static fromArray(array){
		let linkArray = new LinkArray();
		array.forEach((value) => {
			linkArray.push(value);
		});
		return linkArray;
	}

	static fromDoublyLinkedList(list){
		let linkArray = new LinkArray();
		list.forEach((node) => {
			linkArray.push(node.getData());
		});
		return linkArray;
	}

	_init(){
		this.head = null;
		this.tail = this.head;
		this.array = [];
		this.lower_bound = 0;
		this.upper_bound = -1;
		this.lastRefactorUpperBound = -1;
	}
}