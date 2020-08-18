package com.github.immortalmice.linkarray.java;

/**
 * An derived class of LinkArray that will auto do refactor when it think it should.
 * 
 * @author Immortalmice
 *
 * @param <T> The type to store in
 */
public class AutoLinkArray<T> extends LinkArray<T>{
	
	/**
	 * {@inheritDoc}
	 * <br>
	 * Do refactor when this need to allocate new array memory.<br>
	 * Since copy to new array is needed, it's a best timing to do refactor at the same time.<br>
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void allocateNewArray(int minCapacity){
		this.refactor((LinkArrayNode<T>[]) new LinkArrayNode[this.length() + this.reservedCapacity]);
	}
}