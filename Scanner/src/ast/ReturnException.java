package ast;

/**
 * ReturnException is an ASTException which contains a return value.
 * 
 * @version 3/17/18
 * @author David Melisso
 *
 */
public class ReturnException extends ASTException
{
    private Object returnValue; 
    
    /**
     * Creates a ReturnException with a null return value
     */
    public ReturnException()
    {
        this.returnValue = null;
    }
    
    /**
     * Creates a ReturnException with a given return value
     * 
     * @param returnValue
     *  the value to set the returnValue to
     */
    public ReturnException(Object returnValue)
    {
        this.returnValue = returnValue;
    }
    
    /**
     * Returns the returnValue
     * 
     * @return the returnValue
     */
    public Object getReturnValue()
    {
        return returnValue;
    }
}
