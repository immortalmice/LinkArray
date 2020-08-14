package com.github.immortalmice.linkarray.java.analyze;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.function.Supplier;

import com.github.immortalmice.linkarray.java.LinkArray;
import com.github.immortalmice.linkarray.java.AutoLinkArray;
import com.github.immortalmice.linkarray.java.AdaptiveArray;

@SuppressWarnings("unused")
public class LinkArrayApp{
	@SuppressWarnings("unchecked")
	public static void main(String[] args){
		for(int i = 2000; i <= 200000; i += 2000){
			Analyzer analyzer = new Analyzer(new Supplier[]{
				() -> FormatArrayProvider.ARRAY_LIST(),
				() -> FormatArrayProvider.AUTO_LINK_ARRAY()
			}, i, 50, "reports/AutoLinkArray/ArrayList/Report");
			
			analyzer.runDefault();
		}

		for(int i = 2000; i <= 200000; i += 2000){
			Analyzer analyzer = new Analyzer(new Supplier[]{
				() -> FormatArrayProvider.LINKED_LIST(),
				() -> FormatArrayProvider.AUTO_LINK_ARRAY()
			}, i, 50, "reports/AutoLinkArray/LinkedList/Report");
			
			analyzer.runDefault();
		}

		for(int i = 2000; i <= 200000; i += 2000){
			Analyzer analyzer = new Analyzer(new Supplier[]{
				() -> FormatArrayProvider.ARRAY_LIST(),
				() -> FormatArrayProvider.LINKED_LIST(),
				() -> FormatArrayProvider.AUTO_LINK_ARRAY()
			}, i, 50, "reports/AutoLinkArray/Triple/Report");
			
			analyzer.runDefault();
		}


		for(int i = 2000; i <= 200000; i += 2000){
			Analyzer analyzer = new Analyzer(new Supplier[]{
				() -> FormatArrayProvider.ARRAY_LIST(),
				() -> FormatArrayProvider.ADAPTIVE_ARRAY()
			}, i, 50, "reports/AdaptiveArray/ArrayList/Report");
			
			analyzer.runDefault();
		}

		for(int i = 2000; i <= 200000; i += 2000){
			Analyzer analyzer = new Analyzer(new Supplier[]{
				() -> FormatArrayProvider.LINKED_LIST(),
				() -> FormatArrayProvider.ADAPTIVE_ARRAY()
			}, i, 50, "reports/AdaptiveArray/LinkedList/Report");
			
			analyzer.runDefault();
		}

		for(int i = 2000; i <= 200000; i += 2000){
			Analyzer analyzer = new Analyzer(new Supplier[]{
				() -> FormatArrayProvider.ARRAY_LIST(),
				() -> FormatArrayProvider.LINKED_LIST(),
				() -> FormatArrayProvider.ADAPTIVE_ARRAY()
			}, i, 50, "reports/AdaptiveArray/Triple/Report");
			
			analyzer.runDefault();
		}
	}

	private static boolean checkCorrectness(int times, Supplier<RunTest.FormatArray<?>> factory1, Supplier<RunTest.FormatArray<?>> factory2){
		boolean isFailed = false;
		while(times -- > 0 && !isFailed){
			RunTest.FormatArray<?> array1 = factory1.get();
			RunTest.FormatArray<?> array2 = factory2.get();

			int randomLength = (new Random()).nextInt(100000);
			System.out.printf("Round %d with length %d\n", times, randomLength);
			if(!RunTest.testCorrectness(randomLength, array1, array2) || !RunTest.testGetCorrectness(randomLength, array1, array2))
				isFailed = true;
		}
		if(!isFailed)
			System.out.println("All Test Passed!");
		return !isFailed;
	}

	private static class FormatArrayProvider{
		private static RunTest.FormatArray<ArrayList<Integer>> ARRAY_LIST(){
			return new RunTest.FormatArray<>("Array List", new ArrayList<Integer>()
				, (array, value) -> { return array.size() != 0 ? array.get(value % array.size()) : null; }
				, (array, value) -> { array.add(value); }
				, (array, value) -> { array.add(0, value); }
				, (array, value) -> { return array.size() != 0 ? array.remove(array.size()-1) : null; }
				, (array, value) -> { return array.size() != 0 ? array.remove(0) : null; });
		}

		private static RunTest.FormatArray<LinkedList<Integer>> LINKED_LIST(){
			return new RunTest.FormatArray<>("Linked List", new LinkedList<Integer>()
				, (array, value) -> { return array.size() != 0 ? array.get(value % array.size()) : null; }
				, (array, value) -> { array.addLast(value); }
				, (array, value) -> { array.addFirst(value); }
				, (array, value) -> { return array.size() != 0 ? array.removeLast() : null; }
				, (array, value) -> { return array.size() != 0 ? array.removeFirst() : null; });
		}

		private static RunTest.FormatArray<LinkArray<Integer>> LINK_ARRAY(){
			return new RunTest.FormatArray<>("Link Array", new LinkArray<Integer>()
				, (array, value) -> { return array.length() != 0 ? array.get(value % array.length()) : null; }
				, (array, value) -> { array.push(value); }
				, (array, value) -> { array.unshift(value); }
				, (array, value) -> { return array.length() != 0 ? array.pop() : null; }
				, (array, value) -> { return array.length() != 0 ? array.shift() : null; });
		}

		private static RunTest.FormatArray<LinkArray<Integer>> AUTO_LINK_ARRAY(){
			return new RunTest.FormatArray<>("Auto Link Array", new AutoLinkArray<Integer>()
				, (array, value) -> { return array.length() != 0 ? array.get(value % array.length()) : null; }
				, (array, value) -> { array.push(value); }
				, (array, value) -> { array.unshift(value); }
				, (array, value) -> { return array.length() != 0 ? array.pop() : null; }
				, (array, value) -> { return array.length() != 0 ? array.shift() : null; });
		}

		private static RunTest.FormatArray<AdaptiveArray<Integer>> ADAPTIVE_ARRAY(){
			return new RunTest.FormatArray<>("Adaptive Array", new AdaptiveArray<Integer>()
				, (array, value) -> { return array.length() != 0 ? array.get(value % array.length()) : null; }
				, (array, value) -> { array.push(value); }
				, (array, value) -> { array.unshift(value); }
				, (array, value) -> { return array.length() != 0 ? array.pop() : null; }
				, (array, value) -> { return array.length() != 0 ? array.shift() : null; });
		}
	}
}