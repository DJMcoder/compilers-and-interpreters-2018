const Expression = require('./expression');

class Num extends Expression {
  constructor(num) {
    super();
    this.num = parseInt(num);
  }

  eval(env) {
    return this.num;
  }
}

module.exports = Num;
