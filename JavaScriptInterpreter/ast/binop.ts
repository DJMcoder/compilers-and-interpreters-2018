import Expression from './expression'
import Environment from '../environment/environment';

function trunc(v: any) {
  v = +v;
  if (!isFinite(v)) return v;

  return (v - v % 1) || (v < 0 ? -0 : v === 0 ? v : 0);
};

export default class BinOp extends Expression {
  exp1: Expression;
  op: string;
  exp2: Expression;
  single: boolean;

  constructor(exp1: Expression, op: string, exp2: Expression) {
    super();
    this.exp1 = exp1;
    this.op = op;
    this.exp2 = exp2;
    this.single = (typeof op === 'undefined' || typeof exp2 === 'undefined');
  }

  eval(env: Environment): number | boolean {
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

    if (this.op == "*" ) return trunc(trunc(Number(num1)) * trunc(Number(num2)));
    if (this.op == "/" ) return trunc(trunc(Number(num1)) / trunc(Number(num2)));
    if (this.op == "+" ) return trunc(trunc(Number(num1)) + trunc(Number(num2)));
    if (this.op == "-" ) return trunc(trunc(Number(num1)) - trunc(Number(num2)));

    throw new Error("Invalid operator " + this.op);
  }
}