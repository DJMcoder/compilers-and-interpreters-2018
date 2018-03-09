package ast;
import environment.Environment;

/**
 * Writeln is a Statement which 
 * 
 * @author David Melisso
 * @version 3/7/18
 */
public class Writeln extends Statement
{
    Expression expr;
    
    public Writeln(Expression exp)
    {
        expr = exp;
    }
    
    public void exec(Environment env) throws ASTException
    {
        System.out.println(expr.eval(env));
    }
}
