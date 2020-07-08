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

	getMappedIndex(i){ return this.array[i].index - this.lower_bound; }
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

		if(index + this.lower_bound >= 0 && index + this.lower_bound <= this.lastRefactorUpperBound){
			return this.array[index + this.lower_bound].value;
		}

		let start = this.getReverseMappedIndex(index);
		if(start < 0){
			start = Math.abs(start) + this.lastRefactorUpperBound;
		}
		
		for(let i = start; i <= this.array.length-1; i ++){
			if(this.getMappedIndex(i) === index){
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