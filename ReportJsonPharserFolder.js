const FS = require('fs');
let PHARSER_FILE = require("./ReportJsonPharserFile");

module.exports = class ReportJsonPharserFolder{
	constructor(pathsIn, sortByIn){
		this.paths = pathsIn;
		this.sortBy = sortByIn || 'Test Unit Amount';
	}
	parseReport(){
		this.paths.forEach((path) => {
			FS.readdir(path, (err, files) => {
				if(err){
					console.log(err);
					return;
				}

				let results = [];
				files.forEach((file) => {
					let filePharser = new PHARSER_FILE(path + "/" + file);
					results.push(filePharser.parseReport());
				});

				results.sort((resultA, resultB) => {
					return resultA[this.sortBy] - resultB[this.sortBy]
				});

				FS.writeFileSync("jsons/" + path.replace("reports/", "") + ".json", JSON.stringify(results));
			});
		});
	}
}