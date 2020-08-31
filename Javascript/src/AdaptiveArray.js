const AutoLinkArray = require("./AutoLinkArray.js");

module.exports = class AdaptiveArray{
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
			this.inPlaceUpgrade();
		}
		return this.array.unshift(val);
	}

	pop(){
		return this.array.pop();
	}
	shift(){
		if(!this.upgraded && this.array.length >= this.bound){
			this.inPlaceUpgrade();
		}
		return this.array.shift();
	}

	[Symbol.iterator](){
		return this.array[Symbol.iterator];
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

	concat(anotherAdaptiveArray){
		if(this.upgraded ^ anotherAdaptiveArray.upgraded){
			let array1 = this.upgraded ? this : AdaptiveArray.upgrade(this);
			let array2 = anotherAdaptiveArray.upgraded ? anotherAdaptiveArray : AdaptiveArray.upgrade(anotherAdaptiveArray);
			return array1.concat(array2);
		}
		let newAdaptiveArray = new AdaptiveArray(this.bound, this.refactorBound);
		newAdaptiveArray.array = this.array.concat(anotherAdaptiveArray.array);
		newAdaptiveArray.upgraded = this.upgraded;
		return newAdaptiveArray;
	}

	asArray(){
		if(this.upgraded){
			return this.array.asArray();
		}
		return this.array;
	}

	asArray(){
		if(this.upgraded){
			return this.array.asArray();
		}
		return this.array;
	}

	reverse(){
		return this.array.reverse();
	}

	forEach(fun){
		this.array.forEach(fun);
	}

	inPlaceUpgrade(){
		if(!this.upgraded){
			this.array = AutoLinkArray.fromArray(this.array, this.refactorBound);
			this.upgraded = true;
		}
	}

	static upgrade(adaptiveArrayIn){
		let newAdaptiveArray = new AdaptiveArray(this.bound, this.refactorBound);
		if(!adaptiveArrayIn.upgraded){
			newAdaptiveArray.array = AutoLinkArray.fromArray(adaptiveArrayIn.array);
		}else{
			newAdaptiveArray.array = adaptiveArrayIn.array.copy();
		}
		newAdaptiveArray.upgraded = true;
		return newAdaptiveArray;
	}
}