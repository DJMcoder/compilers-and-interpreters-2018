package ast;
import environment.Environment;

public class Variable extends Expression
{
    private String name;
    
    public Variable(String n)
    {
        n = name;
    }
    
    public Object eval(Environment env)
    {
        Object res = env.getVariable(name);
        if (res == null)
        {
            throw new IllegalArgumentException("Variable '" + name
                    + "' does not exist");
        }
        return res;
    }
}
