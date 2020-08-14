const FS = require('fs');
let PHARSER_FILE = require("./ReportJsonPharserFile");

module.exports = class ReportJsonPharserFolder{
	constructor(nameIn, pathsIn, sortByIn){
		this.name = nameIn;
		this.paths = pathsIn;
		this.sortBy = sortByIn || 'Test Unit Amount';

		let index = this.name.lastIndexOf("/");
		if(index !== -1 && !FS.existsSync("jsons/" + this.name.substring(0, index))){
			FS.mkdirSync("jsons/" + this.name.substring(0, index), { recursive: true });
		}
	}

	pharseReport(targets){
		this.paths.forEach((path) => {
			FS.readdir(path, (err, files) => {
				if(err){
					console.log(err);
					return;
				}

				let results = [];
				files.forEach((file) => {
					let filePharser = new PHARSER_FILE(path + "/" + file);
					results.push(filePharser.pharseReport(targets));
				});

				results.sort((resultA, resultB) => {
					return resultA[this.sortBy] - resultB[this.sortBy]
				});

				FS.writeFileSync("jsons/" + this.name + ".json", JSON.stringify(results));
			});
		});
	}

	pharseTimeReport(targets){
		this.paths.forEach((path) => {
			FS.readdir(path, (err, files) => {
				if(err){
					console.log(err);
					return;
				}

				let results = {};
				files.forEach((file) => {
					let filePharser = new PHARSER_FILE(path + "/" + file);
					let pharseResults = [];

					targets.forEach((target) => {
						pharseResults.push(filePharser.pharseTime(target));
					});

					PHARSER_FILE.SUB_FIELD.forEach((KEY) => {
						let field = KEY.substring(0, KEY.length-2);

						if(!results[field]) results[field] = [];
						results[field].push({"Test Unit Amount": pharseResults[0]["Test Unit Amount"]});

						pharseResults.forEach((result, index) => {
							results[field][results[field].length-1][targets[index]] = parseFloat(result[field].toFixed(4));
						});
					});
				});

				PHARSER_FILE.SUB_FIELD.forEach((KEY) => {
					let field = KEY.substring(0, KEY.length-2);

					results[field].sort((a, b) => {
						return a["Test Unit Amount"] - b["Test Unit Amount"];
					});
					FS.writeFileSync("jsons/" + this.name + "-" + field.trim() + ".json", JSON.stringify(results[field]));
				});
			});
		});
	}
}