const Statement = require('./statement');

class While extends Statement {
  constructor(condition, program) {
    super();
    this.condition = condition;
    this.program = program;
  }

  exec(env) {
    while (this.condition.eval(env)) {
      this.program.exec(env);
    }
  }
}

module.exports = While;
