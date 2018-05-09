package ast;
import java.util.HashSet;
import java.util.Set;

import environment.Environment;

/**
 * A Readln statement is a Statement that assigns user input to a variable.
 * Readln is written as:
 * 
 * READLN(x);
 * 
 * Where x is the variable to assign the user input to.
 * 
 * @author David Melisso
 * @version 3/7/18
 */
public class Readln extends Statement
{
    private String var;
    
    /**
     * Creates a Readln object
     * 
     * @param v
     *  the name of the variable to set the input to
     */
    public Readln(String v)
    {
        var = v;
    }
    
    /**
     * Reads user input and sets it to the named variable
     * 
     * @param env
     *  The environment in which the variables are stored
     */
    public void exec(Environment env)
    {
        env.setVariable(var, env.getScanner().nextLine());
    }

    /**
     * Gets a list of variables that are used within this block
     * @return
     *  an empty set
     */
    public Set<String> getUsedVariables()
    {
        return new HashSet<String>();
    }
}
