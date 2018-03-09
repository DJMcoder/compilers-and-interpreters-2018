package environment;
import java.util.HashMap;
import java.util.Map;

public class Environment
{
    private Map<String, Object> vars;
    private java.util.Scanner read;
    
    public Environment()
    {
        vars = new HashMap<String, Object>();
        read = new java.util.Scanner(System.in);
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
     * returns the value associated with the given variable name 
     * 
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
