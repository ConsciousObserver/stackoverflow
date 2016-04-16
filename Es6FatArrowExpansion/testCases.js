//validation of test cases
(function () {
	var tests = document.querySelectorAll('.test');
	var currentExpansionNode = null;
	var currentLogNode = null;
	for(var i = 0; i < tests.length; i++) {
		var currentNode = tests[i];
		addTitle("Test " + (i+1), currentNode);
		createExpansionAndLogNode(currentNode);
		
		var testCode = currentNode.innerText;
		var expandedCode = expandFatArrow(testCode);

		logDom(expandedCode, 'expanded');
		
		eval(expandedCode);
		
	};
	function createExpansionAndLogNode(node) {
		var expansionNode = document.createElement('pre');
		expansionNode.classList.add('expanded');
		currentExpansionNode = expansionNode;
		
		var logNode = document.createElement('div');
		logNode.classList.add('log');
		currentLogNode = logNode;
		
		appendAfter(node,expansionNode);
		addTitle("Expansion Result", expansionNode);
		appendAfter(expansionNode, logNode);
		addTitle("Output", logNode);
	}
	function appendAfter(afterNode, newNode) {
		afterNode.parentNode.insertBefore(newNode, afterNode.nextSibling);
	}

	//logs to expansion node or log node
	function logDom(str, cssClass) {
		console.log(str);
		var node = null;
		if(cssClass === 'expanded') {
			node = currentExpansionNode;
		} else {
			node = currentLogNode;
		}
		
		var newNode = document.createElement("pre");
		
		newNode.innerText = str;
		node.appendChild(newNode);
	}
	function addTitle(title, onNode) {
		var titleNode = document.createElement('h3');
		titleNode.innerText = title;
		onNode.parentNode.insertBefore(titleNode, onNode);
	}
})();