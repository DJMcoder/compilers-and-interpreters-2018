package ast;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import environment.Environment;

/**
 * FunctionCallStatement is a Statement which calls a function and does not pay attention
 * to its return value.
 * 
 * @version 3/17/18
 * @author David Melisso
 *
 */
public class FunctionCallStatement extends Statement
{
    private List<Expression> params;
    private String functionName;
    
    /**
     * Creates a FunctionCallStatement with the function name to call and list of 
     * parameters.
     * 
     * @param functionName
     *  the name of the function to call
     * @param params
     *  the list of Expressions to fill in the parameters with when called
     */
    public FunctionCallStatement(String functionName, List<Expression> params)
    {
        this.functionName = functionName;
        this.params = params;
    }

    /**
     * Creates a new environment to evaluate the function in, filled in with the values
     * of the parameters given, then calls the function in this environment.
     * 
     * @param env
     *  the original environment in which the function is called
     * @throws ASTException
     *  when the function was not defined as a function, when the parameters are of the 
     *  wrong length, or when there is an error evaluating the function
     */
    public void exec(Environment env) throws ASTException
    {
        Environment funcEnv = new Environment(env);
        try
        {
            Function thisFunc = (Function)funcEnv.getVariable(functionName);
            if (thisFunc == null)
            {
                throw new ASTException("Function " + functionName + " was never defined");
            }
            
            int givenParamsLength = params.size();
            int requiredParamsLength = thisFunc.getParams().size();
            if (givenParamsLength != requiredParamsLength)
            {
                throw new ASTException("Error calling function " + functionName + 
                        "Given " + givenParamsLength +" parameters, " + 
                        requiredParamsLength + " required");
            }
            
            for (int i = 0; i < givenParamsLength; i++)
            {
                funcEnv.setVariable(thisFunc.getParams().get(i),
                                    params.get(i).eval(env));
            }
            
            try
            {
                thisFunc.getStatement().exec(funcEnv);
            }
            catch(ReturnException e)
            {
                return;
            }
            return;
        }
        catch (ClassCastException e)
        {
            throw new ASTException("Cannot call variable " + functionName + " because "
                    + "it is not a function");
        }
    }
    
    /**
     * Saves the return address to the stack,
     * Adds parameters to the stack
     * Jumps to a procedure
     * 
     * @param e 
     *  The interface for which to add code to the compiled file
     */
    public void compile(Emitter e)
    {
        e.emitPush("$ra");
        for (Expression arg: params)
        {
            arg.compile(e);
            e.emitPush("$v0");
        }
        
        e.emit("jal proc" + functionName);
        
        
        for (int i = 0; i < params.size(); i++)
        {
            e.emitPop();
        }
        e.emitPop("$ra");
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
