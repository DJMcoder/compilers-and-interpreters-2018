package ast;

import java.util.List;
import environment.Environment;

/**
 * A FunctionCall is an Expression which, when evaluated, calls the function and
 * returns the value returned within the function.
 *  
 * @version 3/17/18
 * @author David Melisso
 *
 */
public class FunctionCall extends Expression
{
    private List<Expression> params;
    private String functionName;
    
    /**
     * Creates a FunctionCall given the function name and parameters given.
     * 
     * @param functionName
     *  The name of the function to call
     * @param params
     *  The parameters, filled in as Expressions, to evaluate the function with
     */
    public FunctionCall(String functionName, List<Expression> params)
    {
        this.functionName = functionName;
        this.params = params;
    }

    /**
     * Creates a new environment to evaluate the function in, filled in with the values
     * of the parameters given, then calls the function in this environment and returns 
     * the value returned in the function
     * 
     * @param env
     *  the original environment in which the function is called
     * @return the value returned in the function
     * @throws ASTException
     *  when the function was not defined as a function, when the parameters are of the 
     *  wrong length, when the function does not return a result, or when there is an
     *  error evaluating the function
     */
    public Object eval(Environment env) throws ASTException
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
                return e.getReturnValue();
            }
            throw new ASTException("Function " + functionName + " did not return a result");
        }
        catch (ClassCastException e)
        {
            throw new ASTException("Cannot call variable " + functionName + " because "
                    + "it is not a function");
        }
    }

}
