package ast;
import environment.Environment;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

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
    /**
     * The list of substatements
     */
    protected List<Statement> stmts;
    
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
     * Gets a list of variables that are used within this block
     * @return
     *  a list of strings correlating to the variables used
     */
    public Set<String> getUsedVariables()
    {
        Set<String> res = new HashSet<String>();
        for (Statement stmt: stmts)
        {
            if (stmt instanceof Assignment)
            {
                res.add(((Assignment) stmt).getVariable());
            }
            else if (stmt instanceof Block)
            {
                res.addAll(((Block) stmt).getUsedVariables());
            }
        }
        return res;
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
    
    /**
     * Runs each of the substatements
     * 
     * @param e 
     *  The interface for which to add code to the compiled file
     */
    public void compile(Emitter e)
    {
        for (Statement stmt: stmts)
        {
            stmt.compile(e);
        }
    }
}
