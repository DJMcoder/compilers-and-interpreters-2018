package ast;

import environment.Environment;

public class Return extends Statement
{
    private Expression returnValue;
    
    public Return()
    {
        returnValue = null;
    }
    
    public Return(Expression returnValue)
    {
        this.returnValue = returnValue;
    }

    @Override
    public void exec(Environment env) throws ASTException
    {
        throw new ReturnException(returnValue.eval(env)); 
    }

}
