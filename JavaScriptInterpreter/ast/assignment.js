const Statement = require('./statement');

class Assignment extends Statement {
  constructor(name, exp) {
    super();
    this.name = name;
    this.exp = exp;
  }

  exec(env) {
    env.setVariable(this.name, this.exp.eval(env));
  }
}

module.exports = Assignment;
