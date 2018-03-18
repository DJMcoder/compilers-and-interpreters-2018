package parser;
import scanner.Scanner;
import environment.Environment;
import ast.*;
import scanner.ScanErrorException;
import java.io.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Parser uses the Scanner to read a file and process a language.
 * 
 * @author David Melisso
 * @version February 28, 2018
 *
 */
public class Parser
{
    private Scanner scanner;
    private String currentToken;
    private Environment global;
    private java.util.Scanner read;
   
   /**
    * Constructs a Parser using a Scanner as input and sets the first
    * currentToken using the first token from said Scanner.
    * 
    * @param s
    *       the scanner to input from
    */
    public Parser(Scanner s) 
    {
        scanner = s;
        read = new java.util.Scanner(System.in);
        global = new Environment(read);
        try 
        {
            currentToken = scanner.nextToken();
        }
        catch (ScanErrorException e)
        {
            e.printStackTrace();
            System.exit(1);
        }
    }
   
   /**
    * Method: hasNext
    * 
    * Determines whether the scanner has another token
    * 
    * @return false if the input stream has ended; otherwise, true
    */
    public boolean hasNext()
    {
        return scanner.hasNext();
    }
   
   /**
    * Checks if currentToken matches the expected character;
    * if it does, moves the scanner to the next token,
    * otherwise, throws an IllegalArgumentException
    * 
    * @param expected
    *       the toke  to expect
    * @throws IllegalArgumentException
    *   if the expected token doesn't match the current token
    */
    private void eat(String expected) throws IllegalArgumentException
    {
        if (currentToken.equals(expected))
        {
            try
            {
                currentToken = scanner.nextToken();
            }
            catch (ScanErrorException e)
            {
                e.printStackTrace();
                System.exit(1);
            }
        }
        else
        {
            throw new IllegalArgumentException(
                   "Unexpected token " + currentToken + 
                   ", expected " + expected);
           
        }
    }
   
   /**
    * Scans the current token as an integer and returns it
    * 
    * @precondition current token is an integer
    * @postcondition number token has been eaten
    * @return the value of the parsed integer
    * @throws NumberFormatException
    *   if the token is not an integer
    */
    private ast.Number parseNumber() throws NumberFormatException
    {
        int res = Integer.parseInt(currentToken);
        eat(currentToken);
        return new ast.Number(res);
    }
    
    private List<Expression> eatParams()
    {
        List<Expression> params = new ArrayList<Expression>();
        eat("(");
        
        while (!currentToken.equals(")"))
        {
            params.add(parseExpr());
            
            while (currentToken.equals(","))
            {
                eat(",");
                params.add(parseExpr());
            }
        }
        
        eat(")");
        
        return params;
    }
   
