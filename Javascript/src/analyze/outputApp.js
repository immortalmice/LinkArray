const PHARSER_FOLDER = require("./ReportJsonPharserFolder");

let pharserFolder;

pharserFolder = new PHARSER_FOLDER(["reports/AdaptiveArray/NormalArray"]);
pharserFolder.pharseReport(["Adaptive Array Win Cases", "Normal Array Win Cases"]);

pharserFolder = new PHARSER_FOLDER(["reports/AdaptiveArray/DoublyLinkedList"]);
pharserFolder.pharseReport(["Adaptive Array Win Cases", "Doubly Linked List Win Cases"]);

pharserFolder = new PHARSER_FOLDER(["reports/AdaptiveArray/Triple"]);
pharserFolder.pharseTimeReport(["Normal Array", "Doubly Linked List", "Adaptive Array"]);