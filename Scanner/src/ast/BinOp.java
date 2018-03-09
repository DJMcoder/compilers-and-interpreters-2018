package ast;
import environment.Environment;

public class BinOp extends Expression
{
    private Expression exp1;
    private Expression exp2;
    private String op;
    
    public BinOp(Expression exp1, String op, Expression exp2)
    {
    }
    
    public Object eval(Environment env) throws ASTException
    {
        Object val1 = exp1.eval(env);
        Object val2 = exp2.eval(env);
        
        if (!(val1 instanceof Integer) || !(val2 instanceof Integer))
        {
            throw new ASTException("Invalid operator " + op);
        }
        
        Integer num1 = (Integer)val1;
        Integer num2 = (Integer)val2;
        
        if (op.equals("*"))
        {
            return num1 * num2;
        }
        if (op.equals("mod"))
        {
            return num1 % num2;
        }
        if (op.equals("/"))
        {
            return num1 / num2;
        }
        if (op.equals("+"))
        {
            return num1 + num2;
        }
        if (op.equals("-"))
        {
            return num1 - num2;
        }
        
        throw new ASTException("Invalid operator " + op);
        
    }
}
