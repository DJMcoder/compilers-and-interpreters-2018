package ast;

import java.util.List;
import environment.Environment;

public class FunctionCall extends Expression
{
    private List<Expression> params;
    private String functionName;
    
    public FunctionCall(String f, List<Expression> p)
    {
        functionName = f;
        params = p;
    }

    @Override
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