   /**
    * Parses the statement:
    *   BEGIN ... END; where the program parses all the statements in between
    *   WRITELN(num); where the program prints the integer num
    *   READLN(var); where the program reads user input and sets it to 
    *       the variable var
    *   IF cond THEN stmt; where the program executes stmt if cond is true
    *   IF cond THEN stmt1 ELSE stmt2; where the program executes stmt1 if cond is true,
    *       and executes stmt2 if cond is false
    *   WHILE cond DO stmt; where the program executes stmt while cond is true
    *   FOR assignment TO number DO stmt; where the program increments the variable in 
    *       the assignment towards that number and executes stmt every interval
    *   BREAK; where the program exits the current loop
    *   CONTINUE; where the program skips the current iteration of the loop
    *   FUNCTION func(params...): stmt; where the program defins a function named func
    *       with parameters params which runs the statement stmt
    *   func(params...); which calls the function func with given parameters (therefore
    *       in a new environment)
    *   var := num; where the program sets the variable var to num
    * 
    * @precondition currentToken is WRITELN
    * @return the Statement of the correct type
    */
    public Statement parseStatement()
    {
        // BEGIN block
        if (currentToken.equals("BEGIN"))
        {
            List<Statement> stmts = new ArrayList<Statement>();
            eat("BEGIN");
            while (!currentToken.equals("END"))
            {
                stmts.add(parseStatement());
            }
            eat("END");
            eat(";");
            return new Block(stmts);
        }
        // IF statement
        if (currentToken.equals("IF"))
        {
            eat("IF");
            Condition cond = parseBool();
            eat("THEN");
            Statement stmt = parseStatement();
            if (currentToken.equals("ELSE"))
            {
                eat("ELSE");
                return new If(cond, stmt, parseStatement());
            }
            return new If(cond, stmt);
        }
        // WHILE loop statement
        if (currentToken.equals("WHILE"))
        {
            eat("WHILE");
            Condition cond = parseBool();
            eat("DO");
            return new While(cond, parseStatement());
        }
        // FOR loop statement
        if (currentToken.equals("FOR"))
        {
            eat("FOR");
            String var = currentToken;
            eat(currentToken);
            eat(":=");
            Expression expr = parseExpr();
            eat("TO");
            Expression limit = parseExpr();
            eat("DO");
            Statement stmt = parseStatement();
            
            Variable variable = new Variable(var);
            Assignment initialAssignment = new Assignment(var, expr);
            Expression increment = new BinOp(variable, "+", new ast.Number(1));
            Statement incrementAssignment = new Assignment(var, increment);
            
            // create loop sub-statement and increment
            List<Statement> loopComponents = new ArrayList<Statement>();
            loopComponents.add(stmt);
            loopComponents.add(incrementAssignment);
            Statement loopStatement = new Block(loopComponents);
            
            // create loop
            Condition loopCondition = new Condition(variable,"<=",limit);
            Statement loop = new While(loopCondition, loopStatement);
            
            // create loop and initial assignment
            List<Statement> assignmentAndLoop = new ArrayList<Statement>();
            assignmentAndLoop.add(initialAssignment);
            assignmentAndLoop.add(loop);
            
            return new Block(assignmentAndLoop);
        }
        // BREAK statement
        if (currentToken.equals("BREAK"))
        {
            eat("BREAK");
            eat(";");
            return new Break();
        }
        // CONTINUE statement
        if (currentToken.equals("CONTINUE"))
        {
            eat("CONTINUE");
            eat(";");
            return new Continue();
        }
        // RETURN statement
        if (currentToken.equals("RETURN"))
        {
            eat("RETURN");
            if (currentToken.equals(";"))
            {
                eat(";");
                return new Return();
            }
            Return res = new Return(parseExpr());
            eat(";");
            return res;
        }
        // WRITELN statement
        if (currentToken.equals("WRITELN"))
        {
            eat("WRITELN");
            eat("(");
            //System.out.println(parseStrings());
            Expression expr = parseStrings();//parseExpr();
            eat(")");
            eat(";");
            return new Writeln(expr);
        }
        // READLN statement
        if (currentToken.equals("READLN"))
        {
            eat("READLN");
            eat("(");
            String var = currentToken;
            eat(currentToken);
            eat(")");
            eat(";");
            return new Readln(var);
        }
        // FUNCTION statement
        if (currentToken.equals("FUNCTION"))
        {
            eat("FUNCTION");
            
            if (!Scanner.isLetter(currentToken.charAt((0))))
            {
                throw new Error("Invalid function identifier " + currentToken);
            }
            String funcName = currentToken;
            eat(funcName);
            
            List<String> params = new ArrayList<String>();
            eat("(");
            
            while (!currentToken.equals(")"))
            {
                params.add(currentToken);
                eat(currentToken);
                
                while (currentToken.equals(","))
                {
                    eat(",");
                    params.add(currentToken);
                    eat(currentToken);
                }
            }
            
            eat(")");
            
            
            eat(":");
            Statement s = parseStatement();
            return new FunctionAssignment(funcName, params, s);
        }
        // ensure identifier
        if (!Scanner.isLetter(currentToken.charAt((0))))
        {
            System.err.println("Error: invalid identifier " + currentToken);
            System.exit(1);
            return null;
        }
        // set variable or call function
        String var = currentToken;
        eat(currentToken);
        
        // set variable
        if (currentToken.equals(":="))
        {
            eat(":=");
            Expression expr = parseExpr();
            eat(";");
            return new Assignment(var, expr);
        }
        
        // call function
        if (currentToken.equals("("))
        {
            List<Expression> params = eatParams();
            FunctionCallStatement res = new FunctionCallStatement(var, params);
            eat(";");
            return res;
        }
        
        throw new IllegalArgumentException("Expected \":=\" or \"(\", read " + 
                            currentToken);
    }
   
   /**
    * Parses one or more strings, connected by a comma
    * 
    * @return a single string, concatenating the multiple strings
    */
    public Text parseStrings()
    {
        List<Text> current = new ArrayList<Text>();
        current.add(parseString());
        while (currentToken.charAt(0) == ',')
        {
            eat(",");
            current.add(parseString());
        }
        return new Text(current);
    }
   
   /**
    * Parses a single string by removing the surrounding quotes, or an expression
    * as a string.
    * 
    * @return the parsed text
    */
    public Text parseString()
    {
        if (currentToken.charAt(0) == '\'')
        {
            Text res = new Text(currentToken.substring(1, currentToken.length() - 1));
            eat(currentToken);
            return res;
        }
        return new Text(parseExpr());
    }
   
