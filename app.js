const LinkArray = require("./LinkArray.js");
const AutoLinkArray = require("./AutoLinkArray.js");
const DoublyLinkedList = require("../double_link_list/index.js");
const RunTest = require("./RunTest.js");
const Analyzer = require("./Analyzer.js");

const OUTPUT_PATH = "reports/Report"

let analyzer = new Analyzer(
		function(){ return RunTest.getFormatArray("Normal Array", []); }
		, function(){ return RunTest.getFormatArray("Auto Link Array", new AutoLinkArray(5000)); }
		, 1000000, 10, OUTPUT_PATH
	);

analyzer.runDefault();

function getNormalFormatArray(){
	return RunTest.getFormatArray("Normal Array", []);
}

function getAutoFormatArray(i){
	return RunTest.getFormatArray("Auto Link Array", new AutoLinkArray(i), {
		"GET": (array, value) => array.get(value % array.length)
	});
}

function getDoublyFormatArray(){
	return RunTest.getFormatArray("Doubly Linked List", new DoublyLinkedList(), {
		"PUSH": (array, value) => array.insert(value) ? array.size : 0,
		"UNSHIFT": (array, value) => array.insertFirst(value) ? array.size : 0,
		"POP": (array) => {
			let result = array.remove();
			return result ? result.getData() : undefined;
		},
		"SHIFT": (array) => {
			let result = array.removeFirst();
			return result ? result.getData() : undefined;
		},
		"GET": (array, value) => {
			let result = array.findAt(value % array.size);
			return result === -1 ? undefined : result.getData();
		}
	});
}
