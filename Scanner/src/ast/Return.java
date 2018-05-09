package ast;

import java.util.HashSet;
import java.util.Set;

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
    
    /**
     * Puts the return address in $v0, jumps to the return address 
     * 
     * @param e 
     *  The interface for which to add code to the compiled file
     */
    public void compile(Emitter e)
    {
        if (returnValue == null)
        {
            e.emit("li $v0 0");
        }
        else
        {
            returnValue.compile(e);
        }
        e.emit("jr $ra");
    }
    
    /**
     * Gets a list of variables that are used within this block
     * @return
     *  an empty set
     */
    public Set<String> getUsedVariables()
    {
        return new HashSet<String>();
    }

}
