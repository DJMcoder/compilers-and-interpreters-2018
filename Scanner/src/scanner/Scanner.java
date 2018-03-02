package scanner;

import java.io.*;
import java.util.ArrayList;

/**
 * Scanner is a simple scanner for Compilers and Interpreters (2014-2015) lab
 * exercise 1. Usage: Create a new Scanner object with an input stream and use
 * Scanner.nextToken() to find each next token in the stream; use
 * Scanner.hasNext() to determine whether there exists another input.
 * 
 * @author David Melisso
 * @version January 28, 2018
 *
 */
public class Scanner
{
    private BufferedReader in;
    private char currentChar;
    private boolean eof;

    /**
     * Scanner constructor for construction of a scanner that uses an InputStream
     * object for input. Usage: FileInputStream inStream - new FileInputStream(new
     * File("file name"); Scanner lex = new Scanner(inStream);
     * 
     * @param inStream
     *            the input stream to use
     */
    public Scanner(InputStream inStream)
    {
        in = new BufferedReader(new InputStreamReader(inStream));
        eof = false;
        getNextChar();
    }

    /**
     * Scanner constructor for constructing a scanner that scans a given input
     * string. It sets the end-of-file flag an then reads the first character of the
     * input string into the instance field currentChar. Usage: Scanner lex = new
     * Scanner(input_string);
     * 
     * @param inString
     *            the string to scan
     */
    public Scanner(String inString)
    {
        in = new BufferedReader(new StringReader(inString));
        eof = false;
        getNextChar();
    }

    /**
     * Method: getNextChar Sets this.currentChar to the next character in the input
     * stream this.in
     */
    private void getNextChar()
    {
        try
        {
            int read = in.read();
            if (read == -1)
            {
                eof = true;
            } 
            else
            {
                currentChar = (char) read;
            }
        } 
        catch (Exception e)
        {
            e.printStackTrace();
            // System.out.println("[ERROR] " + e.toString());
            System.exit(1);
        }
    }
    
    /** 
     * Method: getNextCharOrError
     * If there is a next character, the scanner forwards the input by
     * one character. Otherwise, it throws an error.
     * 
     * @param error - the error message to throw
     * 
     * @throws ScanErrorException
     */
    private void getNextCharOrError(String error) throws ScanErrorException
    {
        if (hasNext())
        {
            getNextChar();
        }
        else
        {
            throw new ScanErrorException(error);
        }
    }

    /**
     * Method: eat
     * 
     * Compares the parameter to the current character, advances the input stream
     * one character if they are the same and throws an error otherwise.
     * 
     * @param expected
     *            - the character to compare to the current character
     * @throws ScanErrorException
     */
    private void eat(char expected) throws ScanErrorException
    {
        if (expected == currentChar)
        {
            getNextChar();
        } 
        else
        {
            throw new ScanErrorException(
                    "Unexpected character " + currentChar + 
                    " (expected character " + expected + ")");
        }
    }

    /**
     * Method: hasNext
     * 
     * Determines whether the input stream has another character
     * 
     * @return false if the input stream has ended; otherwise, true
     */
    public boolean hasNext()
    {
        return !eof;
    }

    /**
     * Method: isDigit
     * 
     * Determines whether a character is a digit
     * 
     * @param c
     *            - the character to examine
     * 
     * @return true if the character is a digit; otherwise, false
     */
    public static boolean isDigit(char c)
    {
        return c >= '0' && c <= '9';
    }

    /**
     * Method: isLetter
     * 
     * Determines whether a character is a letter
     * 
     * @param l
     *            - the character to examine
     * 
     * @return true if the character is a letter; otherwise, false
     */
    public static boolean isLetter(char l)
    {
        return (l >= 'a' && l <= 'z') || (l >= 'A' && l <= 'Z');
    }

    /**
     * Method: isOperand
     * 
     * Determines whether a character is an operand. An operand is defined as any of
     * the following characters: = + - * / % ( )
     * 
     * @param o
     *            - the character to examine
     * 
     * @return true if the character is an operand; otherwise, false
     */
    public static boolean isOperand(char o)
    {
        return o == '=' || 
               o == '+' || 
               o == '-' || 
               o == '*' || 
               o == '/' || 
               o == '%' || 
               o == '(' || 
               o == ')' ||
               o == ';' ||
               o == ':' ||
               o == '<' ||
               o == '>' ||
               o == '.';
    }

    /**
     * Method: isWhiteSpace
     * 
     * Determines whether a character is white space
     * 
     * @param w
     *            - the character to examine
     * 
     * @return true if the character is a whitespace; otherwise, false
     */
    public static boolean isWhiteSpace(char w)
    {
        return w == ' ' || w == '\t' || w == '\n' || w == '\r';
    }

    /**
     * Method: scanNumber
     * 
     * Reads the input stream looking for a number and returns that number. A number
     * is defined as a string of one or more digits.
     * 
     * @return the read number as a String
     * @throws ScanErrorException
     */
    private String scanNumber() throws ScanErrorException
    {
        String res = "";

        if (isDigit(currentChar))
        {
            res += currentChar;
            getNextChar();

            while (hasNext() && !isWhiteSpace(currentChar))
            {
                if (isDigit(currentChar))
                {
                    res += currentChar;
                    getNextChar();
                } 
                else if (isOperand(currentChar))
                {
                    return res;
                }
                else
                {
                    throw new ScanErrorException(
                            "Unexpected character '" + currentChar + 
                            "', expected a digit character");
                }
            }
        } 
        else
        {
            throw new ScanErrorException("Unexpected character '" + 
                    currentChar + "', expected a digit");
        }
        return res;
    }

