package ast;

import environment.Environment;

/**
 * Return is Statement which when executed throws a ReturnException
 * 
 * @version 3/17/18
 * @author David Melisso
 *
 */
public class Return extends Statement
{
    private Expression returnValue;
    
    /**
     * Creates a Return Statement with no return value
     */
    public Return()
    {
        returnValue = null;
    }
    
    /**
     * Creates a Return Statement with a given return value
     * 
     * @param returnValue
     *  the return value of the Return Statement
     */
    public Return(Expression returnValue)
    {
        this.returnValue = returnValue;
    }

    /**
     * Throws a ReturnException.
     * 
     * @param env
     *  the current environment
     * @throws ReturnException   
     *  always
     * @throws ASTException
     *  when there was an error evaluating the return expression
     */
    public void exec(Environment env) throws ASTException
    {
        if (returnValue == null)
        {
            throw new ReturnException();
        }
        throw new ReturnException(returnValue.eval(env)); 
    }

}
