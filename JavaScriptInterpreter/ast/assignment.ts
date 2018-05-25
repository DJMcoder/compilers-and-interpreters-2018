import Statement from './statement';
import Expression from './expression';
import Environment from '../environment/environment';

export default class Assignment extends Statement {
  name: string;
  exp: Expression;

  constructor(name: string, exp: Expression) {
    super();
    this.name = name;
    this.exp = exp;
  }

  exec(env: Environment): void {
    env.setVariable(this.name, this.exp.eval(env));
  }
}