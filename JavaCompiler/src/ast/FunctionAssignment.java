package ast;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

import environment.Environment;

/**
 * FunctionAssignment is a Statement which defines a function in an Environment
 * 
 * @version 3/17/18
 * @author David Melisso
 *
 */
public class FunctionAssignment extends Statement
{
    private String functionName;
    private Function function;
    
    /**
     * Creates a Function Assignment from the function name, List of Parameters, and
     * Statement
     * 
     * @param name
     *  the name of the function
     * @param params
     *  the List of Strings representing the variable names for parameters of the function
     * @param statement
     *  the Statement which the function executes when called
     */
    public FunctionAssignment(String name, List<String> params, Statement statement)
    {
        functionName = name;
        function = new Function(params, statement);
    }

    /**
     * Assigns the Function to its functionName in the current environment
     * 
     * @param env
     *  the environment in which to set the variable
     */
    public void exec(Environment env)
    {
        env.setVariable(functionName, function);
    }
    
    /**
     * Gets the function (procedure) which contains the parameters and statement
     * @return the Function associated with this FunctionAssignment
     */
    public Function getFunction()
    {
        return function;
    }
    
    /**
     * Defines a procedure and skips it (to be called later).
     * 
     * @param e 
     *  The interface for which to add code to the compiled file
     */
    public void compile(Emitter e)
    {
        e.emit("j skipproc" + functionName);
        
        e.emit("proc" + functionName + ":");
        e.setProcedureContext(this);
        function.getStatement().compile(e);
        e.emit("jr $ra");
        e.clearProcedureContext();
        
        e.emit("skipproc" + functionName + ":");
    }

    /**
     * Gets a list of variables that are used within this block
     * @return
     *  an empty set
     */
    public Set<String> getUsedVariables()
    {
        return new HashSet<String>();
    }

}
