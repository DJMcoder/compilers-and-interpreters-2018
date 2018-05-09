package ast;
import java.util.HashSet;
import java.util.Set;

import environment.Environment;

/**
 * Writeln is a Statement which prints an expression; written as:
 * 
 * WRITELN(x);
 * 
 * where x is the expression to write
 * 
 * @author David Melisso
 * @version 3/7/18
 */
public class Writeln extends Statement
{
    private Expression expr;
    
    /**
     * Creates a Writeln object
     * @param exp
     *  the expression to write
     */
    public Writeln(Expression exp)
    {
        expr = exp;
    }
    
    /**
     * Prints the stored expression
     * 
     * @param env
     *  the environment in which to evaluate the expression
     *  
     * @throws ASTException
     *  if an exception occurs while evaluating the expression
     */
    public void exec(Environment env) throws ASTException
    {
        System.out.println(expr.eval(env));
    }
    
    /**
     * Prints whatever was on the stack
     * 
     * @param e 
     *  The interface for which to add code to the compiled file
     */
    public void compile(Emitter e)
    {
        Text text = (Text)expr;
        Expression ex;
        if (text.isNum())
        {
            ex = text.toNum();
        }
        else if (text.isBinOp())
        {
            ex = text.toBinOp();
        }
        else if (text.isVar())
        {
            ex = text.toVariable();
        }
        else if (text.isFunctionCall())
        {
            ex = text.toFunctionCall();
        }
        else 
        {
            throw new RuntimeException("Expression needs to be a number");
        }
            
        ex.compile(e);
        e.emit("move $a0 $v0");
        e.emit("li $v0 1");
        e.emit("syscall");
        
        // newline
        e.emit("li $v0 4");
        e.emit("la $a0 newline");
        e.emit("syscall");
    }
    
    /**
     * Gets a list of variables that are used within this block
     * @return
     *  an empty set
     */
    public Set<String> getUsedVariables()
    {
        return new HashSet<String>();
    }
}
