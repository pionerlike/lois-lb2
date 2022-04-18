/////////////////////////////////////////////////////////////////////////////////////////
// Лабораторная работа №2 по дисциплине ЛОИС
//  Ахроров Мирафзал 921731
// Вариант 6. Проверить являются ли формулы равносильными
const NEGATION = "!";
const CONJUNCTION = "/\";
const DISJUNCTION = "\/";
const IMPLICATION = "->";
const EQUIVALENCE = "~";

const OPENING_BRACKET = "(";
const CLOSING_BRACKET = ")";
const GLOBAL = 'g';

var RESULT;
var INPUTS_ELEMENT = [];

function start(check) {
    let formula1 = document.getElementById('panel1').value;
	let formula2 = document.getElementById('panel2').value;
    let firstTableLabel = document.getElementById("firstTableLabel");
	let secondTableLabel = document.getElementById("secondTableLabel");
	let firstTruthTable = document.getElementById('firstTruthTable');
	let secondTruthTable = document.getElementById('secondTruthTable');
    let sdnfLabel = document.getElementById("sdnf-label");
    let tableLabel = document.getElementById("table-label");
	let res = document.getElementById("result");
    if (checkSyntax(formula1)) {
		if(checkSyntax(formula2)){
			firstTableLabel.innerHTML ="Таблица истинности для первой формулы";
			secondTableLabel.innerHTML ="Таблица истинности для второй формулы";
			INPUTS_ELEMENT = getElementSet(formula1, formula2);
			INPUTS_ELEMENT.sort();
			let object1 = createTruthTable(formula1);
			let object2 = createTruthTable(formula2);
			checkSimpleFormula(formula1, object1);
			checkSimpleFormula(formula2, object2);
			firstTruthTable.innerHTML = drawTable(object1.truthTable);
			secondTruthTable.innerHTML = drawTable(object2.truthTable);
			if((check && compareFormulas(object1, object2)) || (!check && !compareFormulas(object1, object2))){
				res.innerHTML = "Результат: Верно!";
				return;
			} else {
				res.innerHTML = "Результат: Неверно!";
				return;
			}
		} else {
			firstTableLabel.innerHTML = "";
			secondTableLabel.innerHTML = "";
			result.innerHTML = "Вторая формула введена неверно";
			firstTruthTable.innerHTML = "";
			secondTruthTable.innerHTML = "";
		}
    } else {
		firstTableLabel.innerHTML = "";
		secondTableLabel.innerHTML = "";
        result.innerHTML = "Первая формула введена неверно";
        firstTruthTable.innerHTML = "";
		secondTruthTable.innerHTML = "";
    }
}


function checkSimpleFormula(formula, object){
	if (formula === "0" || formula === "1") {
		for (let i = 0; i < Object.keys(object.truthTable).length; i++) {
			let result = object.truthTable[i];
			result[(formula + " ")] = parseInt(formula);
			object.truthTable[i] = result;
		}
		object.result = formula + " ";
	}
	else return;
}



function compareFormulas(object1, object2){
	for (let i = 0; i < Object.keys(object1.truthTable).length; i++) {
		let result1 = object1.truthTable[i];
		let result2 = object2.truthTable[i];
		if (parseInt(result1[object1.result]) !== parseInt(result2[object2.result])) {
			return false;
		}
    }
	return true;
}


function createTruthTable(formula){
    let elementsSize = INPUTS_ELEMENT.length;
    let tableSize = Math.pow(2, elementsSize);
	let result;
    let truthTable = {};
    for (let i = 0; i < tableSize; i++) {
        let currentRow = convertToBinary(i, elementsSize);
        let tempRow = getStartValues(INPUTS_ELEMENT, currentRow);
        let results = getAnswer(formula, tempRow);
        for (let key of Object.keys(results)) {
            tempRow[key] = results[key];
        }
        truthTable[i] = tempRow;
    }
    return {
        truthTable: truthTable,
		result: RESULT
    }
}


function getElementSet(formula1, formula2) {
    let symbol = "([A-Z])";
    symbol = new RegExp(symbol, GLOBAL);
    let results1 = formula1.match(symbol) || [];
	let results2 = formula2.match(symbol) || [];
	let results = results1.concat(results2);
    return results.filter(function (symbol, index) {
        return results.indexOf(symbol) === index;
    });
}


