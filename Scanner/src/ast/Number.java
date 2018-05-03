package ast;
import environment.Environment;

/**
 * A Number is an Expression which stores an integer
 * 
 * @version 3/16/18
 * @author David Melisso
 *
 */
public class Number extends Expression
{
    private Integer value;
    
    /**
     * Creates a Number object with an integer
     * @param val
     *  the value of the integer to store
     */
    public Number(int val)
    {
        value = Integer.valueOf(val);
    }
    
    /**
     * Creates a Number object with an integer
     * @param val
     *  the integer to store
     */
    public Number(Integer val)
    {
        value = val;
    }
    
    /**
     * Returns the value of the integer
     * 
     * @return the value of the stored integer
     * @param e
     *  the current environment
     */
    public Integer eval(Environment e)
    {
        return value;
    }
    
    /**
     * Loads this number into the immediate register $v0
     * 
     * @param e
     *  The interface for which to add code to the compiled file
     */
    public void compile(Emitter e) 
    {
        e.emit("li $v0 " + Integer.toString(value));
    }
}
