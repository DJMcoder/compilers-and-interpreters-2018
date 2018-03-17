package ast;
import environment.Environment;

/**
 * BinOp is an Expression which stores the different components of a single binary
 * operation; i.e. two Expressions evaluated with a joining operator (either +, -, *, /
 * or mod)
 * Can also be a single Expression, which just returns the Expression.
 * 
 * @version 3/16/18
 * @author David Melisso
 *
 */
public class BinOp extends Expression
{
    /**
     * The first (left) expression
     */
    protected Expression exp1;
    /**
     *  the second (right) expression
     */
    protected Expression exp2; 
    /**
     * The operator to use between the expressions
     */
    protected String op;
    /**
     * Whether the 
     * true if only exp1 is used;
     * false otherwise
     */
    protected boolean single;
    
    /**
     * Creates a BinOp Object given the references to two expressions, in the correct
     * order, and the joining operator.
     * @param exp1
     *  the first expression (the one on the left)
     * @param op
     *  the joining operator
     * @param exp2
     *  the second expression
     */
    public BinOp(Expression exp1, String op, Expression exp2)
    {
        this.exp1 = exp1;
        this.exp2 = exp2;
        this.op = op;
        this.single = false;
    }
    
    /**
     * Creates a BinOp Object given a single Expression.
     * @param exp
     *  The Expression to store
     */
    public BinOp(Expression exp)
    {
        this.single = true;
        this.exp1 = exp;
    }
    
    /**
     * Converts an object to an Integer by either parsing a string or 
     * class casting.
     * 
     * @param o
     *   the object to convert
     * @return the resulting integer
     */
    protected static Integer convertToInt(Object o)
    {
        if (o instanceof String)
        {
            return Integer.parseInt((String)o);
        }
        return (Integer)o;
    }
    
    /**
     * Evaluates and returns the result of the operation between the two expressions 
     * (e.g. returns the product of the two expressions if the joining operator was *)
     * 
     * If the BinOp is made up of only one expression, it returns the evaluation of that
     * expression.
     * 
     * @param env
     *  the environment in which to evaluate the expressions
     * @return the result of the operation if it consists of one or more expressions;
     *         the result of the evaluation of the single expression if the BinOp
     *         consists of only one expression
     * @throws ASTException
     *  if the operator was invalid, or one or more of the expressions did not evaluate
     *  to Integers
     */
    public Object eval(Environment env) throws ASTException
    {
        if (single)
        {
            return exp1.eval(env);
        }
        
        Object val1 = exp1.eval(env);
        Object val2 = exp2.eval(env);
        
        if (!(val1 instanceof Integer))
        {
            try 
            {
                val1 = convertToInt(val1);
            }
            catch(ClassCastException | NumberFormatException e)
            {
                throw new ASTException("Cannot use the operator " + op + " on a non-integer");
            }
        }
        if (!(val2 instanceof Integer))
        {
            try 
            {
                val2 = convertToInt(val2);
            }
            catch(ClassCastException | NumberFormatException e)
            {
                throw new ASTException("Cannot use the operator " + op + " on a non-integer");
            }
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
