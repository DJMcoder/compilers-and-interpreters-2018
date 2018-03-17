package ast;

import environment.Environment;

/**
 * If is a Statement which evaluates its "then" sub-statement if its condition is true.
 * It can also have an "else" sub-statement which evaluates otherwise.
 * 
 * @version 3/16/18
 * @author David Melisso
 *
 */
public class If extends Statement
{
    private Condition condition;
    private Statement then;
    private Statement els;
    private boolean hasElse;

    /**
     * Creates an If Statement with a condition and "then" sub-statement.
     * 
     * @param c
     *  the condition
     * @param t
     *  the "then" sub-statement, which executes if the condition is true
     */
    public If(Condition c, Statement t)
    {
        condition = c;
        then = t;
        hasElse = false;
    }
    
    /**
     * Creates an If Statement with a condition and "then" and "else" sub-statements.
     * 
     * @param c
     *  the condition
     * @param t
     *  the "then" sub-statement, which executes if the condition is true
     * @param e
     *  the "else" sub-statement, which executes if the condition is false
     */
    public If(Condition c, Statement t, Statement e)
    {
        condition = c;
        then = t;
        els = e;
        hasElse = true;
    }
    
    /**
     * Runs the "then" sub-statement if the condition is true,
     * Runs the "else" sub-statement if it has one and the condition is false.
     * 
     * @param env
     *  The environment in which to evaluate the condition and execute the sub-statements
     * @throws ASTException
     *  if one of the sub-statement throws an ASTException
     */
    public void exec(Environment env) throws ASTException
    {
        if ((Boolean)condition.eval(env))
        {
            then.exec(env);
        }
        else if (hasElse)
        {
            els.exec(env);
        }
    }

}
