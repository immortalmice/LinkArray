const LinkArray = require("./LinkArray.js");
const AutoLinkArray = require("./AutoLinkArray.js");
const RunTest = require("./RunTest");

let linkArray = RunTest.getFormatArray("Link Array", new LinkArray());
let autoLinkArray = RunTest.getFormatArray("Auto Link Array", new AutoLinkArray());
let normalArray = RunTest.getFormatArray("Normal Array", []);

let testArray = RunTest.genTest(500000, ["GET"]);

RunTest.randomFill(500000, normalArray, linkArray, autoLinkArray);
RunTest.runTest(testArray, normalArray, linkArray, autoLinkArray);

/*
RunTest.randomFill(1000, linkArray, normalArray);
RunTest.testCorrectness(linkArray, normalArray);
*/