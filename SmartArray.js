const AutoLinkArray = require("./AutoLinkArray.js");

module.exports = class SmartArray{
	constructor(boundIn, refactorBoundIn){
		this.bound = boundIn || 5000;
		this.refactorBound = refactorBoundIn || this.bound;
		this.array = [];
		this.upgraded = false;
	}

	get length(){ return this.array.length; }

	get(index){
		return this.upgraded ? this.array.get(index) : this.array[index];
	}

	push(val){
		return this.array.push(val);
	}
	unshift(val){
		if(!this.upgraded && this.array.length >= this.bound){
			this.array = AutoLinkArray.fromArray(this.array, this.refactorBound);
			this.upgraded = true;
		}
		return this.array.unshift(val);
	}

	pop(){
		return this.array.pop();
	}
	shift(){
		if(!this.upgraded && this.array.length >= this.bound){
			this.array = AutoLinkArray.fromArray(this.array, this.refactorBound);
			this.upgraded = true;
		}
		return this.array.shift();
	}

	asArray(){
		return this.upgraded ? this.array.asArray() : this.array;
	}

	isEmpty(){
		return this.length === 0;
	}

	clear(){
		this.array = [];
		this.upgraded = false;
	}

	concat(anotherSmartArray){
		//TODO
	}

	reverse(){
		return this.array.reverse();
	}

	forEach(fun){
		this.array.forEach(fun);
	}
}