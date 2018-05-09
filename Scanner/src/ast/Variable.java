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
    
    /**
     * Loads the value stored in the variable to $v0
     * 
     * @param e
     *  The interface for which to add code to the compiled file
     */
    public void compile(Emitter e)
    {
        if (e.getProcedureContext() != null && e.isLocalVariable(name))
        {
            e.emit("lw $v0 " + Integer.toString(e.getOffset(name)) + "($sp)");
        }
        else
        {
            e.emit("la $t0 var"+name);
            e.emit("lw $v0 ($t0)");
        }
    }
}
