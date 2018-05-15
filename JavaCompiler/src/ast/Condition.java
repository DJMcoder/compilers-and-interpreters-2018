package ast;

import environment.Environment;

/**
 * Condition is an expression which stores a comparison, including the left expression,
 * right expression, and comparison operator.
 * 
 * @version 3/16/18
 * @author David Melisso
 *
 */
public class Condition extends BinOp
{
    
    /**
     * Creates a Condition Object given the references to two expressions, in the correct
     * order, and the joining comparison operator.
     * @param exp1
     *  the first expression (the one on the left)
     * @param op
     *  the joining comparison operator
     * @param exp2
     *  the second expression
     */
    public Condition(Expression exp1, String op, Expression exp2)
    {
        super(exp1, op, exp2);
    }
    
    /**
     * Creates a Condition Object given a single expression.
     * @param exp
     *  the expression to create the Condition from
     */
    public Condition(Expression exp)
    {
        super(exp);
    }

    /**
     * If the Condition consists of two expressions, evaluates whether the operation is true.
     * If the Condition consists of only one expression, evaluates that expression.
     * 
     * An expression is true if
     *      It evaluates to a Boolean which is true
     *      It evaluates to an Integer which is not 0
     *      It evaluates to a String which is not empty
     * 
     * @param env
     *  The environment in which to parse
     * @return true if the operation is true; otherwise,
     *         false
     * @throws ASTException
     *  thrown if the comparison operator is invalid, or the component expressions
     *  are invalid types relative to each other (e.g. Boolean and Integer would not 
     *  cause an error, but Boolean and String would), or the BinOp consists of one
     *  expression which evaluates to an object which is not a Integer, Boolean, or 
     *  String
     */
    public Object eval(Environment env) throws ASTException
    {
        if (single)
        {
            Object res = exp1.eval(env);
            if (res instanceof Integer)
            {
                return ((Integer)res).intValue() != 0;
            }
            if (res instanceof Boolean)
            {
                return ((Boolean)res).booleanValue();
            }
            if (res instanceof String)
            {
                return !res.equals("");
            }
            throw new ASTException("Expression is not Integer, String, or Boolean for condition");
        }
        
        Object val1 = exp1.eval(env);
        Object val2 = exp2.eval(env);
        
        // if the comparison operator is equality
        if (op.equals("="))
        {
            // two integers
            if (val1 instanceof Integer && val2 instanceof Integer)
            {
                return ((Integer)val1).intValue() == ((Integer)val2).intValue();
            }
            // two strings
            if (val1 instanceof String && val2 instanceof String)
            {
                return ((String)val1).equals((String)val2);
            }
            // two booleans
            if (val1 instanceof Boolean && val2 instanceof Boolean)
            {
                return ((Boolean)val1).compareTo((Boolean)val2);
            }
            // integer and a string
            if (val1 instanceof Integer && val2 instanceof String)
            {
                try 
                {
                    return ((Integer)val1).intValue() == Integer.parseInt((String)val2);
                }
                catch(NumberFormatException e)
                {
                    throw new ASTException("Cannot use the operator " + op + " on a non-integer");
                }
            }
            // string and a integer
            if (val1 instanceof String && val2 instanceof Integer)
            {
                try 
                {
                    return ((Integer)val2).intValue() == Integer.parseInt((String)val1);
                }
                catch(NumberFormatException e)
                {
                    throw new ASTException("Cannot use the operator " + op + " on a non-integer");
                }
            }
            // boolean and a integer
            if (val1 instanceof Boolean && val2 instanceof Integer)
            {
                return (((Integer)val2).intValue() == 0 ^ ((Boolean)val1).booleanValue());
            }
            // integer and a boolean
            if (val1 instanceof Integer && val2 instanceof Boolean)
            {
                return (((Integer)val1).intValue() == 0 ^ ((Boolean)val2).booleanValue());
            }
        }
        
        // if the comparison operator is not an equality operator
        // force the expressions to integers
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
        
        if (op.equals(">"))
        {
            return num1 > num2;
        }
        if (op.equals("<"))
        {
            return num1 < num2;
        }
        if (op.equals("<>"))
        {
            return num1 != num2;
        }
        if (op.equals("<="))
        {
            return num1 <= num2;
        }
        if (op.equals(">="))
        {
            return num1 >= num2;
        }
        
        throw new ASTException("Invalid comparison operator " + op);
    }
    
    /**
     * Jumps to the given label if the condition is not true, 
     * otherwise continues.
     * 
     * @param e 
     *  The interface for which to add code to the compiled file
     * @param label
     *  the label to jump to
     */
    public void compile(Emitter e, String label)
    {
        exp1.compile(e);
        e.emitPush("$v0");
        exp2.compile(e);
        e.emitPop("$t0");
        
        if (op.equals("="))
        {
            e.emit("bne $t0 $v0 "+label);
        }
        else if (op.equals(">"))
        {
            e.emit("ble $t0 $v0 "+label);
        }
        else if (op.equals("<"))
        {
            e.emit("bge $t0 $v0 "+label);
        }
        else if (op.equals("<>"))
        {
            e.emit("beq $t0 $v0 "+label);
        }
        else if (op.equals("<="))
        {
            e.emit("bgt $t0 $v0 "+label);
        }
        else if (op.equals(">="))
        {
            e.emit("blt $t0 $v0 "+label);
        }
        else
        {
            throw new RuntimeException("Invalid comparison operator " + op);
        }
    }

}
