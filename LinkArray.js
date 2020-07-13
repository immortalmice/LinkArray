module.exports = class LinkArray{
	constructor(){
		this.head = null;
		this.tail = this.head;
		this.array = [];
		this.lower_bound = 0;
		this.upper_bound = -1;
		this.lastRefactorUpperBound = -1;
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
			return;
		}
		this.array.push(elementToPush);
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
			return;
		}
		this.array.push(elementToUnshift);
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
		if(index < 0 || index > this.length-1 || this.length === 0) return undefined;

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
		/* Should never go here */
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
		this.lastRefactorUpperBound = this.array.length-1;
		this.lower_bound = 0;
		this.upper_bound = this.array.length-1;
	}

	devPrint(){
		console.log(this.array);
	}
	refactorAndPrint(){
		this.refactor();
		console.log(this.array);
	}
}