const Statement = require('./statement');

class If extends Statement {
  constructor(condition, program, els) {
    super();
    this.condition = condition;
    this.program = program;
    this.else = els;
  }

  exec(env) {
    if (this.condition.eval(env)) {
      this.program.exec(env);
    }
    else if (this.else) {
      this.else.exec(env);
    }
  }
}

module.exports = If;
