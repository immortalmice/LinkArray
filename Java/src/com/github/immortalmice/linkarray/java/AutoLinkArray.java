package com.github.immortalmice.linkarray.java;

@SuppressWarnings("unchecked")
public class AutoLinkArray<T> extends LinkArray<T>{
	@Override
	protected void allocateNewArray(int minCapacity){
		this.refactor((LinkArrayNode<T>[]) new LinkArrayNode[minCapacity + this.reservedCapacity]);
	}
}