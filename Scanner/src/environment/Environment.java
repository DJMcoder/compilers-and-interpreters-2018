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
    
    /**
     * Creates an environment with a System.in input stream
     */
    public Environment()
    {
        vars = new HashMap<String, Object>();
        read = new java.util.Scanner(System.in);
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
    }
    
    public Environment(Environment e)
    {
        read = e.getScanner();
        vars = new HashMap<String, Object>();
        
        for (String curVar: e.getVariables())
        {
            this.setVariable(curVar, e.getVariable(curVar));
        }
    }
    
    public String[] getVariables()
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
        vars.put(variable, value);
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
        return vars.get(variable);
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
