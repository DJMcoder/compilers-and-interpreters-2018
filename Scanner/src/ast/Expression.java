package ast;
import environment.Environment;

public abstract class Expression
{   
    public abstract Object eval(Environment e) throws ASTException;
}
