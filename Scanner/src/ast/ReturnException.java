package ast;

public class ReturnException extends ASTException
{
    private Object returnValue; 
    
    public ReturnException()
    {
        
    }
    
    public ReturnException(Object returnValue)
    {
        this.returnValue = returnValue;
    }
    
    public Object getReturnValue()
    {
        return returnValue;
    }
}
