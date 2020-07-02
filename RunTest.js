module.exports = class RunTest{
	static genTest(length, types){
		let testArray = [];
		for(let i = 1; i <= length; i ++){
			let testUnit = {};
			testUnit.type = RunTest.genRandomType(types);
			testUnit.value = RunTest.genRandomValue();
			testArray.push(testUnit);
		}
		return testArray;
	}

	static genRandomType(types){
		let rand = Math.floor(Math.random() * types.length);
		return types[rand];
	}

	static genRandomValue(upperBound){
		return Math.floor(Math.random() * (upperBound || 102400));
	}

	static testCorrectness(array1, array2){
		let length = array1.length;
		for(let i = 0; i <= length-1; i ++){
			let test = {
				type: "GET",
				value: i
			}
			let array1Result = array1.run(test);
			let array2Result = array2.run(test);

			if(array1Result !== array2Result){
				console.log("Failed");
				console.log(array1Result, " : ", array2Result);
				console.log(array1.getArray());
				console.log(array2.getArray());
				return;
			}
		}
		console.log("All Test Pass");
	}

	static printTestArrayInfo(testArray){
		let info = Object.assign({}, null);
		for(let i = 0; i <= testArray.length-1; i ++){
			if(info.hasOwnProperty(testArray[i].type)){
				info[testArray[i].type] ++;
			}else{
				info[testArray[i].type] = 1;
			}
		}

		let str = "| ";
		for(let key of Object.keys(info)){
			str += key;
			str += ": ";
			str += info[key];
			str += " | ";
		}

		console.log("Test Units Amount: ", testArray.length);
		console.log(str);
	}

	static getFormatArray(name, initial){
		return new FormatArray(name, initial);
	}

	static randomFill(length, ...arrays){
		console.log("----------------------------");
		console.log("Filling");
		let data = RunTest.genTest(length, ["PUSH", "UNSHIFT"]);

		arrays.forEach((array) => {
			for (var i = 0; i <= data.length-1; i++) {
				array.run(data[i]);
			}
		});
		console.log("----------------------------");
	}

	static runTest(testArray, ...arrays){
		console.log("----------------------------");
		console.log("Start Testing");
		console.log("");
		RunTest.printTestArrayInfo(testArray);
		console.log("");
/*
		arrays.forEach((array) => {
			let start = process.hrtime();
			for(let i = 0; i <= testArray.length-1; i ++){
				array.run(testArray[i]);
			}
			let end = process.hrtime(start);
			console.log(array.getName(), ": ", end[0], "s ", end[1] / 1000000, "ms");
		});
*/
		for(let i = 0; i <= arrays.length-1; i ++){
			let array = arrays[i];
			let start = process.hrtime();
			for(let i = 0; i <= testArray.length-1; i ++){
				array.run(testArray[i]);
			}
			let end = process.hrtime(start);
			console.log(array.getName(), ": ", end[0], "s ", end[1] / 1000000, "ms");
		}
		
		console.log("");
		console.log("----------------------------");
	}
}

function getCurrentMS(){
	return process.hrtime();
}

class FormatArray{
	constructor(name, initial){
		this.name = name;
		this.array = initial;
	}
	run(testUnit){
		switch(String(testUnit.type)){
			case "PUSH":
				this.array.push(testUnit.value);
				break;
			case "UNSHIFT":
				this.array.unshift(testUnit.value);
				break;
			case "POP":
				return this.array.pop();
			case "SHIFT":
				return this.array.shift();
			case "GET":
				return this.array[testUnit.value % this.array.length];
		}
	}
	getArray(){ return this.array; }
	getName(){ return this.name; }
};