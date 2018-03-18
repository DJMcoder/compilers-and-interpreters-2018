package ast;
import java.util.List;

import environment.Environment;

public class FunctionAssignment extends Statement
{
    private String functionName;
    private Function function;
    
    public FunctionAssignment(String f, List<String> params, Statement statement)
    {
        functionName = f;
        function = new Function(params, statement);
    }

    public void exec(Environment env) throws ASTException
    {
        env.setVariable(functionName, function);
    }

}
