package com.github.immortalmice.linkarray.java;

@SuppressWarnings("unchecked")
public class AutoLinkArray<T> extends LinkArray<T>{
	protected int refactorBound;

	public AutoLinkArray(){
		this(1000, 5000);
	}

	public AutoLinkArray(int reservedCapacityIn, int refactorBoundIn){
		super(reservedCapacityIn);
		this.refactorBound = refactorBoundIn;
	}

	public T get(int index){
		if(this.length() - this.lastRefactorUpperBound >= this.refactorBound){
			this.refactor();

			LinkArrayNode<T> node = this.array[index];
			if(node != null && node.value != null){
				System.out.print("From Catch");
				return node.value;
			}else{
				throw new IndexOutOfBoundsException();
			}
		}
		return super.get(index);
	}

	@Override
	protected void allocateNewArray(int minCapacity){
		this.refactor((LinkArrayNode<T>[]) new LinkArrayNode[minCapacity + this.reservedCapacity]);
	}
}