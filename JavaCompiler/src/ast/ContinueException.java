package ast;

/**
 * BreakException is an exception thrown when the BREAK statement occurs. 
 * Expected to be caught by the While Statement, skipping the current iteration of the 
 * loop.
 * 
 * @version 3/16/18
 * @author David Melisso
 *
 */
public class ContinueException extends ASTException
{
    /**
     * default constructor for BreakException.
     */
    public ContinueException() 
    {
        super("Unexpected CONTINUE statement outside of loop");
    }
}
