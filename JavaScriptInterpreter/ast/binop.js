const Expression = require('./expression');

class BinOp extends Expression {
  constructor(exp1, op, exp2) {
    super();
    this.exp1 = exp1;
    this.op = op;
    this.exp2 = exp2;
    this.single = (typeof op === 'undefined' || typeof exp2 === 'undefined');
  }

  eval(env) {
    if (this.single) return this.exp1.eval(env);

    var num1 = this.exp1.eval(env);
    var num2 = this.exp2.eval(env);

    //console.log('exp2 =', this.exp2)
    //console.log('num2 =', num2)

    if (this.op == "<" ) return true == (num1 <  num2);
    if (this.op == ">" ) return true == (num1 >  num2);
    if (this.op == ">=") return true == (num1 >= num2);
    if (this.op == "<=") return true == (num1 <= num2);
    if (this.op == "<>") return true == (num1 != num2);
    if (this.op == "=" ) return true == (num1 == num2);

    if (this.op == "*" ) return parseInt(num1) * parseInt(num2);
    if (this.op == "/" ) return parseInt(num1) / parseInt(num2);
    if (this.op == "+" ) return parseInt(num1) + parseInt(num2);
    if (this.op == "-" ) return parseInt(num1) - parseInt(num2);

    throw new Error("Invalid operator " + op);
  }
}

module.exports = BinOp;
