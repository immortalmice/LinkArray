package com.github.immortalmice.linkarray.java.analyze;

import java.util.ArrayList;
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

			int randomLength = (new Random()).nextInt(100000);
			System.out.printf("Round %d with length %d\n", times, randomLength);
			if(!RunTest.testCorrectness(randomLength, arrayList, linkArray) || !RunTest.testGetCorrectness(randomLength, arrayList, linkArray))
				isFailed = true;
		}
		if(!isFailed)
			System.out.println("All Test Passed!");
		return isFailed;
	}

	private static RunTest.FormatArray<ArrayList<Integer>> getArrayListFormatArray(){
		return new RunTest.FormatArray<>("ArrayList", new ArrayList<Integer>()
			, (array, value) -> { return array.size() == 0 ? 0 : array.get(value % array.size()); }
			, (array, value) -> { array.add(value); }
			, (array, value) -> { array.add(0, value); }
			, (array, value) -> { if(array.size() != 0) array.remove(array.size()-1); }
			, (array, value) -> { if(array.size() != 0) array.remove(0); });
	}

	private static RunTest.FormatArray<LinkArray<Integer>> getLinkArrayFormatArray(){
		return new RunTest.FormatArray<>("LinkArray", new LinkArray<Integer>()
			, (array, value) -> { return array.length() == 0 ? 0 : array.get(value % array.length()); }
			, (array, value) -> { array.push(value); }
			, (array, value) -> { array.unshift(value); }
			, (array, value) -> { array.pop(); }
			, (array, value) -> { array.shift(); });
	}
}