const Expression = require('./expression');

class Text extends Expression {
  constructor(text) {
    super();
    this.text = text.toString();
  }

  eval(env) {
    return this.text;
  }
}

module.exports = Text;
