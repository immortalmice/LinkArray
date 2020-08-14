package com.github.immortalmice.linkarray.java.analyze;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;
import java.util.stream.Collectors;


public class Analyzer{
	public static final CommandTerm[] COMMANDS = {
		new CommandTerm(new String[]{"GET"}, true),
		new CommandTerm(new String[]{"PUSH"}, false),
		new CommandTerm(new String[]{"UNSHIFT"}, false),
		new CommandTerm(new String[]{"POP"}, true),
		new CommandTerm(new String[]{"SHIFT"}, true),
		new CommandTerm(new String[]{"GET", "PUSH", "UNSHIFT"}, false),
		new CommandTerm(new String[]{"GET", "POP", "SHIFT"}, true),
		new CommandTerm(new String[]{"GET", "PUSH", "POP"}, true),
		new CommandTerm(new String[]{"GET", "PUSH", "POP"}, false),
		new CommandTerm(new String[]{"GET", "SHIFT", "UNSHIFT"}, true),
		new CommandTerm(new String[]{"GET", "SHIFT", "UNSHIFT"}, false),
		new CommandTerm(new String[]{"GET", "PUSH", "UNSHIFT", "POP", "SHIFT"}, true),
		new CommandTerm(new String[]{"GET", "PUSH", "UNSHIFT", "POP", "SHIFT"}, false)
	};

	private Supplier<RunTest.FormatArray<?>>[] arrayFactorys;
	private int unitAmount;
	private int sampleAmount;
	private String outputPath;

	private ProcessLogger processLogger = new ProcessLogger();

	public Analyzer(Supplier<RunTest.FormatArray<?>>[] arrayFactorysIn, int unitAmountIn, int sampleAmountIn, String outputPathIn){
		this.arrayFactorys = arrayFactorysIn;
		this.unitAmount = unitAmountIn;
		this.sampleAmount = sampleAmountIn;
		this.outputPath = outputPathIn;
	}

	public AnalyzeResult runWithCommands(CommandTerm commands){
		long[][] results = new long[this.sampleAmount][];

		for(int i = 0; i <= this.sampleAmount-1; i ++){
			this.processLogger.sampleProgress = i;
			this.processLogger.log();

			RunTest.FormatArray<?>[] arrays = this.getNewInstances();

			if(commands.preFilling){
				System.out.println("Filling...");
				RunTest.randomFill(this.unitAmount, true, arrays);
			}

			System.out.println("Generating Tests...");
			List<RunTest.TestUnit> testList = RunTest.genTest(this.unitAmount, commands.list);

			System.out.println("Testing...");
			results[i] = RunTest.runTest(testList, arrays);
		}

		return new AnalyzeResult(results);
	}

