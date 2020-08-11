package com.github.immortalmice.linkarray.java.analyze;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
}