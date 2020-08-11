package com.github.immortalmice.linkarray.java;

import java.util.ArrayList;

public class LinkArray<T>{
	protected ArrayList<LinkArrayNode<T>> array = new ArrayList<>();
	protected LinkArrayNode<T> head = null;
	protected LinkArrayNode<T> tail = null;
	protected int lowerBound = 0;
	protected int upperBound = -1;
	protected int lastRefactorUpperBound = -1;

	public int length(){ return this.upperBound - this.lowerBound + 1; }

	protected int getMappedIndex(int index){ return index - this.lowerBound; }
	protected int getReverseMappedIndex(int index){ return index + this.lowerBound; }

	protected class LinkArrayNode<F>{
		public int index;
		public F value;
		public LinkArrayNode<F> next = null;
		public LinkArrayNode<F> pre = null;

		public LinkArrayNode(int indexIn, F valueIn){
			this.index = indexIn;
			this.value = valueIn;
		}
	}
}