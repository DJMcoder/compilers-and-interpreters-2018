package ast;
import java.util.List;

public class Function
{
    public List<String> params;
    public Statement statement;
    
    public Function(List<String> p, Statement s)
    {
        params = p;
        statement = s;
    }
    
    public List<String> getParams()
    {
        return params;
    }
    
    public Statement getStatement()
    {
        return statement;
    }
}
