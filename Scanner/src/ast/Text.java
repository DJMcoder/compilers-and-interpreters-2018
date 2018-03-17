package ast;
import environment.Environment;
import java.util.List;

/**
 * Text is an Expression which stores a String.
 * 
 * @version 3/16/18
 * @author David Melisso
 *
 */
public class Text extends Expression
{
    private String str;
    private Expression expr;
    private List<Text> strs;
    
    /**
     * 0 - a String
     * 1 - a List of Texts
     * 2 - an Expression
     */
    private int type;
    
    /**
     * Creates Text from a String
     * @param s
     *  The String to store
     */
    public Text(String s)
    {
        str = s;
        type = 0;
    }
    
    /**
     * Creates Text from a list of Texts
     * @param t
     *  The List of Texts
     */
    public Text(List<Text> t)
    {
        strs = t;
        type = 1;
    }
    
    /**
     * Creates Text from an Expression
     * @param e
     *  The Expression to store
     */
    public Text(Expression e)
    {
        expr = e;
        type = 2;
    }
    
    /**
     * Attempts to parse the text as an integer
     * @return a Number object with the integer
     * @throws NumberFormatException
     *  if the string could not be converted to an integer
     */
    public Number toNum() throws NumberFormatException
    {
        return new ast.Number(Integer.parseInt(str));
    }
    
    /**
     * Returns the stored string or the evaluated expression, depending on which is
     * stored.
     * 
     * @return the stored string or the evaluated expression
     * @param e
     *  the environment in which to evaluate the expression
     * @throws ASTException
     *  if there was an error evaluating the expression, or if the type of Text was
     *  invalid
     */
    public String eval(Environment e) throws ASTException
    {
        if (type == 0)
        {
            return this.str;
        }
        if (type == 1)
        {
            String res = "";
            for (Text stri: strs)
            {
                res += stri.eval(e);
            }
            return res;
        }
        if (type == 2)
        {
            return expr.eval(e).toString();
        }
        throw new ASTException("Invalid Text type " + type);
    }
}