    /**
     * Method: scanIdentifier
     * 
     * Reads the input stream looking for an identifier and returns that identifier.
     * An identifier is defined as a letter followed by any number of letters or
     * digits (interchangeably).
     * 
     * @return the read identifier as a String
     * @throws ScanErrorException
     */
    private String scanIdentifier() throws ScanErrorException
    {
        String res = "";

        if (isLetter(currentChar))
        {
            res += currentChar;
            getNextChar();

            while (hasNext() && !isWhiteSpace(currentChar))
            {
                if (isDigit(currentChar) || isLetter(currentChar))
                {
                    res += currentChar;
                    getNextChar();
                } 
                else if (isOperand(currentChar))
                {
                    return res;
                }
                else
                {
                    throw new ScanErrorException(
                            "Unexpected character '" + currentChar + 
                            "', expected a digit or letter");
                }
            }
        } 
        else
        {
            throw new ScanErrorException("Unexpected character '" + 
                    currentChar + "', expected a letter");
        }
        return res;
    }

    /**
     * Method: scanOperand
     * 
     * Reads the input stream looking for an operand and returns that operand. An
     * operand is defined any of the following characters: = + - * / % ( )
     * 
     * @return the read operand as a String
     * @throws ScanErrorException
     */
    private String scanOperand() throws ScanErrorException
    {
        String res = "";

        if (hasNext() && isOperand(currentChar))
        {
            res += currentChar;
            if (currentChar == '<' || 
                currentChar == '>' || 
                currentChar == '=' ||
                currentChar == ':')
            {
                getNextChar();
                if (currentChar == '=')
                {
                    res += currentChar;
                    getNextChar();
                }
                else if(isWhiteSpace(currentChar))
                {
                    return res;
                }
                else
                {
                    throw new ScanErrorException("Unexpected token '" + 
                            res + "', expected a valid operand");
                }
            }
            else {
                getNextChar();
            }
        } 
        else
        {
            throw new ScanErrorException("Unexpected character '" + 
                    currentChar + "', expected an operand");
        }
        return res;
    }
    
    private String scanString() throws ScanErrorException
    {
        String res = "\'";
        
        if (hasNext() && currentChar == '\'')
        {
            eat('\'');
            while (currentChar != '\'')
            {
                if (!hasNext())
                {
                    throw new ScanErrorException("String never closed");
                }
                res += currentChar;
                getNextChar();
            }
            eat('\'');
            return res + "\'";
        }
        throw new ScanErrorException("Unexpected character '" + 
                currentChar + "', expected \"\'\"");
    }

    /**
     * Method: nextToken
     * 
     * Skips leading whitespace then returns the next token.
     * 
     * @return the string with the next token
     */
    public String nextToken() throws ScanErrorException
    {
        while (hasNext() && isWhiteSpace(currentChar))
        {
            getNextChar();
        }
        if (currentChar == '\'')
        {
            return scanString();
        }
        if (isLetter(currentChar))
        {
            return scanIdentifier();
        } 
        else if (isDigit(currentChar))
        {
            return scanNumber();
        }
        if (!hasNext())
        {
            return null;
        }
        String op = scanOperand();
        if (op.charAt(0) == '/' && currentChar == '/')
        {
            while (currentChar != '\n')
            {
                getNextChar();
                
            }
            return nextToken();
        } 
        else if (op.charAt(0) == '(' && currentChar == '*')
        {
            getNextCharOrError("Multiline comment never closed.");
            char lastChar;

            int commentLevel = 1;
            while (commentLevel > 0)
            {
                lastChar = currentChar;
                getNextCharOrError("Multiline comment never closed.");
                //System.out.println("("+lastChar+currentChar+");" + commentLevel);
                if (lastChar == '*' && currentChar == ')')
                {
                    commentLevel--;
                    if (commentLevel > 0)
                    {
                        getNextCharOrError("Multiline comment never closed.");
                    }
                    else
                    {
                        getNextChar();
                        return nextToken();
                    }
                } 
                else if (lastChar == '(' && currentChar == '*')
                {
                    commentLevel++;
                    getNextCharOrError("Multiline comment never closed.");
                }
            }
            getNextChar();
            return nextToken();
        } 
        else
        {
            return op;
        }
    }

    /**
     * Method: main
     * 
     * Runs a test of the Scanner class, using an input provided by Mrs. Datar.
     * Prints all errors to the console, followed by an array of recognized tokens.
     * 
     * @param args
     *            - array of arguments
     */
    public static void main(String[] args)
    {
        String filename = "./ScannerTest.txt";
        try
        {
            FileInputStream inStream = new FileInputStream(new File(filename));
            //System.out.println("File '" + filename + "' successfully scanned.");
            Scanner lex = new Scanner(inStream);

            ArrayList<String> tokens = new ArrayList<String>();
            while (lex.hasNext())
            {
                try
                {
                    String token = lex.nextToken();
                    if (token == null)
                    {
                        return;
                    }
                    //System.out.println(token);
                    tokens.add(token);
                } 
                catch (ScanErrorException e)
                {
                    System.out.println("[ERROR] " + e.toString());
                    return;
                }
            }
        } 
        catch (FileNotFoundException e)
        {
            System.out.println("File '" + filename + "' not found.");
        }
    }
}
