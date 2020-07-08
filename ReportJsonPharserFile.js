const FS = require('fs');

module.exports = class ReportJsonPharserFile{
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

	constructor(pathIn){
		this.path = pathIn;
	}

	parseReport(){
		let FILE_CONTENT = FS.readFileSync(this.path);
		let result = {};

		ReportJsonPharserFile.PRIME_FIELD.forEach((FIELD) => {
			let pos = FILE_CONTENT.indexOf(FIELD);
			let value = findValue(String(FILE_CONTENT.slice(pos)));

			result[FIELD] = value;
		});

		["Auto Link Array Win Cases", "Normal Array Win Cases"].forEach((NAME_SPACE, index) => {
			let namePos = FILE_CONTENT.indexOf(NAME_SPACE);
			let rest = String(FILE_CONTENT.slice(namePos));

			ReportJsonPharserFile.SUB_FIELD.forEach((FIELD) => {
				let pos = rest.indexOf(FIELD);
				if(pos !== -1){
					let value = findValue(rest.slice(pos));
					if(!result[FIELD.substring(0, FIELD.length-2)])
						result[FIELD.substring(0, FIELD.length-2)] = index === 1 ? -1 * value : value;
				}
			});
		});

		return result;
	}
}

function findValue(str){
	for(let i = 0; i <= str.length-1; i ++){
		if(str[i].match(/\d/)){
			return parseFloat(str.slice(i));
		}
	}
}