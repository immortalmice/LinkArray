const LinkArray = require("./LinkArray.js");

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
}