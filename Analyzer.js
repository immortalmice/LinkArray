const RunTest = require("./RunTest.js");
const FS = require('fs');

const COMMANDS = [
	{ list: ["GET"], preFilling: true },
	{ list: ["PUSH"], preFilling: false },
	{ list: ["UNSHIFT"], preFilling: false },
	{ list: ["POP"], preFilling: true },
	{ list: ["SHIFT"], preFilling: true },
	{ list: ["GET", "PUSH", "POP"], preFilling: true },
	{ list: ["GET", "PUSH", "POP"], preFilling: false },
	{ list: ["GET", "PUSH", "UNSHIFT"], preFilling: false },
	{ list: ["GET", "SHIFT", "UNSHIFT"], preFilling: true },
	{ list: ["GET", "SHIFT", "UNSHIFT"], preFilling: false },
	{ list: ["GET", "POP", "SHIFT"], preFilling: true },
	{ list: ["GET", "PUSH", "UNSHIFT", "POP", "SHIFT"], preFilling: true },
	{ list: ["GET", "PUSH", "UNSHIFT", "POP", "SHIFT"], preFilling: false }
];

module.exports = class Analyzer{
	constructor(arrayFactorysIn, unitAmountIn, sampleAmountIn, outputPathIn){
		this.arrayFactorys = arrayFactorysIn;
		this.unitAmount = unitAmountIn;
		this.sampleAmount = sampleAmountIn;
		this.outputPath = outputPathIn;
	}

	runWithCommands(commandsElement, progressLogger){
		let results = [];
		let commands = commandsElement.list;

		for(let i = 1; i <= this.sampleAmount; i ++){
			let instances = this.arrayFactorys.map((factory) => factory());

			try{
				if(progressLogger) progressLogger.setAndLog({type: "descript", value: "Runing GC..."});
				if(global.gc) global.gc();
				if(progressLogger) progressLogger.end("descript");
			}catch(e){
				console.log(e);
			}

			if(progressLogger) progressLogger.set({type: "sampleProgress", value: i});

			if(commandsElement.preFilling){
				if(progressLogger)
					progressLogger.setAndLog({type: "descript", value: "Filling..."});

				RunTest.randomFill(this.unitAmount, true, ...instances);

				if(progressLogger)
					progressLogger.end("descript");
			}

			if(progressLogger)
				progressLogger.setAndLog({type: "descript", value: "Generating Tests..."});

			let testArray = RunTest.genTest(this.unitAmount, commands);
			if(progressLogger)
				progressLogger.setAndLog({type: "descript", value: "Testing..."});

			results.push(RunTest.runTest(testArray, false, ...instances));

			if(progressLogger)
				progressLogger.end("descript");
		}
		return this.analyze(results);
	}

	analyze(results){
		let report = [];
		for(let i = 0; i <= this.arrayFactorys.length-1; i ++){
			report.push(new AnalyzeResult(
				Math.max(...results.map((item) => {
					return parseSecond(item[i]);
				})),

				Math.min(...results.map((item) => {
					return parseSecond(item[i]);
				})),

				results.map((item) => {
					return parseSecond(item[i]);
				}).reduce((accumulator, currentValue) => {
					return accumulator + currentValue;
				}) / results.length
			));
		}
		return report;
	}

	runDefault(){
		let instances = this.arrayFactorys.map((factory) => factory());

		let dateStr = (new Date()).toUTCString().replace(/:/g, "-");
		this.outputPath += " - " + dateStr + ".txt";

		let progressLogger = new ProgressLogger();
		FS.appendFileSync(this.outputPath, "=========================================================================================\n\n");
		FS.appendFileSync(this.outputPath, dateStr + "\n");
		FS.appendFileSync(this.outputPath, "Test Unit Amount: " + this.unitAmount + "\n");
		FS.appendFileSync(this.outputPath, "Sample Amount: " + this.sampleAmount + "\n\n");

		let autoArray = instances.find((instance) => instance.getName() === "Auto Link Array");
		if(autoArray){
			FS.appendFileSync(this.outputPath, "Auto List Array Parameters: " + autoArray.getArray().refactorBound + "\n\n");
		}
		
		let winResult = [];
		instances.forEach(() => winResult.push([]));

		COMMANDS.forEach((commands, index) => {
			progressLogger.set({type: "commands", value: commands.list.toString()});
			progressLogger.set({type: "state", value: index});
			progressLogger.set({type: "sampleAmount", value: this.sampleAmount});

			FS.appendFileSync(this.outputPath, "-----------------------------------------------------------------------------------------\n\n");
			FS.appendFileSync(this.outputPath, "Running Command [" + commands.list.join(", ") + "]" + (commands.preFilling ? " With PreFilling" : "") + " :\n\n");
			progressLogger.log();

			let result = this.runWithCommands(commands, progressLogger);
			instances.forEach((instance, index) => {
				FS.appendFileSync(this.outputPath, instance.getName() + " ".repeat(20 - instance.getName().length) + "|" + result[index].toString() + "\n");
			});
			FS.appendFileSync(this.outputPath, "\n");

			let winIndex = result.reduce((accumulator, currentValue, currentIndex) => {
				return result[accumulator].averge < currentValue.averge ? accumulator : currentIndex;
			}, 0);

			FS.appendFileSync(this.outputPath, "Winner: " + instances[winIndex].getName() + "\n");

			let ratio;
			if(instances.length === 2){
				ratio = result[0].averge / result[1].averge;
				ratio = ratio < 1 ? 1 / ratio : ratio;
				FS.appendFileSync(this.outputPath, "Ratio: " + ratio.toFixed(4) + "\n\n");
			}

			winResult[winIndex].push(Object.assign({
					ratio: ratio
				}, commands));
		});
		FS.appendFileSync(this.outputPath, "-----------------------------------------------------------------------------------------\n\n");

		winResult.forEach((winArray, index) => {
			FS.appendFileSync(this.outputPath, instances[index].getName() + " Win Cases:\n");
			winArray.forEach((element) => {
				let preStr = "[" + element.list.join(", ") + "]" + (element.preFilling ? " With PreFilling" : "") + " :";
				let postStr = element.ratio ? element.ratio.toFixed(4) : "";

				FS.appendFileSync(this.outputPath, preStr + " ".repeat(89 - preStr.length - postStr.length) + postStr + "\n");
			});

			FS.appendFileSync(this.outputPath, "\n");
		});
		
		FS.appendFileSync(this.outputPath, "=========================================================================================\n");
	}
}

