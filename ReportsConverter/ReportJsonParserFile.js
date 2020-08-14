const FS = require('fs');

module.exports = class ReportJsonParserFile{
	static PRIME_FIELD = [
		"Test Unit Amount",
		"Sample Amount",
		"Auto List Array Parameters"
	];

	static SUB_FIELD = [
		"[GET] With PreFilling :",
		"[PUSH] :",
		"[UNSHIFT] :",
		"[POP] With PreFilling :",
		"[SHIFT] With PreFilling :",
		"[GET, PUSH, UNSHIFT] :",
		"[GET, POP, SHIFT] With PreFilling :",
		"[GET, PUSH, POP] With PreFilling :",
		"[GET, PUSH, POP] :",
		"[GET, SHIFT, UNSHIFT] With PreFilling :",
		"[GET, SHIFT, UNSHIFT] :",
		"[GET, PUSH, UNSHIFT, POP, SHIFT] With PreFilling :",
		"[GET, PUSH, UNSHIFT, POP, SHIFT] :"
	];

	static SORT_FIELD_ARRAY = [
		...ReportJsonParserFile.PRIME_FIELD, 
		...(ReportJsonParserFile.SUB_FIELD.map((str) => str.substring(0, str.length-2)))
	];

	constructor(pathIn){
		this.path = pathIn;
	}

	getContent(){
		return FS.readFileSync(this.path);
	}

	parseReport(targets){

		let FILE_CONTENT = this.getContent();
		let result = this.getPrimeField();

		targets.forEach((NAME_SPACE, index) => {
			let namePos = FILE_CONTENT.indexOf(NAME_SPACE);
			let rest = String(FILE_CONTENT.slice(namePos));

			ReportJsonParserFile.SUB_FIELD.forEach((FIELD) => {
				let pos = rest.indexOf(FIELD);
				if(pos !== -1){
					let value = findValue(rest.slice(pos));
					if(!result[FIELD.substring(0, FIELD.length-2)])
						result[FIELD.substring(0, FIELD.length-2)] = index === 1 ? -1 * value : value;
				}
			});
		});

		return this.sort(result);
	}

	parseTime(target){
		target = target || "Adaptive Array";

		let FILE_CONTENT = this.getContent();
		let result = this.getPrimeField();

		ReportJsonParserFile.SUB_FIELD.forEach((FIELD) => {
			let linePos = FILE_CONTENT.indexOf(FIELD);
			let arrayPos = FILE_CONTENT.slice(linePos).indexOf(target) + linePos;
			let avergePos = FILE_CONTENT.slice(arrayPos).indexOf("Averge:") + arrayPos;
			let sPos = FILE_CONTENT.slice(avergePos).indexOf("s") + avergePos;

			let s = findValue(String(FILE_CONTENT.slice(avergePos)));
			let ms = findValue(String(FILE_CONTENT.slice(sPos)));

			result[FIELD.substring(0, FIELD.length-2)] = s * 1000 + ms;
		});

		return this.sort(result);
	}

	getPrimeField(){
		let fileContent = this.getContent();
		let result = {};
		ReportJsonParserFile.PRIME_FIELD.forEach((FIELD) => {
			let pos = fileContent.indexOf(FIELD);
			let value = findValue(String(fileContent.slice(pos)));

			result[FIELD] = value;
		});
		return result;
	}

	sort(obj){
		let sortedObj = {};
		ReportJsonParserFile.SORT_FIELD_ARRAY.forEach((key) => {
			sortedObj[key] = obj[key];
		});

		return sortedObj;
	}
} 

function findValue(str){
	for(let i = 0; i <= str.length-1; i ++){
		if(str[i].match(/\d/)){
			return parseFloat(str.slice(i));
		}
	}
}