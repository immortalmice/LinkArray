const FS = require('fs');
let PARSER_FILE = require("./ReportJsonParserFile");

module.exports = class ReportJsonParserFolder{
	constructor(nameIn, pathsIn, sortByIn){
		this.name = nameIn;
		this.paths = pathsIn;
		this.sortBy = sortByIn || 'Test Unit Amount';

		let index = this.name.lastIndexOf("/");
		if(index !== -1 && !FS.existsSync("jsons/" + this.name.substring(0, index))){
			FS.mkdirSync("jsons/" + this.name.substring(0, index), { recursive: true });
		}
	}

	parseReport(targets){
		this.paths.forEach((path) => {
			FS.readdir(path, (err, files) => {
				if(err){
					console.log(err);
					return;
				}

				let results = [];
				files.forEach((file) => {
					let fileParser = new PARSER_FILE(path + "/" + file);
					results.push(fileParser.parseReport(targets));
				});

				results.sort((resultA, resultB) => {
					return resultA[this.sortBy] - resultB[this.sortBy]
				});

				FS.writeFileSync("jsons/" + this.name + ".json", JSON.stringify(results));
			});
		});
	}

	parseTimeReport(targets){
		this.paths.forEach((path) => {
			FS.readdir(path, (err, files) => {
				if(err){
					console.log(err);
					return;
				}

				let results = {};
				files.forEach((file) => {
					let fileParser = new PARSER_FILE(path + "/" + file);
					let parseResults = [];

					targets.forEach((target) => {
						parseResults.push(fileParser.parseTime(target));
					});

					PARSER_FILE.SUB_FIELD.forEach((KEY) => {
						let field = KEY.substring(0, KEY.length-2);

						if(!results[field]) results[field] = [];
						results[field].push({"Test Unit Amount": parseResults[0]["Test Unit Amount"]});

						parseResults.forEach((result, index) => {
							results[field][results[field].length-1][targets[index]] = parseFloat(result[field].toFixed(4));
						});
					});
				});

				PARSER_FILE.SUB_FIELD.forEach((KEY) => {
					let field = KEY.substring(0, KEY.length-2);

					if(results[field]){
						results[field].sort((a, b) => {
							return a["Test Unit Amount"] - b["Test Unit Amount"];
						});
					}
					
					FS.writeFileSync("jsons/" + this.name + "-" + field.trim() + ".json", JSON.stringify(results[field] ? results[field] : []));
				});
			});
		});
	}
}