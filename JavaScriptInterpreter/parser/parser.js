const { Scanner } = require('../scanner');
const AST = require('../ast');
const Environment = require('../environment/environment');
const { Assignment, BinOp, Display, Expression, If, Program, Statement, Variable, While } = AST;

class Parser {

  constructor(inStream) {
    if (inStream instanceof Scanner) {
      this.scanner = inStream;
    }
    else {
      this.scanner = new Scanner(inStream);
    }

    this.currentToken = this.scanner.nextToken();
  }

  eat(expected) {
    if (this.currentToken == expected) {
      this.currentToken = this.scanner.nextToken();
    }
    else {
      throw new Error("[ERROR] Unexpected token " + this.currentToken +
                      ", expected " + expected);
    }
  }

  static isIdentifier(str) {
    return Scanner.isLetter(str.charAt(0));
  }

  parseValue() {
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
      return new AST.Boolean(true);
    }
    if (this.currentToken == "false") {
      this.eat("false");
      return new AST.Boolean(false);
    }

    // id
    if (Parser.isIdentifier(this.currentToken)) {
      var res = this.currentToken;
      this.eat(res);
      return new Variable(res);
    }

    // number
    var res = parseInt(this.currentToken);
    this.eat(this.currentToken);
    return new AST.Number(res);
  }

  parseNegExpr() {
    if (this.currentToken.charAt(0) == "-")
    {
      return -1 * this.parseValue();
    }
    return this.parseValue();
  }

  parseMultExpr() {
    var res = this.parseNegExpr();
    while (this.currentToken == "*" || this.currentToken == "/") {
      var operator = this.currentToken;
      this.eat(operator);
      var nextTerm = this.parseNegExpr();
      res = new BinOp(res, operator, nextTerm);
    }
    return res;
  }

  parseAddExpr() {
    var res = this.parseMultExpr();
    while (this.currentToken == "+" || this.currentToken == "-") {
      var operator = this.currentToken;
      this.eat(operator);
      var nextTerm = this.parseMultExpr();
      res = new BinOp(res, operator, nextTerm);
    }
    return res;
  }

  parseExpression() {
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

  parseDisplay() {
    this.eat("display")
    var expr
    if (this.currentToken.charAt(0) == "'") {
      expr = new AST.Text(this.currentToken.substr(1,this.currentToken.length - 2));
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

  parseAssign() {
    this.eat("assign");
    var toVar = this.currentToken;
    this.eat(toVar);
    this.eat("=");
    return new Assignment(toVar, this.parseExpression());
  }

  parseWhile() {
    this.eat("while");
    var condition = this.parseExpression();
    this.eat("do");
    var prog = this.parseProgram();
    this.eat("end");
    return new While(condition, prog);
  }

  parseIf() {
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
    var filename =  __dirname + "/ParserTest.txt";
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
