package ast;
import environment.Environment;

/**
 * Writeln is a Statement which prints an expression; written as:
 * 
 * WRITELN(x);
 * 
 * where x is the expression to write
 * 
 * @author David Melisso
 * @version 3/7/18
 */
public class Writeln extends Statement
{
    Expression expr;
    
    /**
     * Creates a Writeln object
     * @param exp
     *  the expression to write
     */
    public Writeln(Expression exp)
    {
        expr = exp;
    }
    
    /**
     * Prints the stored expression
     * 
     * @param env
     *  the environment in which to evaluate the expression
     *  
     * @throws ASTException
     *  if an exception occurs while evaluating the expression
     */
    public void exec(Environment env) throws ASTException
    {
        System.out.println(expr.eval(env));
    }
}
