package com.github.immortalmice.linkarray.java.analyze;

import java.text.DecimalFormat;
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

	public static void randomFill(int length, boolean showProgress, FormatArray<?> ...arrays){
		if(showProgress){
			System.out.println("----------------------------");
			System.out.println("Filling");
		}

		List<TestUnit> data = RunTest.genTest(length, new String[]{"PUSH", "UNSHIFT"});
		float length_fifth = data.size() / 5;

		for(int i = 0; i <= arrays.length-1; i ++){
			FormatArray<?> array = arrays[i];
			for(int j = 0; j <= data.size()-1; j ++){
				TestUnit unit = data.get(j);
				array.run(unit);
				if(showProgress && Math.floor((j + 1) / length_fifth) - Math.floor(j / length_fifth) > 0){
					System.out.printf("%s: %d / %d | %d%%\n"
						, array.getName(), i + 1, arrays.length, (int)((float) j / (data.size() - 1) * 100));
				}
			};
		}
	}

	public static long[] runTest(List<TestUnit> testArray, FormatArray<?> ...arrays){
		System.out.println("");

		long[] results = new long[arrays.length];
		for(int i = 0; i <= arrays.length-1; i ++){
			FormatArray<?> array = arrays[i];

			long startTime = System.nanoTime();
			testArray.forEach((unit) -> {
				array.run(unit);
			});
			long endTime = System.nanoTime();

			results[i] = endTime - startTime;

			System.out.printf("%s: %s\n", array.getName(), RunTest.formatSecondSring(results[i]));
		}

		System.out.println("");
		return results;
	}

	public static String formatSecondSring(long nanoSecond){
		String second = String.valueOf(nanoSecond / 1000000000);
		String milliSecond = (new DecimalFormat("0.##")).format(((float)nanoSecond / 1000000) % 1000);
		return String.format("%s%ss%s%sms "
			, new String(new char[3 - second.length()]).replace("\0", " ")
			, second
			, new String(new char[7 - milliSecond.length()]).replace("\0", " ")
			, milliSecond);
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
		private BiFunction<T, Integer, Integer> popFunction;
		private BiFunction<T, Integer, Integer> shiftFunction;

		public FormatArray(String nameIn, T initialIn
			, BiFunction<T, Integer, Integer> getFunctionIn
			, BiConsumer<T, Integer> pushFunctionIn
			, BiConsumer<T, Integer> unshiftFunctionIn
			, BiFunction<T, Integer, Integer> popFunctionIn
			, BiFunction<T, Integer, Integer> shiftFunctionIn){

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
					return this.popFunction.apply(this.array, testUnit.getValue());
				case "SHIFT":
					return this.shiftFunction.apply(this.array, testUnit.getValue());
			}
			return null;
		}

		public String getName(){ return this.name; }
		public T getArray(){ return this.array; }
	}
}