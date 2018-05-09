package ast;

import java.util.HashSet;
import java.util.Set;

import environment.Environment;

/**
 * Break is a Statement which when executed throws a BreakException.
 * 
 * @version 3/16/18
 * @author David Melisso
 *
 */
public class Break extends Statement
{

    /**
     * Throws a BreakException.
     * 
     * @param env
     *  the current environment
     * @throws BreakException   
     *  always
     */
    public void exec(Environment env) throws BreakException
    {
        throw new BreakException();
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
