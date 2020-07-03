const LinkArray = require("./LinkArray.js");
const AutoLinkArray = require("./AutoLinkArray.js");
const RunTest = require("./RunTest.js");
const Analyzer = require("./Analyzer.js");

const OUTPUT_PATH = "reports/Report"

let analyzer = new Analyzer(
		function(){ return RunTest.getFormatArray("Normal Array", []); }
		, function(){ return RunTest.getFormatArray("Auto Link Array", new AutoLinkArray(5000)); }
		, 1000000, 10, OUTPUT_PATH
	);

analyzer.runDefault();

// let normalArray = RunTest.getFormatArray("Normal Array", []);
// let autoArray = RunTest.getFormatArray("Auto Link Array", new AutoLinkArray());

// let testArray = RunTest.genTest(100000, ["GET", "PUSH", "UNSHIFT", "POP", "SHIFT"]);
// RunTest.runTest(testArray, true, normalArray, autoArray);
// RunTest.testCorrectness(normalArray, autoArray);