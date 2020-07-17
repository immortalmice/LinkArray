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
		if(this.upgraded ^ anotherSmartArray.upgraded){
			let array1 = this.upgraded ? this : SmartArray.upgrade(this);
			let array2 = anotherSmartArray.upgraded ? anotherSmartArray : SmartArray.upgrade(anotherSmartArray);
			return array1.concat(array2);
		}
		let newSmartArray = new SmartArray(this.bound, this.refactorBound);
		newSmartArray.array = this.array.concat(anotherSmartArray.array);
		newSmartArray.upgraded = this.upgraded;
		return newSmartArray;
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

	static upgrade(smartArrayIn){
		let newSmartArray = new SmartArray(this.bound, this.refactorBound);
		if(!smartArrayIn.upgraded){
			newSmartArray.array = AutoLinkArray.fromArray(smartArrayIn.array);
		}else{
			newSmartArray.array = smartArrayIn.array.copy();
		}
		newSmartArray.upgraded = true;
		return newSmartArray;
	}
}