const LinkArray = require("./LinkArray.js");

module.exports = class AutoLinkArray extends LinkArray{
	constructor(refactorBoundIn, refactorRatioIn){
		super();
		this.refactorBound = refactorBoundIn || 200;
		this.refactorRatio = refactorRatioIn || 0.75;
	}
	get(index){
		if(this.array.length >= this.refactorBound
			&& (this.lastRefactorUpperBound <= 1 || this.lastRefactorUpperBound / this.array.length <= this.refactorRatio)){
			this.refactor();
			return this.array[index];
		}
		return super.get(index);
	}
}