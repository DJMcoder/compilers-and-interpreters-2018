const Expression = require('./expression');

class Variable extends Expression {
  constructor(name) {
    super();
    this.name = name;
  }

  eval(env) {
    return env.getVariable(this.name);
  }
}

module.exports = Variable;
