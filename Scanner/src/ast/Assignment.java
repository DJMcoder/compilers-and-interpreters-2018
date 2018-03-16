package ast;
import environment.Environment;

/**
 * An assignment statement assigns the result of an expression to a variable.
 * An assignment is written as:
 * 
 * x := expr;
 * 
 * Where x is the name of the variable and expr is the expression
 * 
 * @author David Melisso
 * @version 3/7/18
 */
public class Assignment extends Statement
{
    private String var;
    private Expression exp;
    
    /**
     * Creates an assignment object
     * 
     * @param v
     *  The name of the variable to set the expression to
     * @param e
     *  The expression to evaluate
     */
    public Assignment(String v, Expression e)
    {
        var = v;
        exp = e;
    }
    
    /**
     * Sets the variable to the result of the expression
     * 
     * @param env
     *  The environment in which to set the variable
     * @throws ASTException
     *  if there was an error evaluting the expression
     */
    public void exec(Environment env) throws ASTException
    {
        env.setVariable(var, exp.eval(env));
    }
}
