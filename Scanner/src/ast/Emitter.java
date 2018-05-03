package ast;
import java.io.*;

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

	/**
	 * creates an emitter for writing to a new file with given name
	 * @param outputFileName
	 *     the path of the file to output to
	 */
    public Emitter(String outputFileName)
    {
        labelcur = 1;
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
        this.emit("subu $sp $sp 4");
        this.emit("sw " + reg + " ($sp)");
    }
	
	/**
	 * writes code to get the value from the stack and put it in the given register
	 * @param reg
	 *     the register in which to store the value from the stack
	 */
    public void emitPop(String reg)
    {
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
}