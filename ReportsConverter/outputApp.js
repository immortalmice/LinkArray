const PARSER_FOLDER = require("./ReportJsonParserFolder");

const JAVASCRIPT_CONFIG = {
	languageFolder: "JavaScript",
	folderNames: [
		"NormalArray",
		"DoublyLinkedList",
		"Triple"
	],
	parameters: [
		"Normal Array",
		"Doubly Linked List"
	]
};

const JAVA_CONFIG = {
	languageFolder: "Java",
	folderNames: [
		"ArrayList",
		"LinkedList",
		"Triple"
	],
	parameters: [
		"Array List",
		"Linked List"
	]
};

parseReports(JAVA_CONFIG);

function parseReports(config){
	config = config || JAVASCRIPT_CONFIG;

	let parserFolder;

	/* ================================================Auto Link Array==================================================================== */

	parserFolder = new PARSER_FOLDER(config.languageFolder + "/Auto Link Array/" + config.folderNames[0], ["../" + config.languageFolder + "/reports/AutoLinkArray/" + config.folderNames[0]]);
	parserFolder.parseReport(["Auto Link Array Win Cases", config.parameters[0] + " Win Cases"]);

	parserFolder = new PARSER_FOLDER(config.languageFolder + "/Auto Link Array/" + config.folderNames[1], ["../" + config.languageFolder + "/reports/AutoLinkArray/" + config.folderNames[1]]);
	parserFolder.parseReport(["Auto Link Array Win Cases", config.parameters[1] + " Win Cases"]);

	parserFolder = new PARSER_FOLDER(config.languageFolder + "/Auto Link Array/" + config.folderNames[2], ["../" + config.languageFolder + "/reports/AutoLinkArray/" + config.folderNames[2]]);
	parserFolder.parseTimeReport([config.parameters[0], config.parameters[1], "Auto Link Array"]);

	/* ================================================Adaptive Array==================================================================== */

	parserFolder = new PARSER_FOLDER(config.languageFolder + "/Adaptive Array/" + config.folderNames[0], ["../" + config.languageFolder + "/reports/AdaptiveArray/" + config.folderNames[0]]);
	parserFolder.parseReport(["Adaptive Array Win Cases", config.parameters[0] + " Win Cases"]);

	parserFolder = new PARSER_FOLDER(config.languageFolder + "/Adaptive Array/" + config.folderNames[1], ["../" + config.languageFolder + "/reports/AdaptiveArray/" + config.folderNames[1]]);
	parserFolder.parseReport(["Adaptive Array Win Cases", config.parameters[1] + " Win Cases"]);

	parserFolder = new PARSER_FOLDER(config.languageFolder + "/Adaptive Array/" + config.folderNames[2], ["../" + config.languageFolder + "/reports/AdaptiveArray/" + config.folderNames[2]]);
	parserFolder.parseTimeReport([config.parameters[0], config.parameters[1], "Adaptive Array"]);
}
