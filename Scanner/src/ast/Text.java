package ast;
import environment.Environment;

public class Text extends Expression
{
    private String str;
    
    public Text(String s)
    {
        str = s;
    }
    
    public String eval(Environment e)
    {
        return str;
    }
}
