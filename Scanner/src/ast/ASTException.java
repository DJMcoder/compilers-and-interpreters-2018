package ast;

/**
 * ASTException is a sub class of Exception and is thrown to indicate a 
 * scanning error.  Usually, the scanning error is the result of an illegal 
 * token or token type in the input stream. 
 * 
 * @author David Melisso
 * @version 3/7/18
 *
 */
public class ASTException extends Exception
{
    /**
     * default constructor for ASTErrorObjects.
     */
    public ASTException() 
    {
        super();
    }
    /**
     * Constructor for ASTErrorObjects that includes a reason for the error.
     * @param reason - the reason for the error
     */
    public ASTException(String reason) 
    {
        super(reason);
    }
}
