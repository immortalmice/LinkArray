package com.github.immortalmice.linkarray.java.analyze;

import java.util.ArrayList;
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

	public static class TestUnit{
		private String command;
		private int value;

		public TestUnit(String commandIn, int valueIn){
			this.command = commandIn;
			this.value = valueIn;
		}

		public String getCommand(){ return this.command; }
		public int getValue(){ return this.value; }
	}

	public static class FromatArray<T>{
		private String name;
		private T array;

		private BiFunction<T, Integer, Integer> getFunction;
		private BiConsumer<T, Integer> pushFunction;
		private BiConsumer<T, Integer> unshiftFunction;
		private BiConsumer<T, Integer> popFunction;
		private BiConsumer<T, Integer> shiftFunction;

		public FromatArray(String nameIn, T initialIn
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