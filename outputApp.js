const PHARSER_FOLDER = require("./ReportJsonPharserFolder");

const REPORT_FOLDERS = [
	"reports/UnitResult",
	"reports/LowUnitResult",
	"reports/BoundResult_100000",
	"reports/BoundResult_300000"
];

let pharserFolder = new PHARSER_FOLDER(REPORT_FOLDERS);

pharserFolder.parseReport();