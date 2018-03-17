package ast;

import environment.Environment;

/**
 * While is a Statement which executes its sub-statement iteratively while its condition
 * evaluates to true.
 * While Statements come in the form:
 * 
 * WHILE cond DO stmt
 * 
 * Where cond is the condition to evaluate and stmt is the statement to execute.
 * 
 * @version 3/16/18
 * @author David Melisso
 *
 */
public class While extends Statement
{
    private Condition condition;
    private Statement statement;

    /**
     * Creates a While Object given its condition and statement
     * @param c
     *  the condition to check
     * @param s
     *  the statement to execute while the condition evaluates to true
     */
    public While(Condition c, Statement s)
    {
        condition = c;
        statement = s;
    }
    
    /**
     * Executes the statement while the condition is true, handles BREAK and CONTINUE
     * statements by exiting the loop and skipping the current iteration of the loop,
     * respectively.
     * 
     * @param env
     *  the environment to evaluate the condition and execute the statement in
     * @throws ASTException 
     *  if the evaluation or execution throws an ASTException
     */
    public void exec(Environment env) throws ASTException
    {
        boolean notBroken = true;
        while (notBroken && (Boolean)condition.eval(env))
        {
            try
            {
                statement.exec(env);
            }
            catch(ContinueException e)
            {
                // do nothing, this automatically skips the loop
            }
            catch(BreakException e)
            {
                notBroken = false;
            }
        }
    }

}
