package ast;
import java.io.*;
import java.util.Stack;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;

/**
 * Emitter is a helper to write to the compiled file
 * 
 * @author Anu Datar
 * @author David Melisso
 *
 * @version 4/30/18
 */
public class Emitter
{
    private PrintWriter out;
    private int labelcur;
    private Stack<FunctionAssignment> procStack;
    private Stack<Integer> excessStackHeight;
    private List<String> globalVariables;
    private Stack<List<String>> varStack;

	/**
	 * creates an emitter for writing to a new file with given name
	 * @param outputFileName
	 *     the path of the file to output to
	 */
    public Emitter(String outputFileName)
    {
        labelcur = 1;
        procStack = new Stack<FunctionAssignment>();
        excessStackHeight = new Stack<Integer>();
        varStack = new Stack<List<String>>();
        globalVariables = new ArrayList<String>();
        try
        {
            out = new PrintWriter(new FileWriter(outputFileName), true);
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
    }

	/**
	 * prints one line of code to file (with non-labels indented)
	 * @param code
	 *     the code to print
	 */
    public void emit(String code)
    {
        if (!code.endsWith(":"))
            code = "\t" + code;
        out.println(code);
    }

	/**
	 * closes the file.  should be called after all calls to emit.
	 */
    public void close()
    {
        out.close();
    }
	
	/**
	 * writes code to push the value of the given register onto the stack
	 * @param reg
	 *     the register whose value to push to the stack
	 */
    public void emitPush(String reg)
    {
        if (excessStackHeight.size() != 0)
        {
            excessStackHeight.push(excessStackHeight.pop() + 1);
        }
        this.emit("subu $sp $sp 4");
        this.emit("sw " + reg + " ($sp)");
    }
    
    /**
     * writes code pop the stack, moving the stack pointer downwards
     */
    public void emitPop()
    {
        if (excessStackHeight.size() != 0)
        {
            excessStackHeight.push(excessStackHeight.pop() - 1);
        }
        this.emit("addu $sp $sp 4");
    }
	
	/**
	 * writes code to get the value from the stack and put it in the given register
	 * @param reg
	 *     the register in which to store the value from the stack
	 */
    public void emitPop(String reg)
    {
        if (excessStackHeight.size() != 0)
        {
            excessStackHeight.push(excessStackHeight.pop() - 1);
        }
        this.emit("lw " + reg + " ($sp)");
        this.emit("addu $sp $sp 4");
    }
	
	/**
	 * gets the sequential ID of the labels
	 * @return the next id to use
	 */
    public int getNextLabel()
    {
        return labelcur++;
    }
    
    /**
     * Adds a local variable to the list of local variables,
     * and emits code to make space in the stack for it
     * @param varName
     *  the name of the variable to add
     */
    private void addLocalVariable(String varName)
    {
        emit("subu $sp $sp 4");
        varStack.peek().add(varName);
    }
    
    /**
     * remember proc as current procedure context
     * @param proc
     *  the procedure to set as the current procedure
     */
    public void setProcedureContext(FunctionAssignment proc)
    {
        excessStackHeight.push(0);
        procStack.push(proc);
        varStack.push(new ArrayList<String>(proc.getFunction().getParams()));
        
        Set<String> vars = proc.getFunction().getStatement().getUsedVariables();
        for (String var: vars)
        {
            if (!isGlobalVariable(var))
            {
                addLocalVariable(var);
            }
        }
    }
    
    /**
     * clear current procedure context
     */
    public void clearProcedureContext()
    {
        excessStackHeight.pop();
        procStack.pop();
        varStack.pop();
    }
    
    /**
     * Gets the current procedure context
     * @return the procedure at the top of the procedure stack
     */
    public FunctionAssignment getProcedureContext()
    {
        if (procStack.size() == 0)
        {
            return null;
        }
        return procStack.peek();
    }
    
    /**
     * Determines whether a variable is defined in the current procedure
     * @param varName
     *  the name of the variable to check
     * @return true if the list of local variables includes the given variable;
     *         false otherwise
     */
    public boolean isLocalVariable(String varName)
    {
        return varStack.peek().contains(varName);
    }
    
    /**
     * Adds a global variable to the list of global variables,
     * and emits code to define the variable globally.
     * @param varName
     *  the name of the variable to add
     */
    public void addGlobalVariable(String varName)
    {
        this.emit("var"+varName+": .word 0");
        this.globalVariables.add(varName);
    }
    
    /**
     * Determines whether a variable is defined globally.
     * @param varName
     *  the name of the variable to check
     * @return true if the list of global variables includes the given variable;
     *         false otherwise
     */
    public boolean isGlobalVariable(String varName)
    {
        return globalVariables.contains(varName);
    }
    
    /**
     * Gets the offset from the stack pointer of the memory address of a local variable
     * 
     * @precondition: localVarName is the name of a local
     *                variable for the procedure currently
     *                being compiled
     * @param localVarName
     *  the name of the variable to look up
     * @return the offset for the given variable
     */
    public int getOffset(String localVarName)
    {
        List<String> localvars = varStack.peek();
        int excessStack;
        if (excessStackHeight.size() == 0)
        {
            excessStack = 0;
        }
        else
        {
            excessStack = excessStackHeight.peek();
        }
        return 4*(localvars.size() - 1 - localvars.indexOf(localVarName) + excessStack);
    }
}