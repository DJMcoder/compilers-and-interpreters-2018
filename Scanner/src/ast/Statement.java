package ast;

import environment.Environment;

/**
 * Statement is a line of code which can be executed
 * 
 * @version 3/16/18
 * @author David Melisso
 *
 */
public abstract class Statement
{
    /**
     * Executes the Statement
     * @param env
     *  the environment in which to execute the statement
     * @throws ASTException
     *  if there was an error executing the statement
     */
    public abstract void exec(Environment env) throws ASTException;
}
