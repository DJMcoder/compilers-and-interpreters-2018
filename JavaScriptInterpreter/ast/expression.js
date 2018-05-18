/**
 * Expression stores values which it evaluates and returns the result, being either
 * an Integer, a String, or a Boolean
 *
 * @version 3/16/18
 * @author David Melisso
 *
 */
class Expression
{
    constructor() {
      if (new.target === Expression) {
        throw new TypeError("Cannot construct Expression instances directly");
      }

      if (typeof this.eval !== 'function') {
        throw new TypeError("Must override method eval");
      }
    }

    /**
     * https://stackoverflow.com/questions/29480569/does-ecmascript-6-have-a-convention-for-abstract-classes
     * Evaluates the expression and returns the result
     *
     * @param env
     *  the environment which to evaluate the expression in
     * @return
     *  the result of the expression
     * @throws ASTException
     *  if there was an error evaluating the expression
     */

}

module.exports = Expression;
