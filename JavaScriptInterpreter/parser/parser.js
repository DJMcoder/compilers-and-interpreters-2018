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
/**
 * Parser parses an input from a Scanner and returns an Abstract Syntax Tree through
 * its method parseProgram.
 */
var Parser = /** @class */ (function () {
    /**
     * Creates a new Parser using a Scanner, and gets the first Token
     * @param {Scanner | string} inStream the Scanner or string which is fed into the parser
     */
    function Parser(inStream) {
        if (inStream instanceof scanner_1.default) {
            this.scanner = inStream;
        }
        else {
            this.scanner = new scanner_1.default(inStream);
        }
        this.currentToken = this.scanner.nextToken();
    }
    /**
     * Compares a string to the current token and throws an error if they are different
     * @param {string} expected the string to compare to the current token
     */
    Parser.prototype.eat = function (expected) {
        if (this.currentToken == expected) {
            this.currentToken = this.scanner.nextToken();
        }
        else {
            throw new Error("[ERROR] Unexpected token " + this.currentToken +
                ", expected " + expected);
        }
    };
    /**
     * Determines whether a string is an identifier
     * An identifier is any string which starts with a letter and is followed by numbers or letters
     * @param {string} str the string to evaluate as an identifier
     * @returns true if str is an identifier;
     *          false otherwise
     */
    Parser.isIdentifier = function (str) {
        return scanner_1.default.isLetter(str.charAt(0));
    };
    /**
     * Reads in a *Value* from the input scanner
     *
     * *Value*s can be of the form:
     * - `(`*Expression*`)`
     * - `true`
     * - `false`
     * - *id*
     * - *num*
     *
     * @returns the Expression correlating to the *Value* found
     */
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
    /**
     * Reads in a *NegExpr* from the input scanner
     *
     * *NegExpr*s can be of the form:
     * - *Value*
     * - `-`*Value*
     *
     * @returns the Expression correlating to the *NegExpr* found
     */
    Parser.prototype.parseNegExpr = function () {
        if (this.currentToken.charAt(0) == "-") {
            this.eat("-");
            return new binop_1.default(new number_1.default(0), "-", this.parseValue());
        }
        return this.parseValue();
    };
    /**
     * Reads in a *MultExpr* from the input scanner
     *
     * *MultExpr*s can be of the form:
     * - *NegExpr*
     * - *NegExpr* `*` *NegExpr*
     * - *NegExpr* `/` *NegExpr*
     *
     * @returns the Expression correlating to the *MultExpr* found
     */
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
    /**
     * Reads in a *AddExpr* from the input scanner
     *
     * *AddExpr*s can be of the form:
     * - *MultExpr*
     * - *MultExpr* `+` *MultExpr*
     * - *MultExpr* `-` *MultExpr*
     *
     * @returns the Expression correlating to the *AddExpr* found
     */
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
    /**
     * Reads in a *Expression* from the input scanner
     *
     * *AddExpr*s can be of the form:
     * - *AddExpr*
     * - *AddExpr* `<` *AddExpr*
     * - *AddExpr* `>` *AddExpr*
     * - *AddExpr* `>=` *AddExpr*
     * - *AddExpr* `<=` *AddExpr*
     * - *AddExpr* `<>` *AddExpr*
     * - *AddExpr* `=` *AddExpr*
     *
     * @returns the Expression correlating to the *Expression* found
     */
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
    /**
     * Reads in a *Display* Statement from the Scanner
     * Display Statements come both of these forms:
     *
     * - `display` *Expression*
     * - `display` *Expression* `read` *id*
     *
     * Both print the evaluated *Expression* to the console; but
     * the second one reads a value from the user and sets it to
     * a variable with the given identifier
     *
     * @returns a Display Statement correlating to the input from the Scanner
     */
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
    /**
     * Reads in a *Assignment* Statement from the Scanner
     * Assignment Statements come both of these forms:
     *
     * - `assign` *id* `=` *Expression*
     *
     * This assigns the evaluated *Expression* to the variable with the given *id*
     *
     * @returns a Assignment Statement correlating to the input from the Scanner
     */
    Parser.prototype.parseAssign = function () {
        this.eat("assign");
        var toVar = this.currentToken;
        this.eat(toVar);
        this.eat("=");
        return new assignment_1.default(toVar, this.parseExpression());
    };
    /**
     * Reads in a *While* Statement from the Scanner
     * While Statements come both of these forms:
     *
     * - `while` *Expression* `do` *Program* `end`
     *
     * This evaluates the *Program* every time the *Expression* evaluates to true
     *
     * @returns a While Statement correlating to the input from the Scanner
     */
    Parser.prototype.parseWhile = function () {
        this.eat("while");
        var condition = this.parseExpression();
        this.eat("do");
        var prog = this.parseProgram();
        this.eat("end");
        return new while_1.default(condition, prog);
    };
    /**
     * Reads in a *If* Statement from the Scanner
     * If Statements come both of these forms:
     *
     * - `if` *Expression* `then` *Program* `end`
     * - `if` *Expression* `then` *Program* `else` *Program* `end`
     *
     * If the expression evaluates to `true`, then the first *Program* is evaluated;
     * If the expression evaluates to `false` and there is a second *Program*, then it runs
     * the second *Program*
     *
     * @returns an If Statement correlating to the input from the Scanner
     */
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
    /**
     * Parses a statement depending on the current Token
     *
     * If the current token is `display`, it parses a Display Statement
     * If the current token is `assign`, it parses a Assignment Statement
     * If the current token is `while`, it parses a While Statement
     * If the current token is `if`, it parses an If Statement
     *
     * @returns the Statement correlating to the given input from the Scanner
     * @throws Error if the current token is not `display`, `assign`, `while`, or `if`
     */
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
    /**
     * Parses a Program, which is an array of Statements, until it reaches an endpoint
     * An enpoint occurs when it the code ends, or it reaches the token `end` or `else`
     *
     * @returns the Program containing all of the Statements
     */
    Parser.prototype.parseProgram = function () {
        var res = [];
        res.push(this.parseStatement());
        while (this.scanner.hasNext() && this.currentToken != "end" && this.currentToken != "else") {
            res.push(this.parseStatement());
        }
        return new program_1.default(res);
    };
    /**
     * Runs a test of the Parser, parsing the file `ParserTest.txt` in this directrory
     */
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
// If this is file is being run, run the test
if (require.main === module) {
    Parser.test();
}
