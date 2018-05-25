import Scanner from "../scanner/scanner";
import Expression from "../ast/expression";
import Boolean from "../ast/boolean";
import Number from "../ast/number";
import Text from "../ast/text";
import Variable from "../ast/variable";
import BinOp from "../ast/binop";
import Display from "../ast/display";
import Assignment from "../ast/assignment";
import While from "../ast/while";
import If from "../ast/if";
import Program from "../ast/program";
import Environment from "../environment/environment";

export default class Parser {
  scanner: Scanner;
  currentToken: string;

  constructor(inStream: Scanner | string) {
    if (inStream instanceof Scanner) {
      this.scanner = inStream;
    }
    else {
      this.scanner = new Scanner(inStream);
    }

    this.currentToken = this.scanner.nextToken();
  }

  eat(expected: string): void {
    if (this.currentToken == expected) {
      this.currentToken = this.scanner.nextToken();
    }
    else {
      throw new Error("[ERROR] Unexpected token " + this.currentToken +
                      ", expected " + expected);
    }
  }

  static isIdentifier(str: string): boolean {
    return Scanner.isLetter(str.charAt(0));
  }

  parseValue(): Expression {
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
      return new Boolean(true);
    }
    if (this.currentToken == "false") {
      this.eat("false");
      return new Boolean(false);
    }

    // id
    if (Parser.isIdentifier(this.currentToken)) {
      let res = this.currentToken;
      this.eat(res);
      return new Variable(res);
    }

    // number
    let res = parseInt(this.currentToken);
    this.eat(this.currentToken);
    return new Number(res);
  }

  parseNegExpr(): Expression {
    if (this.currentToken.charAt(0) == "-")
    {
      this.eat("-");
      return new BinOp(new Number(0), "-", this.parseValue());
    }
    return this.parseValue();
  }

  parseMultExpr(): Expression {
    var res = this.parseNegExpr();
    while (this.currentToken == "*" || this.currentToken == "/") {
      var operator = this.currentToken;
      this.eat(operator);
      var nextTerm = this.parseNegExpr();
      res = new BinOp(res, operator, nextTerm);
    }
    return res;
  }

  parseAddExpr(): Expression {
    var res = this.parseMultExpr();
    while (this.currentToken == "+" || this.currentToken == "-") {
      var operator = this.currentToken;
      this.eat(operator);
      var nextTerm = this.parseMultExpr();
      res = new BinOp(res, operator, nextTerm);
    }
    return res;
  }

  parseExpression(): Expression {
    var res = this.parseAddExpr();
    while (this.currentToken == "<"
        || this.currentToken == ">"
        || this.currentToken == ">="
        || this.currentToken == "<="
        || this.currentToken == "<>"
        || this.currentToken == "=" ) {
        var operator = this.currentToken;
        this.eat(operator);
        var nextTerm = this.parseAddExpr();
        res = new BinOp(res, operator, nextTerm);
    }
    return res;
  }

  parseDisplay(): Display {
    this.eat("display")
    let expr: Expression;
    if (this.currentToken.charAt(0) == "'") {
      expr = new Text(this.currentToken.substr(1,this.currentToken.length - 2));
      this.eat(this.currentToken);
    }
    else {
      expr = this.parseExpression();
    }
    if (this.currentToken == "read") {
      this.eat("read");
      var saveTo = this.currentToken;
      this.eat(saveTo);
      return new Display(expr, saveTo);
    }
    return new Display(expr);
  }

  parseAssign(): Assignment {
    this.eat("assign");
    var toVar = this.currentToken;
    this.eat(toVar);
    this.eat("=");
    return new Assignment(toVar, this.parseExpression());
  }

  parseWhile(): While {
    this.eat("while");
    var condition = this.parseExpression();
    this.eat("do");
    var prog = this.parseProgram();
    this.eat("end");
    return new While(condition, prog);
  }

  parseIf(): If {
    this.eat("if");
    var cond = this.parseExpression();
    this.eat("then");
    var then = this.parseProgram();
    if (this.currentToken == "else") {
      this.eat("else");
      var els = this.parseProgram();
      this.eat("end");
      return new If(cond, then, els);
    }
    this.eat("end");
    return new If(cond, then);
  }

  parseStatement() {
    if (this.currentToken == "display") return this.parseDisplay();
    if (this.currentToken == "assign" ) return this.parseAssign();
    if (this.currentToken == "while"  ) return this.parseWhile();
    if (this.currentToken == "if"     ) return this.parseIf();

    throw new Error("invalid statement: " + this.currentToken);
  }

  parseProgram() {
    var res = []
    res.push(this.parseStatement());
    while (this.scanner.hasNext() && this.currentToken != "end" && this.currentToken != "else") {
      res.push(this.parseStatement());
    }
    return new Program(res);
  }

  static test() {
    let filename: string =  __dirname + "/ParserTest.txt";
    try
    {
        var p = new Parser(filename);
        p.parseProgram().exec(new Environment());
    }
    catch (e)
    {
        console.error(e);
        process.exit(1);
    }
  }

}

// Main method
if (require.main === module)
{
  Parser.test();
}

module.exports = Parser;
