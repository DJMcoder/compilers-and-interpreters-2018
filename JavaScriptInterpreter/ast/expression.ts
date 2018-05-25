import Environment from "../environment/environment";

/**
 * Expression stores values which it evaluates and returns the result, being either
 * an Integer, a String, or a Boolean
 *
 * @version 3/16/18
 * @author David Melisso
 *
 */
export default abstract class Expression
{
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
  abstract eval(env: Environment): number | boolean;
}