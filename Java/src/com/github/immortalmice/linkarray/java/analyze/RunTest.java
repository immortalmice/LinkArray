package com.github.immortalmice.linkarray.java.analyze;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class RunTest{
	public static List<TestUnit> genTest(int length, String[] types){
		List<TestUnit> testArray = new ArrayList<>();
		for(int i = 1; i <= length; i ++){
			testArray.add(new TestUnit(RunTest.genRandomType(types), RunTest.genRandomValue(length)));
		}
		return testArray;
	}

	public static String genRandomType(String[] types){
		return types[(new Random()).nextInt(types.length)];
	}

	public static int genRandomValue(){
		return RunTest.genRandomValue(102400);
	}
	
	public static int genRandomValue(int upperBound){
		return (new Random()).nextInt(upperBound);
	}

	public static boolean testCorrectnessWithList(List<TestUnit> testArray, FormatArray<?> array1, FormatArray<?> array2){
		Iterator<TestUnit> iterator = testArray.iterator();
		while(iterator.hasNext()){
			TestUnit testUnit = iterator.next();

			Object result1 = array1.run(testUnit);
			Object result2 = array2.run(testUnit);

			if(result1 != null && result2 != null && !result1.equals(result2)){
				System.out.printf("Failed => %s\n", testUnit.toString());
				System.out.printf("%s: %d\n", array1.getName(), result1);
				System.out.printf("%s: %d\n", array2.getName(), result2);
				return false;
			}
		}
		return true;
	}

	public static boolean testGetCorrectness(int length, FormatArray<?> array1, FormatArray<?> array2){
		List<TestUnit> testArray = new ArrayList<>();
		for(int i = 0; i <= length-1; i ++){
			testArray.add(new TestUnit("GET", i));
		}
		return RunTest.testCorrectnessWithList(testArray, array1, array2);
	}

	public static boolean testCorrectness(int length, FormatArray<?> array1, FormatArray<?> array2){
		List<TestUnit> testArray = RunTest.genTest(length, new String[]{"GET", "PUSH", "UNSHIFT", "POP", "SHIFT"});
		return RunTest.testCorrectnessWithList(testArray, array1, array2);
	}

	public static class TestUnit{
		private String command;
		private int value;

		public TestUnit(String commandIn, int valueIn){
			this.command = commandIn;
			this.value = valueIn;
		}

		public String getCommand(){ return this.command; }
		public int getValue(){ return this.value; }
		public String toString(){ return String.format("%s: %d", this.command, this.value); }
	}

	public static class FormatArray<T>{
		private String name;
		private T array;

		private BiFunction<T, Integer, Integer> getFunction;
		private BiConsumer<T, Integer> pushFunction;
		private BiConsumer<T, Integer> unshiftFunction;
		private BiConsumer<T, Integer> popFunction;
		private BiConsumer<T, Integer> shiftFunction;

		public FormatArray(String nameIn, T initialIn
			, BiFunction<T, Integer, Integer> getFunctionIn
			, BiConsumer<T, Integer> pushFunctionIn
			, BiConsumer<T, Integer> unshiftFunctionIn
			, BiConsumer<T, Integer> popFunctionIn
			, BiConsumer<T, Integer> shiftFunctionIn){

			this.name = nameIn;
			this.array = initialIn;

			this.getFunction = getFunctionIn;
			this.pushFunction = pushFunctionIn;
			this.unshiftFunction = unshiftFunctionIn;
			this.popFunction = popFunctionIn;
			this.shiftFunction = shiftFunctionIn;
		}

		public Object run(TestUnit testUnit){
			switch(testUnit.getCommand()){
				case "GET":
					return this.getFunction.apply(this.array, testUnit.getValue());
				case "PUSH":
					this.pushFunction.accept(this.array, testUnit.getValue());
					break;
				case "UNSHIFT":
					this.unshiftFunction.accept(this.array, testUnit.getValue());
					break;
				case "POP":
					this.popFunction.accept(this.array, testUnit.getValue());
					break;
				case "SHIFT":
					this.shiftFunction.accept(this.array, testUnit.getValue());
					break;
			}
			return null;
		}

		public String getName(){ return this.name; }
		public T getArray(){ return this.array; }
	}
}