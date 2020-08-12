package com.github.immortalmice.linkarray.java.analyze;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import com.github.immortalmice.linkarray.java.LinkArray;

public class LinkArrayApp{
	public static void main(String[] args){
		LinkArrayApp.checkCorrectness(10);
	}

	private static boolean checkCorrectness(int times){
		boolean isFailed = false;
		while(times -- > 0 && !isFailed){
			RunTest.FormatArray<ArrayList<Integer>> arrayList = LinkArrayApp.getArrayListFormatArray();
			RunTest.FormatArray<LinkArray<Integer>> linkArray = LinkArrayApp.getLinkArrayFormatArray();
			RunTest.FormatArray<LinkedList<Integer>> linkedList = LinkArrayApp.getLinkedListFormatArray();

			int randomLength = (new Random()).nextInt(100000);
			System.out.printf("Round %d with length %d\n", times, randomLength);
			if(!RunTest.testCorrectness(randomLength, linkedList, linkArray) || !RunTest.testGetCorrectness(randomLength, linkedList, linkArray))
				isFailed = true;
		}
		if(!isFailed)
			System.out.println("All Test Passed!");
		return isFailed;
	}
	
	
	private static RunTest.FormatArray<ArrayList<Integer>> getArrayListFormatArray(){
		return new RunTest.FormatArray<>("ArrayList", new ArrayList<Integer>()
			, (array, value) -> { return array.size() != 0 ? array.get(value % array.size()) : null; }
			, (array, value) -> { array.add(value); }
			, (array, value) -> { array.add(0, value); }
			, (array, value) -> { return array.size() != 0 ? array.remove(array.size()-1) : null; }
			, (array, value) -> { return array.size() != 0 ? array.remove(0) : null; });
	}

	private static RunTest.FormatArray<LinkedList<Integer>> getLinkedListFormatArray(){
		return new RunTest.FormatArray<>("LinkedList", new LinkedList<Integer>()
			, (array, value) -> { return array.size() != 0 ? array.get(value % array.size()) : null; }
			, (array, value) -> { array.addLast(value); }
			, (array, value) -> { array.addFirst(value); }
			, (array, value) -> { return array.size() != 0 ? array.removeLast() : null; }
			, (array, value) -> { return array.size() != 0 ? array.removeFirst() : null; });
	}

	private static RunTest.FormatArray<LinkArray<Integer>> getLinkArrayFormatArray(){
		return new RunTest.FormatArray<>("LinkArray", new LinkArray<Integer>()
			, (array, value) -> { return array.length() != 0 ? array.get(value % array.length()) : null; }
			, (array, value) -> { array.push(value); }
			, (array, value) -> { array.unshift(value); }
			, (array, value) -> { return array.size() != 0 ? array.pop() : null; }
			, (array, value) -> { return array.size() != 0 ? array.shift() : null; });
	}
}