"use strict";

function generateJson(generationCallback) {
	console.log("started");
	var excelUrl = "test.xlsx";
	//following are the datatypes which will be stored as arrays, expected to be lower case
	var inputTypes = ["dropdown", "radio button"];
	var dateType = "date picker"; //expected to be lower case

	getExcel(excelUrl, readExcel);

	function getExcel(url, callback) {
		var request = new XMLHttpRequest();
		request.open("GET", url, true);
		request.responseType = "arraybuffer";

		request.onload = function (event) {
			callback(request.response);
		};
		request.send();
	}

	function readExcel(buffer) {
		var data = new Uint8Array(buffer);
		var arr = [];
		for(var i = 0; i != data.length; ++i) {
			arr[i] = String.fromCharCode(data[i]);
		}
		var bstr = arr.join("");

		/* create workbook */
		var workbook = XLSX.read(bstr, {type:"binary"});
		//console.log(workbook);
		
		//selecting first sheet, change as required
		var sheetName = workbook.SheetNames[0];
		var sheet = workbook.Sheets[sheetName];

		var resultSchema = sheetToJson(sheet);
		
		generationCallback(resultSchema);
	}
	
	function sheetToJson(sheet) {
		console.log("generating json:");
		console.log(sheet);
		var headerName = {};
		var htmlInputType = [];//drop down, radio
		var jsonArray = [];
		var lastColumnTag = sheet["!ref"][3];//format is A1:H7

		for(var cellAddress in sheet) {
			if(cellAddress[0] !== "!") {
				var value = sheet[cellAddress].v;
				//console.log(cellAddress + " : " + value);
				//storing header
				var columnTag = cellAddress[0];
				var rowNum = cellAddress[1];

				switch(rowNum) {
					case "1":
						headerName[columnTag] = value;
						break;
					case "2":
						htmlInputType[headerName[columnTag]] = value ? value.toLowerCase() : value;
						break;
					default :
						var currentObj = currentObj || {};
						var type = htmlInputType[headerName[columnTag]];
						
						currentObj[headerName[columnTag]] = value;
						
						if(columnTag === lastColumnTag) {
							jsonArray.push(currentObj);
							currentObj = null;
						}
				}
			}
		}
		
		var inputValues = getInputValues(jsonArray, htmlInputType);
		
		var jsonSchema = {
			array : jsonArray,
			inputs : inputValues
		};

		//console.log(JSON.stringify(jsonSchema));
		return jsonSchema;
	}

	function getInputValues(schema, htmlType) {
		var inputValues = {};
		for(var index in schema) {
			var obj = schema[index];
			for(var prop in obj) {
				var type = htmlType[prop];
				if(inputTypes.indexOf(type) !== -1) {//expected lower case
					inputValues[prop] = inputValues[prop] || {type: type, values: []};
					
					var value = obj[prop];

					if(inputValues[prop].values.indexOf(value) === -1) {
						inputValues[prop].values.push(value);
					}
				}
			}
		}
		return inputValues;
	}

	function strToDate(str) {
		var year = Number(str.substring(0, 4));
		var month = Number(str.substring(4,6));
		var date = Number(str.substring(6, str.length));
		console.log("%s, %s, %s", year, month, date);
		return new Date(year, month-1, date);
	}
};