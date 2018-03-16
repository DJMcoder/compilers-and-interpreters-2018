package ast;
import environment.Environment;
import java.util.List;

/**
 * A block statement is a list of multiple statements, in the format:
 * 
 * BEGIN
 *   stmt
 *   stmt
 *   stmt
 *   ...
 * END;
 * 
 * @author David Melisso
 * @version 3/7/2018
 *
 */
public class Block extends Statement
{
    List<Statement> stmts;
    
    /**
     * Creates a Block object
     * 
     * @param s
     *  the list of statements in the block
     */
    public Block(List<Statement> s)
    {
        stmts = s;
    }
    
    /**
     * Executes each statement inside the block
     * 
     * @param env
     *  The environment to execute the statements in
     * @throws ASTException
     *  if there was an error executing one of the statements
     */
    public void exec(Environment env) throws ASTException
    {
        for (Statement stmt: stmts)
        {
            stmt.exec(env);
        }
    }
}
