package ast;
import java.util.List;

/**
 * Function stores a List of Parameters and a Statement; the two components of a function
 * 
 * @version 3/17/18
 * @author David Melisso
 *
 */
public class Function
{
    private List<String> params;
    private Statement statement;
    
    /**
     * Creates a function from a List of Parameters and a Statement
     * 
     * @param params
     *  the List of Parameters to create the Function with
     * @param statement
     *  the Statement to create the Function with
     */
    public Function(List<String> params, Statement statement)
    {
        this.params = params;
        this.statement = statement;
    }
    
    /**
     * Returns this Function's List of Parameters
     * 
     * @return the List of Paramters
     */
    public List<String> getParams()
    {
        return params;
    }
    
    /**
     * Returns this Function's Statement
     * 
     * @return the Statement
     */
    public Statement getStatement()
    {
        return statement;
    }
}
