package parser;
import scanner.Scanner;
import ast.*;
import environment.Environment;
import scanner.ScanErrorException;
import java.io.*;
import java.util.Map;
import java.util.HashMap;
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
   private java.util.Scanner read;
   private String currentToken;
   private Map<String, Object> vars;
   
   /**
    * Constructs a Parser using a Scanner as input and sets the first
    * currentToken using the first token from said Scanner.
    * 
    * @param s
    *       the scanner to input from
    */
   public Parser(Scanner s) 
   {
       vars = new HashMap<String, Object>();
       read = new java.util.Scanner(System.in);
       scanner = s;
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
    * Parses an IF statement
    * 
    * @precondition current token begins an IF statement
    * @postcondition: all tokens in statement have been
    *                 eaten; current token is first one
    *                 after the IF statement
    */
   private void parseIf()
   {
       eat("IF");
       eat(currentToken);
       eat(currentToken);
       eat(currentToken);
       eat("THEN");
       eat(currentToken);
       eat(currentToken);
       eat(currentToken);
       eat(";");
   }
   
   /**
    * Scans the current token as an integer and returns it
    * 
    * @precondition current token is an integer
    * @postcondition number token has been eaten
    * @return the value of the parsed integer
    */
   private ast.Number parseNumber()
   {
       int res = Integer.parseInt(currentToken);
       eat(currentToken);
       return new ast.Number(res);
   }
   
   /**
    * Parses the statement:
    *   BEGIN ... END; where the program parses all the statements in between
    *   WRITELN(num); where the program prints the integer num
    *   READLN(var); where the program reads user input and sets it to 
    *       the variable var
    *   var := num; where the program sets the variable var to num
    * 
    * @precondition currentToken is WRITELN
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
       // WRITELN statement
       if (currentToken.equals("WRITELN"))
       {
           eat("WRITELN");
           eat("(");
           //System.out.println(parseStrings());
           Expression expr = parseExpr();
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
       // ensure identifier
       if (!Scanner.isLetter(currentToken.charAt((0))))
       {
           System.err.println("Error: invalid identifier " + currentToken);
           System.exit(1);
           return null;
       }
       // set variable
       String var = currentToken;
       eat(currentToken);
       eat(":=");
       Expression expr = parseExpr();
       eat(";");
       return new Assignment(var, expr);
   }
   
   /**
    * Parses one or more strings, connected by a comma
    * 
    * @return a single string, concatenating the multiple strings
    */
   public Text parseStrings()
   {
       String current = parseString();
       while (currentToken.charAt(0) == ',')
       {
           eat(",");
           current += parseString();
       }
       return new Text(current);
   }
   
   /**
    * Parses a single string by removing the surrounding quotes
    * 
    * @return the parsed string
    */
   public String parseString()
   {
       if (currentToken.charAt(0) == '\'')
       {
           String res = currentToken.substring(1, currentToken.length() - 1);
           eat(currentToken);
           return res;
       }
       return parseExpr().toString();
   }
   
   /*private boolean parseBool()
   {
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
       if (currentToken.equals("("))
       {
           eat("(");
           boolean bool = parseBool();
           eat(")");
           return bool;
       }
       // parse integer comparison
       if (Scanner.isDigit(currentToken.charAt(0)))
       {
           int expr1 = parseExpr();
           
           if (currentToken.equals(">"))
           {
               eat(">");
               return expr1 > parseExpr();
           }
           if (currentToken.equals("<"))
           {
               eat("<");
               return expr1 < parseExpr();
           }
           if (currentToken.equals(">="))
           {
               eat(">=");
               return expr1 >= parseExpr();
           }
           if (currentToken.equals("<="))
           {
               eat("<=");
               return expr1 <= parseExpr();
           }
           if (currentToken.equals("<>"))
           {
               eat("<>");
               return expr1 != parseExpr();
           }
           if (currentToken.equals("="))
           {
               eat("=");
               return expr1 == parseExpr();
           }
       }
   }
   */
   
   /**
    * Parses a factor, which can be:
    *   - factor        => returns -1 times the next factor
    *   ( expr )        => returns the expr within the ( )
    *   id              => returns the value of the variable
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
       // identifier
       else if (Scanner.isLetter(currentToken.charAt(0)))
       {
           Variable res = new Variable(currentToken);
           eat(currentToken);
           return res;
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
    *   TODO: string checking
    *   
    * @return the parsed expr
    */
   private Expression parseExpr()
   {
       Expression cur = parseTerm();
       if (cur instanceof Text)
       {
           return cur;
       }
       
       if (currentToken.equals("+") || currentToken.equals("-"))
       {
           String operator = currentToken;
           eat(currentToken);
           return new BinOp(cur, operator, parseExpr());
       }
       return cur;
   }
   
   /**
    * Converts an object to an Integer by either parsing a string or 
    * class casting.
    * 
    * @param o
    *   the object to convert
    * @return the resulting integer
    */
   private static Integer convertToInt(Object o)
   {
       if (o instanceof String)
       {
           return Integer.parseInt((String)o);
       }
       return (Integer)o;
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
       Object cur = parseTerm(); // TODO: AVOID LEFT RECURSION
       if (cur instanceof String)
       {
           try
           {
               cur = Integer.parseInt((String)cur);
           }
           catch(NumberFormatException e)
           {
               if (currentToken.equals("*") || 
               currentToken.equals("/") ||
               currentToken.equals("mod"))
               {
                   throw new Error("Cannot use operator " + currentToken +
                           " between a String and an Integer");
               }
               return new Text((String)cur);
           }
       }
       
       int current = ((Integer)cur).intValue();
       
       if (currentToken.equals("*") || 
               currentToken.equals("/") ||
               currentToken.equals("mod"))
       {
           String operator = currentToken;
           eat(operator);
           int value;
           try {
               value = convertToInt(parseFactor());
           }
           catch(NumberFormatException | ClassCastException e)
           {
               throw new Error("Cannot use operator " + operator +
                       " between an Integer and a String");
           }
           if (operator.equals("*"))
           {
               current *= value;
           }
           else if (operator.equals("/"))
           {
               current /= value;
           }
           else if (operator.equals("mod"))
           {
               current %= value;
           }
           else
           {
               throw new Error("Invalid operator " + operator);
           }
       }
       return Integer.valueOf(current);
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
           p.parseStatement();
       }
       catch (FileNotFoundException e)
       {
           e.printStackTrace();
       }
   }
}
