package ast;

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
     */
    public void exec(Environment env) throws ASTException
    {
        throw new BreakException();
    }

}
