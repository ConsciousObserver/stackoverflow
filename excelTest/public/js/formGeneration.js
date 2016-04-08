"use strict";

$(function () {
	generateJson(function (schema) {
		//printing JSON
		$("#jsonContainer").text(JSON.stringify(schema, null, "  "));

		var data = schema.array;
		var inputs = schema.inputs;
		
		var container = $("#formContainer");
		
		data.forEach(function (obj) {
			var form = createForm(obj);
			container.append(form);
		});

		function createForm(obj) {
			var form = $("<form/>");
			for(var prop in obj) {
				var value = obj[prop];
				form.append(getInput(prop, value, inputs[prop]));
			}
			return form;
		}

		function getInput(prop, value, inputValues) {
			var inputStr = "";
			var labelStr = "<label for='" + prop + "'>" + prop + "</label>";
			if(!inputValues) {
				inputStr = "<input name='" + prop + "' value='" + value + "'>";
			} else if(inputValues.type === "dropdown") {
				inputStr = "<select>";
				inputValues.values.forEach(function(optionValue) {
					var selected = optionValue === value ? "selected" : "";
					
					inputStr += "<option value='" + optionValue + "' " + selected + ">" + optionValue + "</option>";
				});
				inputStr += "</select>";
			} else if(inputValues.type === "radio button") {
				inputValues.values.forEach(function(radioValue) {
					var checked = radioValue === value ? "checked" : "";
					inputStr += "<input type='radio' name='" + prop + "' value='" + value + "' " + checked + ">" + radioValue + "</input>";
				});
			}
			
			return labelStr + inputStr;
		}
	});
});