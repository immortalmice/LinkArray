package com.github.immortalmice.linkarray.java;

import java.util.ArrayList;
import java.util.Iterator;
/**
 * A adaptable array-like data structure.<br>
 * When length is small, the implementation is a {@link ArrayList}
 *     , and when this grow up to a specific length then implementation will be {@link AutoLinkArray}.<br>
 * Once upgrade, won't degrade event when length being short again, unless {@link AdaptiveArray#clear()} is called.
 * 
 * @author Immortalmice
 *
 * @param <T> The type to store in
 * 
 * @see AutoLinkArray
 */
public class AdaptiveArray<T> implements Iterable<T>{
	/**
	 * Will be used in implementation when this is not upgraded.
	 */
	private ArrayList<T> nativeArray = new ArrayList<>();
	/**
	 * Will be used in implementation when this is upgraded.
	 */
	private AutoLinkArray<T> linkArray = new AutoLinkArray<>();
	/**
	 * A flag shows this is upgraded or not currently.
	 */
	private boolean isUpgraded = false;
	/**
	 * When length is over {@link AdaptiveArray#upgradeBound}, this will upgrade itself.
	 */
	private int upgradeBound;
	
	/**
	 * @param upgradeBoundIn {@link AdaptiveArray#upgradeBound}
	 */
	public AdaptiveArray(int upgradeBoundIn){
		this.upgradeBound = upgradeBoundIn;
	}
	
	/**
	 * Default of {@link AdaptiveArray#upgradeBound} will be 1000.
	 * 
	 * @see #AdaptiveArray(int)
	 */
	public AdaptiveArray(){
		this(1000);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<T> iterator(){ return this.isUpgraded ? this.linkArray.iterator() : this.nativeArray.iterator(); }
	
	/**
	 * Gets the length of this.
	 * 
	 * @return The length of this
	 */
	public int length(){ return this.isUpgraded ? this.linkArray.length() : this.nativeArray.size(); }
	
	/**
	 * Gets the value at the index.
	 * 
	 * @param index The index to query
	 * @return The value at the specific index
	 * 
	 * @see ArrayList#get(int)
	 * @see AutoLinkArray#get(int)
	 */
	public T get(int index){ return this.isUpgraded ? this.linkArray.get(index) : this.nativeArray.get(index); }
	
	/**
	 * Pushes a element to the back of this.
	 * 
	 * @param val The value to push
	 * 
	 * @see ArrayList#add(Object)
	 * @see AutoLinkArray#push(Object)
	 */
	public void push(T val){ if(this.isUpgraded) this.linkArray.push(val); else this.nativeArray.add(val); }
	
	/**
	 * Pops out the last element.
	 * 
	 * @return The element at the end of this, {@code null} if this is empty
	 * 
	 * @see ArrayList#add(int, Object)
	 * @see AutoLinkArray#pop()
	 */
	public T pop(){ return this.isUpgraded ? this.linkArray.pop() : this.nativeArray.remove(this.nativeArray.size()-1); }
	
	/**
	 * Unshifts a element to the front of this.
	 * Will upgrade this if length is over {@link AdaptiveArray#upgradeBound}.
	 * 
	 * @param val The value to unshift
	 * 
	 * @see ArrayList#add(int, Object)
	 * @see AutoLinkArray#unshift(Object)
	 */
	public void unshift(T val){
		if(!this.isUpgraded){
			if(this.nativeArray.size() < this.upgradeBound){
				this.nativeArray.add(0, val);
				return;
			}
			this.upgrade();
		}
		this.linkArray.unshift(val);
		return;
	}
	
	/**
	 * Shifts out the first element.
	 * Will upgrade this if length is over {@link AdaptiveArray#upgradeBound}.
	 * 
	 * @return The element at the start of this, {@code null} if this is empty
	 * 
	 * @see ArrayList#remove(int)
	 * @see AutoLinkArray#shift()
	 */
	public T shift(){
		if(!this.isUpgraded){
			if(this.nativeArray.size() < this.upgradeBound){
				return this.nativeArray.remove(0);
			}
			this.upgrade();
		}
		return this.linkArray.shift();
	}
	
	/**
	 * Upgrade this implementation from {@link ArrayList} to {@link AutoLinkArray}.
	 */
	protected void upgrade() {
		if(this.linkArray.length() != 0){
			this.linkArray.clear();
		}

		Iterator<T> iterator = this.nativeArray.iterator();
		while(iterator.hasNext()){
			this.linkArray.push(iterator.next());
		}

		this.isUpgraded = true;
	}
	
	/**
	 * Empties this and degrade this if this is upgraded.
	 */
	public void clear(){
		if(this.isUpgraded){
			this.linkArray.clear();
			this.isUpgraded = false;
		}

		this.nativeArray.clear();
	}
}