    /**
     * Parses a boolean (condition), which can be:
     * 
     * expr; where the expression is evaluated as a boolean
     * expr op expr; where the expression is compared to another expression with a
     *      comparison operator
     * 
     * @return the Condition which represents the boolean
     */
    private Condition parseBool()
    {
       /*
       if (currentToken.equals("TRUE"))
       {
           eat("TRUE");
           return true;
       }
       if (currentToken.equals("FALSE"))
       {
           eat("FALSE");
           return false;
       }
       if (currentToken.equals("NOT"))
       {
           eat("NOT");
           return !parseBool();
       }
       */
       
        Expression expr1 = parseExpr();
           
        if (currentToken.equals(">") ||
                currentToken.equals("<") ||
                currentToken.equals(">=") ||
                currentToken.equals("<=") ||
                currentToken.equals("<>") ||
                currentToken.equals("="))
           
        {
            String op = currentToken;
            eat(op);
            return new Condition(expr1, op, parseExpr());
        }
        return new Condition(expr1);
    }
   
   /**
    * Parses a factor, which can be:
    *   - factor        => returns -1 times the next factor
    *   ( expr )        => returns the expr within the ( )
    *   id              => returns the value of the variable
    *   func()          => returns the result of the function
    *   num             => returns the value of the number
    * where num is a number and id is an identifier.
    * 
    * @return the parsed factor
    */
    private Expression parseFactor()
    {
        // - factor
        if (currentToken.equals("-"))
        {
            eat("-");
            return new BinOp(new ast.Number(0), "-", parseFactor());
        }
        // ( expr )
        else if (currentToken.equals("("))
        {
            eat("(");
            Expression res = parseExpr();
            //System.out.println(res);
            eat(")");
            return res;
        }
       /*else if (currentToken.equals(","))
       {
           eat(",");
           return null;
       }*/
       // identifier (variable or function)
        else if (Scanner.isLetter(currentToken.charAt(0)))
        {
            String id = currentToken;
            eat(id);
            
            // function
            if (currentToken.equals("("))
            {
                return new FunctionCall(id, eatParams());
            }
            
            // variable
            return new Variable(id);
        }
       // number or string
        else
        {
            try
            {
                return parseNumber();
            }
            catch(NumberFormatException e)
            {
                return parseStrings();
            }
        }
    }
   
   /**
    * Parses an expr, which can be:
    *   expr (+ | -) term       => returns either the sum or difference
    *                              of the two values
    *   term                    => returns the value of the term
    *   string                  => returns the string
    *   
    *   
    * @return the parsed expr
    */
    private Expression parseExpr()
    {
        Expression cur = parseTerm();
        if (cur instanceof Text)
        {
            if (currentToken.equals("*") || 
                    currentToken.equals("/") ||
                    currentToken.equals("mod"))
            {
                try
                {
                    cur = ((Text) cur).toNum();
                }
                catch(NumberFormatException e)
                {
                    throw new Error("Cannot use operator " + currentToken +
                            " between a String and an Integer");
                }
            }
            return cur;
        }
       
        while (currentToken.equals("+") || currentToken.equals("-"))
        {
            String operator = currentToken;
            eat(currentToken);
            cur = new BinOp(cur, operator, parseTerm());
        }
        return cur;
    }
   
   /**
    * Parses an term, which can be:
    *   term (* | / | mod) factor   => returns the result of the
    *                                  evaluated term
    *   factor                      => returns the value of the factor
    *   string                      => returns the string
    *   
    * @return the parsed term
    */
    private Expression parseTerm()
    {
        Expression cur = parseFactor();
        if (cur instanceof Text)
        {
            if (currentToken.equals("*") || 
                    currentToken.equals("/") ||
                    currentToken.equals("mod"))
            {
                try
                {
                    cur = ((Text) cur).toNum();
                }
                catch(NumberFormatException e)
                {
                    throw new Error("Cannot use operator " + currentToken +
                            " between a String and an Integer");
                }
            }
            return cur;
        }
       
        while (currentToken.equals("*") || 
                currentToken.equals("/") ||
                currentToken.equals("mod"))
        {
            String operator = currentToken;
            eat(currentToken);
            cur = new BinOp(cur, operator, parseFactor());
        }
        return cur;
    }
    
    /**
     * Parses all the statements in the code in a given environment.
     * @param env
     *  the environment to execute in
     * @throws ASTException
     *  if an error occurs while parsing the statement
     */
    public void parseAndExecute(Environment env) throws ASTException
    {
        while (hasNext())
        {
            parseStatement().exec(env);
        }
    }
    
    /**
     * Parses all the statements in the code in the global environment.
     * @throws ASTException
     *  if an error occurs while parsing the statement
     */
    public void parseAndExecute() throws ASTException
    {
        while (hasNext())
        {
            parseStatement().exec(global);
        }
    }
   
   
   /**
    * Parses all the statements on file ./HomemadeParserTest.txt
    * 
    * @param args
    *   command line arguments
    */
    public static void main(String[] args)
    {
        String filename =  "./HomemadeParserTest.txt";
        try
        {
            Parser p = (new Parser(
                    new Scanner(
                           new FileInputStream(
                                   new File(filename)))));
           /*while(p.hasNext())
           {
               p.parseStatement();
           }*/
            p.parseAndExecute();
        }
        catch (FileNotFoundException | ASTException e)
        {
            e.printStackTrace();
        }
    }
}
