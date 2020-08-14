package com.github.immortalmice.linkarray.java;

import java.util.ArrayList;
import java.util.Iterator;

public class AdaptiveArray<T> implements Iterable<T>{
	private ArrayList<T> nativeArray = new ArrayList<>();
	private AutoLinkArray<T> linkArray = new AutoLinkArray<>();
	private boolean isUpgraded = false;
	private int upgradeBound;

	public AdaptiveArray(int upgradeBoundIn){
		this.upgradeBound = upgradeBoundIn;
	}

	public AdaptiveArray(){
		this(1000);
	}

	@Override
	public Iterator<T> iterator(){ return this.isUpgraded ? this.linkArray.iterator() : this.nativeArray.iterator(); }

	public int length(){ return this.isUpgraded ? this.linkArray.length() : this.nativeArray.size(); }

	public T get(int index){ return this.isUpgraded ? this.linkArray.get(index) : this.nativeArray.get(index); }

	public void push(T val){ if(this.isUpgraded) this.linkArray.push(val); else this.nativeArray.add(val); }

	public T pop(){ return this.isUpgraded ? this.linkArray.pop() : this.nativeArray.remove(this.nativeArray.size()-1); }

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

	public T shift(){
		if(!this.isUpgraded){
			if(this.nativeArray.size() < this.upgradeBound){
				return this.nativeArray.remove(0);
			}
			this.upgrade();
		}
		return this.linkArray.shift();
	}

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
}