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

	getMappedIndex(i){ return this.getIndex(i) - this.lower_bound; }
	getIndex(i){ return this.array[i].index; }
	
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
		this.head = elementToUnshift;
		this.array.push(elementToUnshift);
	}

	pop(){
		if(this.tail){
			let value = this.tail.value;
			this.tail.index = undefined;
			this.tail = this.tail.pre;
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
			this.lower_bound ++;
		}
		return undefined;
	}

	get(index){
		if(index < 0 || index > this.length-1 || this.length === 0) return undefined;

		if(index + this.lower_bound >= 0 && index + this.lower_bound <= this.lastRefactorUpperBound)
			return this.array[index + this.lower_bound].value;

		let start, end, operator;
		if(index + this.lower_bound < 0){
			start = this.array.length-1;
			end = this.lastRefactorUpperBound;
			operator = -1;
		}else{
			start = this.lastRefactorUpperBound+1;
			end = this.array.length;
			operator = 1;
		}
		
		for(let i = start; i !== end; i += operator){
			if(this.getMappedIndex(i) === index){
				let value = this.array[i].value;
				if(this.lastRefactorUpperBound - this.lower_bound){
					[this.array[index], this.array[i]] = [this.array[i], this.array[index]];
				}
				return value;
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