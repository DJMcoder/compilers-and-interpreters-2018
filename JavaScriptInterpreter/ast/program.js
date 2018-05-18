const Statement = require('./statement');

class Program extends Statement {
  constructor(stmts) {
    super();
    this.stmts = stmts;
  }

  exec(env) {
    for (var stmt of this.stmts) {
      stmt.exec(env);
    }

  }
}

module.exports = Program;
