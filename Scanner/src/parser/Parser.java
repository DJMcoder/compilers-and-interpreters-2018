package parser;
import scanner.Scanner;
import scanner.ScanErrorException;
import java.io.*;
import java.util.Map;
import java.util.HashMap;

/**
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
   private int parseNumber()
   {
       int res = Integer.parseInt(currentToken);
       eat(currentToken);
       return res;
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
   public void parseStatement()
   {
       // BEGIN block
       if (currentToken.equals("BEGIN"))
       {
           eat("BEGIN");
           while (!currentToken.equals("END"))
           {
               parseStatement();
           }
           eat("END");
           eat(";");
           return;
       }
       // WRITELN statement
       if (currentToken.equals("WRITELN"))
       {
           eat("WRITELN");
           eat("(");
           System.out.println(parseStrings());
           eat(")");
           eat(";");
           return;
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
           vars.put(var, read.nextInt());
           return;
       }
       // ensure identifier
       if (!Scanner.isLetter(currentToken.charAt((0))))
       {
           System.out.println("Error: invalid identifier " + currentToken);
           System.exit(1);
           return;
       }
       // set variable
       String var = currentToken;
       eat(currentToken);
       eat(":=");
       vars.put(var, parseExpr());
       eat(";");
   }
   
   public String parseStrings()
   {
       String current = parseString();
       while (currentToken.charAt(0) == ',')
       {
           eat(",");
           current += parseString();
       }
       return current;
   }
   
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
   
   private Object parseFactor()
   {
       // - factor
       if (currentToken.equals("-"))
       {
           eat("-");
           return Integer.valueOf(-1 * ((Integer)parseFactor()).intValue());
       }
       // ( expr )
       else if (currentToken.equals("("))
       {
           eat("(");
           Object res = parseExpr();
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
           Object res = (Integer)vars.get(currentToken);
           eat(currentToken);
           return res;
       }
       // number
       else
       {
           return Integer.valueOf(parseNumber());
       }
   }
   
   /*private boolean parseBoolean()
   {
       
   }*/
   
   private Object parseExpr()
   {
       Object cur = parseTerm();
       if (cur instanceof String)
       {
           return cur;
       }
       int current = ((Integer)cur).intValue();
       while (currentToken.equals("+") || currentToken.equals("-"))
       {
           if (currentToken.equals("+"))
           {
               eat("+");
               current += ((Integer)parseTerm()).intValue();
           }
           else
           {
               eat("-");
               current -= ((Integer)parseTerm()).intValue();
           }
       }
       return current;
   }
   
   private Object parseTerm()
   {
       Object cur = parseFactor();
       if (cur instanceof String)
       {
           return cur;
       }
       int current = ((Integer)cur).intValue();
       while (currentToken.equals("*") || 
               currentToken.equals("/") ||
               currentToken.equals("mod"))
       {
           if (currentToken.equals("*"))
           {
               eat("*");
               current *= ((Integer)parseFactor()).intValue();
           }
           else if (currentToken.equals("/"))
           {
               eat("/");
               current /= ((Integer)parseFactor()).intValue();
           }
           else 
           {
               eat("mod");
               current %= ((Integer)parseFactor()).intValue();
           }
           
       }
       return Integer.valueOf(current);
   }
   
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
