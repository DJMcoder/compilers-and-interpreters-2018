const Expression = require('./expression');

class Bool extends Expression {
  constructor(bool) {
    super();
    this.bool = bool == true;
  }

  eval(env) {
    return this.bool;
  }
}

module.exports = Bool;
