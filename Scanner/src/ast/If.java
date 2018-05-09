package ast;

import java.util.HashSet;
import java.util.Set;

import environment.Environment;

/**
 * If is a Statement which evaluates its "then" sub-statement if its condition is true.
 * It can also have an "else" sub-statement which evaluates otherwise.
 * 
 * If Statements come in the forms:
 * 
 * IF cond THEN stmt
 * IF cond THEN stmt ELSE stmt2
 * 
 * Where cond is the condition, stmt is the statment to be evaluated if cond is true,
 * and stmt2 is the statement to be evaluated if cond if false
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
    
    /**
     * Runs the then statement if the condition is true,
     * Runs the else statement if the condition is untrue and an else statement exists
     * 
     * @param e
     *  The interface for which to add code to the compiled file
     */
    public void compile(Emitter e)
    {
        if (hasElse)
        {
            String endif = "endif" + Integer.toString(e.getNextLabel());
            String startelse = "else" + Integer.toString(e.getNextLabel());
            
            condition.compile(e, startelse);
            then.compile(e);
            e.emit("j "+endif);
            
            e.emit(startelse + ":");
            els.compile(e);
            e.emit(endif + ":");
        }
        else
        {
            String endif = "endif" + Integer.toString(e.getNextLabel());
            
            condition.compile(e, endif);
            then.compile(e);
            e.emit(endif + ":");
        }
    }

    /**
     * Gets a list of variables that are used within this block
     * @return
     *  a list of strings correlating to the variables used
     */
    public Set<String> getUsedVariables()
    {
        Set<String> set = new HashSet<String>();
        set.addAll(els.getUsedVariables());
        if (hasElse)
        {
            set.addAll(then.getUsedVariables());
        }
        return set;
    }

}
