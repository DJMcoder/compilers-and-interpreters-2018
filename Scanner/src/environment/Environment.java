package environment;
import java.util.HashMap;
import java.util.Map;

/**
 * Environment stores variables and the input stream.
 * 
 * @version 3/16/18
 * @author David Melisso
 *
 */
public class Environment
{
    private Map<String, Object> vars;
    private java.util.Scanner read;
    private Environment parentEnvironment;
    
    /**
     * Creates an environment with a default scanner
     */
    public Environment()
    {
        this(new java.util.Scanner(System.in));
    }
    
    /**
     * Creates an environment with a given input scanner
     * 
     * @param in
     *  the scanner to use
     */
    public Environment(java.util.Scanner in)
    {
        vars = new HashMap<String, Object>();
        read = in;
        parentEnvironment = null;
    }
    
    /**
     * Creates a sub Environment
     * 
     * @param e
     *  The previously made environment to copy from
     */
    public Environment(Environment parent)
    {
        this(parent.getScanner());
        parentEnvironment = parent;
    }
    
    /**
     * Gets a list of variable identifiers stored in this environment (and not the parent)
     * 
     * @return the list of variable identifiers as an array of Strings
     */
    public String[] getLocalVariables()
    {
        return vars.keySet().toArray(new String[0]);
    }
    
    /**
     * Associates the given variable name with the given value
     * 
     * @param variable
     *  the name of the variable
     * @param value
     *  the value to associate with the variable
     */
    public void setVariable(String variable, Object value)
    {
        if (parentEnvironment == null || parentEnvironment.getVariable(variable) == null)
        {
            vars.put(variable, value);
        }
        parentEnvironment.setVariable(variable, value);
    }
    
    /**
     * Returns the value associated with the given variable name 
     * 
     * @return the value associated with the given variable
     * @param variable
     *  the variable to get
     */
    public Object getVariable(String variable)
    {
        Object res = vars.get(variable);
        if (res == null)
        {
            return parentEnvironment.getVariable(variable);
        }
        return res;
    }
    
    /**
     * Gets the user input stream as a Scanner
     * 
     * @return the user input stream
     */
    public java.util.Scanner getScanner()
    {
        return read;
    }
}
