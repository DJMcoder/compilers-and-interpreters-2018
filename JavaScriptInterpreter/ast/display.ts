import Statement from './statement';
import readlineSync from 'readline-sync';
import Expression from './expression';
import Environment from '../environment/environment';

export default class Display extends Statement {
  exp: Expression;
  readToVar: string | void;
  constructor(exp: Expression, readToVar?: string) {
    super();
    this.exp = exp;
    this.readToVar = readToVar;
  }

  exec(env: Environment) {
    console.log(this.exp.eval(env));

    if (this.readToVar) {
      var toRead = readlineSync.question('');
      if (toRead == 'true') {
        env.setVariable(this.readToVar, true);
        return;
      }
      if (toRead == 'false') {
        env.setVariable(this.readToVar, false);
        return;
      }
      var toReadInt = parseInt(toRead);
      if (isNaN(toReadInt)) {
        throw new TypeError('Inputs must be integers; could not read input \'' + toRead + '\'')
      }
      env.setVariable(this.readToVar, toReadInt);
    }

  }
}
