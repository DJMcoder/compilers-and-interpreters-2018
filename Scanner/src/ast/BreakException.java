package ast;

/**
 * BreakException is an exception thrown when the BREAK statement occurs. 
 * Expected to be caught by the While Statement, exiting the loop.
 * 
 * @version 3/16/18
 * @author David Melisso
 *
 */
public class BreakException extends ASTException
{
    /**
     * default constructor for BreakException.
     */
    public BreakException() 
    {
        super("Unexpected BREAK statement outside of loop");
    }
}
