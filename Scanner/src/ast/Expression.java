package ast;
import environment.Environment;

/**
 * Expression stores values which it evaluates and returns the result, being either
 * an Integer, a String, or a Boolean
 * 
 * @version 3/16/18
 * @author David Melisso
 *
 */
public abstract class Expression
{   
    /**
     * Evaluates the expression and returns the result
     * 
     * @param env
     *  the environment which to evaluate the expression in
     * @return 
     *  the result of the expression
     * @throws ASTException
     *  if there was an error evaluating the expression
     */
    public abstract Object eval(Environment env) throws ASTException;
    
    /**
     * Throws a runtime exception because this method was not implemented in the child classes
     * @param e
     *  the Emitter which the child classes use to interface with the output file
     */
    public void compile(Emitter e)
    {
        throw new RuntimeException("Implement me!!!!!");
    }
}
