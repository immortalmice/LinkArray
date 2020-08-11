package com.github.immortalmice.linkarray.java.analyze;

import java.util.Iterator;

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
		System.out.println(array.get(0));
		System.out.println(array.get(5));
		System.out.println(array.get(3));
		System.out.println(array.get(2));

		System.out.println("===========");
		Iterator<Integer> iterator = array.iterator();
		while(iterator.hasNext()){
			System.out.println(iterator.next());
		}
		System.out.println("===========");
		
		array.refactor();
		array.devPrint();
		System.out.println(array.get(0));
		System.out.println(array.get(5));
		System.out.println(array.get(3));
		System.out.println(array.get(2));
		
		System.out.println("===========");
		System.out.printf("POP: %d\n", array.pop());
		System.out.printf("SHIFT: %d\n", array.shift());
		System.out.printf("POP: %d\n", array.pop());
		System.out.printf("POP: %d\n", array.pop());
		System.out.printf("SHIFT: %d\n", array.shift());
		System.out.printf("SHIFT: %d\n", array.shift());
		System.out.printf("POP: %d\n", array.pop());
		System.out.printf("SHIFT: %d\n", array.shift());
	}
}