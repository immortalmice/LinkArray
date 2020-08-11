package com.github.immortalmice.linkarray.java.analyze;

import java.util.ArrayList;
import java.util.Iterator;

import com.github.immortalmice.linkarray.java.LinkArray;
import com.github.immortalmice.linkarray.java.analyze.RunTest;

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

	private static RunTest.FormatArray<ArrayList<Integer>> getArrayListFormatArray(){
		return new RunTest.FormatArray<>("ArrayList", new ArrayList<Integer>()
			, (array, value) -> { return array.get(array.size() == 0 ? 0 : value % array.size()); }
			, (array, value) -> { array.add(value); }
			, (array, value) -> { array.add(0, value); }
			, (array, value) -> { array.remove(array.size()-1); }
			, (array, value) -> { array.remove(0); });
	}

	private static RunTest.FormatArray<LinkArray<Integer>> getLinkArrayFormatArray(){
		return new RunTest.FormatArray<>("LinkArray", new LinkArray<Integer>()
			, (array, value) -> { return array.get(array.length() == 0 ? 0 : value % array.length()); }
			, (array, value) -> { array.push(value); }
			, (array, value) -> { array.unshift(value); }
			, (array, value) -> { array.pop(); }
			, (array, value) -> { array.shift(); });
	}
}