function parseSecond(data){
	return data[0] + data[1] / 1000000000;
}

function formatSecondSring(secondFloat){
	let second = parseInt(secondFloat, 10).toString();
	let milliSecond = ((secondFloat - second) * 1000).toFixed(2);
	return " ".repeat(3 - second.length) + second + "s" + " ".repeat(7 - milliSecond.length) + milliSecond + "ms ";
}

class AnalyzeResult{
	constructor(maxIn, minIn, avergeIn){
		this.max = maxIn;
		this.min = minIn;
		this.averge = avergeIn;
	}

	toString(){
		let str = "";
		str += "| Max: " + formatSecondSring(this.max);
		while(str.length < 20) str += " ";
		str += "| Min: " + formatSecondSring(this.min);
		while(str.length < 40) str += " ";
		str += "| Averge: " + formatSecondSring(this.averge);
		while(str.length < 65) str += " ";
		str += "||"
		return str;
	}
}

class ProgressLogger{
	set(info){
		this[info.type] = info.value;
	}

	end(type){
		this[type] = undefined;
	}

	log(){
		console.clear();
		let logStrs = [];

		ProgressLogger.newLine(logStrs);
		if(this.commands !== undefined){
			ProgressLogger.append(logStrs, "Running Command: " + this.commands.toString());
		}

		if(this.state !== undefined){
			ProgressLogger.append(logStrs, " (" + (this.state + 1) + "/" + COMMANDS.length + ")");
		}

		if(this.sampleProgress !== undefined && this.sampleAmount !== undefined){
			ProgressLogger.newLine(logStrs);
			ProgressLogger.append(logStrs, "Sample: " + this.sampleProgress + "/" + this.sampleAmount);
		}

		if(this.descript !== undefined){
			ProgressLogger.newLine(logStrs);
			ProgressLogger.append(logStrs, this.descript);
		}

		for(let i = 0; i <= logStrs.length-1; i ++){
			console.log(logStrs[i]);
		}
	}

	setAndLog(info){
		this.set(info);
		this.log();
	}

	static newLine(logStrs){
		logStrs[logStrs.length] = "";
	}

	static append(logStrs, str){
		logStrs[logStrs.length-1] += str;
	}
}