function convertToBinary(number, stringLength) {
    let string = (number >>> 0).toString(2);
    for (let i = string.length; i < stringLength; i++) {
        string = "0" + string;
    }
    return string;
}

function getStartValues(elements, currentNumber) {
    let object = {};
    for (let i = 0; i < elements.length; i++) {
        let element = elements[i];
        object[element] = currentNumber[i];
    }
    return object;
}

function getAnswer(formula, tempObject) {
    let constFormula = formula;
    for (let key of Object.keys(tempObject)) {
        let val = tempObject[key];
        constFormula = constFormula.replace(new RegExp(key, GLOBAL), val);
    }
    return calculateRowTable(constFormula, formula);
}

function calculateRowTable(formula, symbolFormula) {
    let regFormula = "([(][!][0-1][)])|([(][0-1]((&)|(->)|(~)|(\\|))[0-1][)])";
    let regSymbolFormula = "([(][!]([A-Zzf]|[\\d])[)])|([(]([A-Zzf]|[\\d])((&)|(->)|(~)|(\\|))([A-Zzf]|[\\d])[)])";
    let regCounter = "[!]|[~]|[&]|[|]|[>]";
	let counter;
    regCounter = new RegExp(regCounter, GLOBAL);
	if(symbolFormula.match(regCounter)) counter = symbolFormula.match(regCounter).length;
	else counter = 0;
    let results = [];
    let operations = [];
    let i = 0;
    regFormula = new RegExp(regFormula);
    regSymbolFormula = new RegExp(regSymbolFormula);
	while(symbolFormula.includes("0") || symbolFormula.includes("1")){
		symbolFormula = symbolFormula.replace("0", "z");
		symbolFormula = symbolFormula.replace("1", "f");
	}
	RESULT = symbolFormula;
    while (regFormula.exec(formula) != null && regSymbolFormula.exec(symbolFormula) != null) {
        let subFormula = regFormula.exec(formula)[0];
        let symbolIndex = regSymbolFormula.exec(symbolFormula)[0];
        let result = chooseOperation(subFormula);
        let resultIndex = deleteDigits(symbolIndex, operations);
		while(resultIndex.includes("z") || resultIndex.includes("f")){
			resultIndex = resultIndex.replace("z", "0");
			resultIndex = resultIndex.replace("f", "1");
		}
        results[resultIndex] = result;
        formula = formula.replace(subFormula, result);
        counter--;
        if (counter === 0) {
            RESULT = resultIndex;
            results[resultIndex] = result;
        }
		
        operations[i] = resultIndex;
        symbolFormula = symbolFormula.replace(symbolIndex, i);
        i++;
    }
    return results;
}

function deleteDigits(formula, operations) {
    let regNegation = "([(][!][\\d]+[)])";
    let regLeftDelete = "(([(][\\d]+)((&)|(->)|(~)|(\\|))[A-Zzf][)])";
    let regRightDelete = "(([(][A-Zzf])((&)|(->)|(~)|(\\|))[\\d]+[)])";
    let regTwoDelete = "(([(][\\d]+)((&)|(->)|(~)|(\\|))[\\d]+[)])";
    let expLeft = formula.match(new RegExp(regNegation + "|" + regLeftDelete, GLOBAL));
    let expRight = formula.match(new RegExp(regNegation + "|" + regRightDelete, GLOBAL));
    let exp = formula.match(new RegExp(regNegation + "|" + regTwoDelete, GLOBAL));
    if (exp !== null || expLeft !== null || expRight !== null) {
        let first;
        let operation = "";
        let second;
        let wrong;
        if (expRight !== null) {
            wrong = expRight[0];
        } else if (expLeft !== null) {
            wrong = expLeft[0];
        } else if (exp !== null) {
            wrong = exp[0];
        }
        if (wrong.indexOf(NEGATION) === -1) {
            if (wrong.indexOf(IMPLICATION) > -1) {
                operation = IMPLICATION;
                if (expRight !== null) {
                    first = wrong[1];
                    second = parseInt(wrong[4]);
                    return OPENING_BRACKET + first + operation + operations[second] + CLOSING_BRACKET;
                } else if (expLeft !== null) {
                    first = parseInt(wrong[1]);
                    second = wrong[4];
                    return OPENING_BRACKET + operations[first] + operation + second + CLOSING_BRACKET;
                } else if (exp !== null) {
                    first = parseInt(wrong[1]);
                    second = parseInt(wrong[4]);
                    return OPENING_BRACKET + operations[first] + operation + operations[second] + CLOSING_BRACKET;
                }
            } else {
                operation = wrong[2];
                if (expRight !== null) {
                    first = wrong[1];
                    second = parseInt(wrong[3]);
                    return OPENING_BRACKET + first + operation + operations[second] + CLOSING_BRACKET;
                } else if (expLeft !== null) {
                    first = parseInt(wrong[1]);
                    second = wrong[3];
                    return OPENING_BRACKET + operations[first] + operation + second + CLOSING_BRACKET;
                } else if (exp !== null) {
                    first = parseInt(wrong[1]);
                    second = parseInt(wrong[3]);
                    return OPENING_BRACKET + operations[first] + operation + operations[second] + CLOSING_BRACKET;
                }
            }
        } else {
            first = parseInt(wrong[2]);
            operation = NEGATION;
            return OPENING_BRACKET + operation + operations[first] + CLOSING_BRACKET;
        }
    } else {
        return formula;
    }
}


