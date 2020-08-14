const PHARSER_FOLDER = require("./ReportJsonPharserFolder");

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

parseReports();

function parseReports(config){
	config = config || JAVASCRIPT_CONFIG;

	let pharserFolder;

	/* ================================================Auto Link Array==================================================================== */

	pharserFolder = new PHARSER_FOLDER(config.languageFolder + "/Auto Link Array/" + config.folderNames[0], ["../" + config.languageFolder + "/reports/AutoLinkArray/" + config.folderNames[0]]);
	pharserFolder.pharseReport(["Auto Link Array Win Cases", config.parameters[0] + " Win Cases"]);

	pharserFolder = new PHARSER_FOLDER(config.languageFolder + "/Auto Link Array/" + config.folderNames[1], ["../" + config.languageFolder + "/reports/AutoLinkArray/" + config.folderNames[1]]);
	pharserFolder.pharseReport(["Auto Link Array Win Cases", config.parameters[1] + " Win Cases"]);

	pharserFolder = new PHARSER_FOLDER(config.languageFolder + "/Auto Link Array/" + config.folderNames[2], ["../" + config.languageFolder + "/reports/AutoLinkArray/" + config.folderNames[2]]);
	pharserFolder.pharseTimeReport([config.parameters[0], config.parameters[1], "Auto Link Array"]);

	/* ================================================Adaptive Array==================================================================== */

	pharserFolder = new PHARSER_FOLDER(config.languageFolder + "/Adaptive Array/" + config.folderNames[0], ["../" + config.languageFolder + "/reports/AdaptiveArray/" + config.folderNames[0]]);
	pharserFolder.pharseReport(["Adaptive Array Win Cases", config.parameters[0] + " Win Cases"]);

	pharserFolder = new PHARSER_FOLDER(config.languageFolder + "/Adaptive Array/" + config.folderNames[1], ["../" + config.languageFolder + "/reports/AdaptiveArray/" + config.folderNames[1]]);
	pharserFolder.pharseReport(["Adaptive Array Win Cases", config.parameters[1] + " Win Cases"]);

	pharserFolder = new PHARSER_FOLDER(config.languageFolder + "/Adaptive Array/" + config.folderNames[2], ["../" + config.languageFolder + "/reports/AdaptiveArray/" + config.folderNames[2]]);
	pharserFolder.pharseTimeReport([config.parameters[0], config.parameters[1], "Adaptive Array"]);
}