	public void runDefault(){
		String[] arrayNames = Arrays.stream(this.getNewInstances()).map((array) -> array.getName()).toArray(String[]::new);

		String dataStr = (new SimpleDateFormat("E, dd MM yyyy HH-mm-ss z", Locale.ROOT)).format(new Date());
		this.outputPath += " - " + dataStr + ".txt";

		try{
			File file = new File(this.outputPath);
			FileWriter fileWriter = new FileWriter(file, true);
			PrintWriter printWriter = new PrintWriter(fileWriter);

			file.getParentFile().mkdirs();

			printWriter.printf("=========================================================================================\n\n");
			printWriter.printf(dataStr + "\n");
			printWriter.printf("Test Unit Amount: %d\n", this.unitAmount);
			printWriter.printf("Sample Amount: %d\n\n", this.sampleAmount);

			@SuppressWarnings("unchecked")
			List<WinResult>[] winResult = (List<WinResult>[]) new List[arrayNames.length];
			Arrays.setAll(winResult, (i) -> new ArrayList<>());

			for(int index = 0; index <= Analyzer.COMMANDS.length-1; index ++){
				CommandTerm commands = Analyzer.COMMANDS[index];

				this.processLogger.commands = commands.toString();
				this.processLogger.state = index;

				printWriter.printf("-----------------------------------------------------------------------------------------\n\n");
				printWriter.printf("Running Command " + commands.toString() + (commands.preFilling ? " With PreFilling" : "") + " :\n\n");

				AnalyzeResult result = this.runWithCommands(commands);
				for(int arrayIndex = 0; arrayIndex <= arrayNames.length-1; arrayIndex ++){
					printWriter.printf("%s%s|%s\n", arrayNames[arrayIndex], new String(new char[20 - arrayNames[arrayIndex].length()]).replace("\0", " "), result.toString(arrayIndex));
				}

				int winIndex = Arrays.stream(result.averge).boxed().collect(Collectors.toList()).indexOf(Arrays.stream(result.averge).min().getAsLong());
				printWriter.printf("\nWinner: %s\n", arrayNames[winIndex]);

				double ratio = -1;
				if(arrayNames.length == 2){
					ratio = (double) result.averge[0] / result.averge[1];
					ratio = ratio < 1 ? 1 / ratio : ratio;
					printWriter.printf("Ratio: %.2f\n\n", ratio);
				}
				winResult[winIndex].add(new WinResult(commands, ratio));
			}
			printWriter.printf("-----------------------------------------------------------------------------------------\n\n");

			for(int index = 0; index <= arrayNames.length-1; index ++){
				printWriter.printf("%s Win Cases:\n", arrayNames[index]);

				List<WinResult> winList = winResult[index];
				winList.forEach((result) -> {
					String preStr = result.commands.toString() + (result.commands.preFilling ? " With PreFilling" : "") + " :";
					String postStr = result.ratio != -1 ? (new DecimalFormat("0.####")).format(result.ratio) : "";

					printWriter.printf(preStr + new String(new char[89 - preStr.length() - postStr.length()]).replace("\0", " ") + postStr + "\n");
				});

				printWriter.printf("\n");
			}

			printWriter.printf("=========================================================================================\n");

			System.out.printf("=========================================================================================\n");
			System.out.printf("All Test Finised!\n");

			printWriter.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	private RunTest.FormatArray<?>[] getNewInstances(){
		return Arrays.stream(this.arrayFactorys).map(factory -> factory.get()).toArray(RunTest.FormatArray<?>[]::new);
	}

	private static class CommandTerm{
		private final String[] list;
		private final boolean preFilling;

		private CommandTerm(String[] listIn, boolean preFillingIn){
			this.list = listIn;
			this.preFilling = preFillingIn;
		}
		
		public String toString(){ return "[" + String.join(", ", this.list) + "]"; }
	}

	private class ProcessLogger{
		private String commands = "";
		private int state = 0;
		private int sampleProgress = 0;
		private String desciption = "";

		private void log(){
			System.out.printf("=========================================================================================\n");
			System.out.printf("Running Command: %s (%d/%d)\n", this.commands.toString(), this.state + 1, Analyzer.COMMANDS.length);
			System.out.printf("Sample: %d/%d\n", this.sampleProgress + 1, Analyzer.this.sampleAmount);
			System.out.printf(this.desciption);
		}
	}

	private class AnalyzeResult{
		private long[] max = new long[Analyzer.this.arrayFactorys.length];
		private long[] min = new long[Analyzer.this.arrayFactorys.length];
		private long[] averge = new long[Analyzer.this.arrayFactorys.length];

		public AnalyzeResult(long[][] resultsIn){
			for(int i = 0; i <= Analyzer.this.arrayFactorys.length-1; i ++){
				final int index = i;
				max[i] = Arrays.stream(resultsIn).map(sample -> sample[index]).max(Comparator.comparingLong((integer) -> integer)).get();
				min[i] = Arrays.stream(resultsIn).map(sample -> sample[index]).min(Comparator.comparingLong((integer) -> integer)).get();
				averge[i] = Arrays.stream(resultsIn).mapToLong(sample -> sample[index]).sum() / Analyzer.this.sampleAmount;
			}
		}

		public String toString(int index){
			String str = "";
			str += "| Max: " + RunTest.formatSecondSring(this.max[index]);
			while(str.length() < 20) str += " ";
			str += "| Min: " + RunTest.formatSecondSring(this.min[index]);
			while(str.length() < 40) str += " ";
			str += "| Averge: " + RunTest.formatSecondSring(this.averge[index]);
			while(str.length() < 65) str += " ";
			str += "||";
			return str;
		}
	}

	private class WinResult{
		private CommandTerm commands;
		private double ratio;

		public WinResult(CommandTerm commandsIn, double ratioIn){
			this.commands = commandsIn;
			this.ratio = ratioIn;
		} 
	}
}