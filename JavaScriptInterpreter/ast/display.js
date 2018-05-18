const Statement = require('./statement');
const readlineSync = require('readline-sync');

class Display extends Statement {
  constructor(exp, readToVar) {
    super();
    this.exp = exp;
    this.readToVar = readToVar;
  }

  exec(env) {
    console.log(this.exp.eval(env));

    if (this.readToVar) {
      var toRead = readlineSync.question('');
      var toReadInt = parseInt(toRead);
      if (isNaN(toReadInt)) {
        throw new TypeError('Inputs must be integers; could not read input \'' + toRead + '\'')
      }
      env.setVariable(this.readToVar, toRead);
    }

  }
}

module.exports = Display;
