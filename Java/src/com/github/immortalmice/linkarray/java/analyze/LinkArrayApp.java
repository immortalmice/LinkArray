package com.github.immortalmice.linkarray.java.analyze;

import com.github.immortalmice.linkarray.java.LinkArray;

public class LinkArrayApp{
	public static void main(String[] args){
		LinkArray<Integer> array = new LinkArray<>();
		array.push(5);
		array.push(9);
		array.unshift(-8);
		array.push(106);
		array.unshift(66);
		array.unshift(7);
		array.devPrint();
	}
}