package ast;
import environment.Environment;

public class Number extends Expression
{
    private Integer value;
    
    public Number(int val)
    {
        value = Integer.valueOf(val);
    }
    
    public Number(Integer val)
    {
        value = val;
    }
    
    public Integer eval(Environment e)
    {
        return value;
    }
}
