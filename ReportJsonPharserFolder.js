const FS = require('fs');
let PHARSER_FILE = require("./ReportJsonPharserFile");

module.exports = class ReportJsonPharserFolder{
	constructor(pathsIn){
		this.paths = pathsIn;
	}
	parseReport(){
		this.paths.forEach((path) => {
			FS.readdir(path, (err, files) => {
				let results = [];
				files.forEach((file) => {
					let filePharser = new PHARSER_FILE(path + "/" + file);
					results.push(filePharser.parseReport());
				});
				FS.writeFileSync("jsons/" + path.replace("reports/", "") + ".json", JSON.stringify(results));
			});
		});
	}
}