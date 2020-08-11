const LinkArray = require("./LinkArray.js");
/**
 * A child class extends {@link LinkArray}.
 * This will auto refator itself when the length of unrefactor parts exceed a const value set in contruction.
 * 
 * @author Immortalmice
 */
module.exports = class AutoLinkArray extends LinkArray{
	constructor(refactorBoundIn){
		super();
		this.refactorBound = refactorBoundIn || 5000;
	}
	
	get(index){
		if(this.array.length - this.lastRefactorUpperBound >= this.refactorBound){
			this.refactor();
			if(this.array[index] !== undefined && this.array[index].index !== undefined){
				return this.array[index].value;
			}else{
				return undefined;
			}
		}
		return super.get(index);
	}
	concat(anotherLinkArray, newRefactorBound){
		return AutoLinkArray.fromLinkArray(super.concat(anotherLinkArray), newRefactorBound || this.refactorBound);
	}

	copy(){
		return AutoLinkArray.fromLinkArray(super.copy(), this.refactorBound);
	}

	static fromArray(array, boundIn){
		return AutoLinkArray.fromLinkArray(LinkArray.fromArray(array), boundIn);
	}

	static fromDoublyLinkedList(list, boundIn){
		return AutoLinkArray.fromLinkArray(LinkArray.fromDoublyLinkedList(list), boundIn);
	}

	static fromLinkArray(linkArray, boundIn){
		let autoLinkArray = new AutoLinkArray(boundIn);
		linkArray.forEach((value) => {
			autoLinkArray.push(value);
		});
		return autoLinkArray;
	}
}