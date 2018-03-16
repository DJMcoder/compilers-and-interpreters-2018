package ast;
import environment.Environment;

/**
 * Variable is an Expression which stores the identifier of a variable and when 
 * evaluated returns the value stored at the variable.
 * 
 * @version 3/16/18
 * @author David Melisso
 *
 */
public class Variable extends Expression
{
    private String name;
    
    /**
     * Creates a Variable
     * @param n
     *  the name of the variable
     */
    public Variable(String n)
    {
        name = n;
    }
    
    /**
     * Returns the value stored in the variable
     * 
     * @param env
     *  the environment which stores the variable
     * @return the value stored in the variable
     * @throws ASTException
     *  if the variable doesn't exist
     */
    public Object eval(Environment env) throws ASTException
    {
        Object res = env.getVariable(name);
        if (res == null)
        {
            throw new ASTException("Variable '" + name
                    + "' does not exist");
        }
        return res;
    }
}
