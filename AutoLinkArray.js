const LinkArray = require("./LinkArray.js");

module.exports = class AutoLinkArray extends LinkArray{
	constructor(refactorBoundIn){
		super();
		this.refactorBound = refactorBoundIn || 10000;
	}
	get(index){
		if(this.array.length - this.lastRefactorUpperBound >= this.refactorBound){
			this.refactor();
			return this.array[index];
		}
		return super.get(index);
	}
}