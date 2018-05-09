package ast;

import java.util.List;

import parser.Parser;
import scanner.Scanner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Set;

/**
 * Program is the same as Block but with an added compile method which compiles the code
 * @author David Melisso
 * @version 4/24/18
 *
 */
public class Program extends Block
{

    /**
     * Creates a Program from a list of statements
     * @param s
     *  the list of statements
     */
    public Program(List<Statement> s)
    {
        super(s);
    }
    
    /**
     * Declares the variables (sets them to zero),
     * runs the program,
     * ends the program.
     * 
     * @param e
     *  The interface for which to add code to the compiled file
     */
    public void compile(Emitter e) 
    {
        e.emit(".data");
        e.emit("newline: .asciiz \"\\n\"");
        
        Set<String> vars = getUsedVariables();
        
        for (String var: vars)
        {
            e.addGlobalVariable(var);
        }
        
        e.emit(".text");
        e.emit(".globl main");
        e.emit("main:");
        
        for (Statement statement: stmts)
        {
            statement.compile(e);
        }
        
        e.emit("li $v0 10");
        e.emit("syscall # halt");
        
    }
    
    /**
     * Compiles the code given in HomemadeParserTest.txt
     * @param args
     *  not used
     */
    public static void main(String[] args) 
    {
        String filename =  "./HomemadeParserTest.txt";
        System.out.println("Beginning compilation of file " + filename);
        try
        {
            Parser p = (new Parser(
                    new Scanner(
                           new FileInputStream(
                                   new File(filename)))));
            
            Emitter e = new Emitter("./compiled.asm");
            List<ast.Statement> statements = new ArrayList<ast.Statement>();
            while (p.hasNext())
            {
                statements.add(p.parseStatement());
            }
            ast.Program program = new ast.Program(statements);
            
            
            program.compile(e);
            e.close();
            System.out.println("Compilation complete.");
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }

}
