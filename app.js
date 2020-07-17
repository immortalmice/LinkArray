const LinkArray = require("./LinkArray.js");
const AutoLinkArray = require("./AutoLinkArray.js");
const AdaptiveArray = require("./AdaptiveArray.js");
const DoublyLinkedList = require("../double_link_list/index.js");
const RunTest = require("./RunTest.js");
const Analyzer = require("./Analyzer.js");


while(RunTest.testCorrectnessWithArray(
		RunTest.genTest(Math.floor(Math.random() * 100000), ["GET", "PUSH", "UNSHIFT", "POP", "SHIFT"])
		, getLinkFormatArray(), getAdaptiveFormatArray()
		)){
	
}

function getNormalFormatArray(){
	return RunTest.getFormatArray("Normal Array", []);
}

function getAutoFormatArray(i){
	return RunTest.getFormatArray("Auto Link Array", new AutoLinkArray(i), {
		"GET": (array, value) => array.get(array.length === 0 ? 0 : value % array.length)
	});
}

function getAdaptiveFormatArray(i, j) {
	return RunTest.getFormatArray("Adaptive Array", new AdaptiveArray(i, j), {
		"GET": (array, value) => array.get(array.length === 0 ? 0 : value % array.length)
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
			let result = array.findAt(array.size === 0 ? 0 : value % array.size);
			return result === -1 ? undefined : result.getData();
		}
	});
}
