package ast;

import environment.Environment;

/**
 * Break is a Statement which when executed throws a ContinueException.
 * 
 * @version 3/16/18
 * @author David Melisso
 *
 */
public class Continue extends Statement
{

    /**
     * Throws a ContinueException.
     * 
     * @param env
     *  the current environment
     * @throws ContinueException   
     *  always
     */
    public void exec(Environment env) throws ContinueException
    {
        throw new ContinueException();
    }

}
