"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
var scanner_1 = __importDefault(require("../scanner/scanner"));
var boolean_1 = __importDefault(require("../ast/boolean"));
var number_1 = __importDefault(require("../ast/number"));
var text_1 = __importDefault(require("../ast/text"));
var variable_1 = __importDefault(require("../ast/variable"));
var binop_1 = __importDefault(require("../ast/binop"));
var display_1 = __importDefault(require("../ast/display"));
var assignment_1 = __importDefault(require("../ast/assignment"));
var while_1 = __importDefault(require("../ast/while"));
var if_1 = __importDefault(require("../ast/if"));
var program_1 = __importDefault(require("../ast/program"));
var environment_1 = __importDefault(require("../environment/environment"));
var Parser = /** @class */ (function () {
    function Parser(inStream) {
        if (inStream instanceof scanner_1.default) {
            this.scanner = inStream;
        }
        else {
            this.scanner = new scanner_1.default(inStream);
        }
        this.currentToken = this.scanner.nextToken();
    }
    Parser.prototype.eat = function (expected) {
        if (this.currentToken == expected) {
            this.currentToken = this.scanner.nextToken();
        }
        else {
            throw new Error("[ERROR] Unexpected token " + this.currentToken +
                ", expected " + expected);
        }
    };
    Parser.isIdentifier = function (str) {
        return scanner_1.default.isLetter(str.charAt(0));
    };
    Parser.prototype.parseValue = function () {
        // (Expression)
        if (this.currentToken.charAt(0) == "(") {
            this.eat("(");
            var temp = this.parseExpression();
            this.eat(")");
            return temp;
        }
        // boolean
        if (this.currentToken == "true") {
            this.eat("true");
            return new boolean_1.default(true);
        }
        if (this.currentToken == "false") {
            this.eat("false");
            return new boolean_1.default(false);
        }
        // id
        if (Parser.isIdentifier(this.currentToken)) {
            var res_1 = this.currentToken;
            this.eat(res_1);
            return new variable_1.default(res_1);
        }
        // number
        var res = parseInt(this.currentToken);
        this.eat(this.currentToken);
        return new number_1.default(res);
    };
    Parser.prototype.parseNegExpr = function () {
        if (this.currentToken.charAt(0) == "-") {
            this.eat("-");
            return new binop_1.default(new number_1.default(0), "-", this.parseValue());
        }
        return this.parseValue();
    };
    Parser.prototype.parseMultExpr = function () {
        var res = this.parseNegExpr();
        while (this.currentToken == "*" || this.currentToken == "/") {
            var operator = this.currentToken;
            this.eat(operator);
            var nextTerm = this.parseNegExpr();
            res = new binop_1.default(res, operator, nextTerm);
        }
        return res;
    };
    Parser.prototype.parseAddExpr = function () {
        var res = this.parseMultExpr();
        while (this.currentToken == "+" || this.currentToken == "-") {
            var operator = this.currentToken;
            this.eat(operator);
            var nextTerm = this.parseMultExpr();
            res = new binop_1.default(res, operator, nextTerm);
        }
        return res;
    };
    Parser.prototype.parseExpression = function () {
        var res = this.parseAddExpr();
        while (this.currentToken == "<"
            || this.currentToken == ">"
            || this.currentToken == ">="
            || this.currentToken == "<="
            || this.currentToken == "<>"
            || this.currentToken == "=") {
            var operator = this.currentToken;
            this.eat(operator);
            var nextTerm = this.parseAddExpr();
            res = new binop_1.default(res, operator, nextTerm);
        }
        return res;
    };
    Parser.prototype.parseDisplay = function () {
        this.eat("display");
        var expr;
        if (this.currentToken.charAt(0) == "'") {
            expr = new text_1.default(this.currentToken.substr(1, this.currentToken.length - 2));
            this.eat(this.currentToken);
        }
        else {
            expr = this.parseExpression();
        }
        if (this.currentToken == "read") {
            this.eat("read");
            var saveTo = this.currentToken;
            this.eat(saveTo);
            return new display_1.default(expr, saveTo);
        }
        return new display_1.default(expr);
    };
    Parser.prototype.parseAssign = function () {
        this.eat("assign");
        var toVar = this.currentToken;
        this.eat(toVar);
        this.eat("=");
        return new assignment_1.default(toVar, this.parseExpression());
    };
    Parser.prototype.parseWhile = function () {
        this.eat("while");
        var condition = this.parseExpression();
        this.eat("do");
        var prog = this.parseProgram();
        this.eat("end");
        return new while_1.default(condition, prog);
    };
    Parser.prototype.parseIf = function () {
        this.eat("if");
        var cond = this.parseExpression();
        this.eat("then");
        var then = this.parseProgram();
        if (this.currentToken == "else") {
            this.eat("else");
            var els = this.parseProgram();
            this.eat("end");
            return new if_1.default(cond, then, els);
        }
        this.eat("end");
        return new if_1.default(cond, then);
    };
    Parser.prototype.parseStatement = function () {
        if (this.currentToken == "display")
            return this.parseDisplay();
        if (this.currentToken == "assign")
            return this.parseAssign();
        if (this.currentToken == "while")
            return this.parseWhile();
        if (this.currentToken == "if")
            return this.parseIf();
        throw new Error("invalid statement: " + this.currentToken);
    };
    Parser.prototype.parseProgram = function () {
        var res = [];
        res.push(this.parseStatement());
        while (this.scanner.hasNext() && this.currentToken != "end" && this.currentToken != "else") {
            res.push(this.parseStatement());
        }
        return new program_1.default(res);
    };
    Parser.test = function () {
        var filename = __dirname + "/ParserTest.txt";
        try {
            var p = new Parser(filename);
            p.parseProgram().exec(new environment_1.default());
        }
        catch (e) {
            console.error(e);
            process.exit(1);
        }
    };
    return Parser;
}());
exports.default = Parser;
// Main method
if (require.main === module) {
    Parser.test();
}
module.exports = Parser;