function chooseOperation(inputFormula) {
    if (inputFormula.indexOf(NEGATION) > -1) {
        return calculateNegation(inputFormula);
    } else if (inputFormula.indexOf(CONJUNCTION) > -1) {
        return calculateConjuction(inputFormula);
    } else if (inputFormula.indexOf(EQUIVALENCE) > -1) {
        return calculateEquivalence(inputFormula);
    } else if (inputFormula.indexOf(IMPLICATION) > -1) {
        return calculateImplication(inputFormula);
    } else if (inputFormula.indexOf(DISJUNCTION) > -1) {
        return calculateDisjunction(inputFormula);
    } else {
        return -1;
    }
}

function calculateNegation(inputFormula) {
    let value = parseInt(inputFormula[2]);
    return (!value) ? 1 : 0;
}


function calculateConjuction(inputFormula) {
    let firstValue = parseInt(inputFormula[1]);
    let secondValue = parseInt(inputFormula[3]);
    return (firstValue && secondValue) ? 1 : 0;
}


function calculateDisjunction(inputFormula) {
    let firstValue = parseInt(inputFormula[1]);
    let secondValue = parseInt(inputFormula[3]);
    return (firstValue || secondValue) ? 1 : 0;
}


function calculateImplication(inputFormula) {
    let firstValue = parseInt(inputFormula[1]);
    let secondValue = parseInt(inputFormula[4]);
    return ((!firstValue) || secondValue) ? 1 : 0;
}


function calculateEquivalence(inputFormula) {
    let firstValue = parseInt(inputFormula[1]);
    let secondValue = parseInt(inputFormula[3]);
    return (firstValue === secondValue) ? 1 : 0;
}



function drawTable(truthTable) {
    let tableSize = Math.pow(2, INPUTS_ELEMENT.length);
    let innerHTML = "";
    let tr = "<tr>";
    for (let key of Object.keys(truthTable[0])) {
        tr += "<th>" + key + "</th>"
    }
    tr += "</tr>";
    innerHTML += tr;
    for (let i = 0; i < tableSize; i++) {
        let object = truthTable[i];
        tr = "<tr>";
        for (let key of Object.keys(object)) {
            let val = object[key];
            tr += "<td>" + val + "</td>"
        }
        tr += "</tr>";
        innerHTML += tr;
    }
    return innerHTML;
}


function checkSyntax(formula) {
    let regFormula = "([(][!]<ATOM_OR_CONST>[)])|([(]<ATOM_OR_CONST>((&)|(\\|)|(->)|(~))<ATOM_OR_CONST>[)])";
    let patter = "<ATOM_OR_CONST>";
    let atom = "([A-Z]|[0-1])";
    let replaceFormula = "A";

    regFormula = regFormula.replace(new RegExp(patter, GLOBAL), atom);
    regFormula = new RegExp(regFormula);
    let old;
    old = formula;
    formula = formula.replace(regFormula, replaceFormula);

    while (formula !== old) {
        old = formula;
        formula = formula.replace(regFormula, replaceFormula);
    }

    let arrOutput = formula.match(new RegExp(atom, GLOBAL));
    return (formula.length === 1) && (arrOutput != null) && (arrOutput.length === 1);
}
