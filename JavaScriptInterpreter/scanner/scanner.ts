import fs from "fs";
import ScanErrorException from "./scanErrorException";

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
export default class Scanner
{
    fullString: string;
    in: string;
    eof: boolean;
    readingComment: boolean;
    currentChar: string;

    /**
     * Scanner constructor for construction of a scanner that uses an InputStream
     * object for input. Usage: FileInputStream inStream - new FileInputStream(new
     * File("file name"); Scanner lex = new Scanner(inStream);
     *
     * @param inStream
     *            the input stream to use
     */
    constructor(inStream: string)
    {
        this.fullString = fs.readFileSync(inStream, { encoding: 'utf8' });
        this.in = this.fullString;
        this.eof = false;
        this.readingComment = false;
        this.currentChar = "";
        this.getNextChar();
    }

    /**
     * Method: getNextChar Sets this.currentChar to the next character in the input
     * stream this.in
     */
    getNextChar()
    {
      this.currentChar = "";
      if (this.in.length <= 0)
      {
        this.eof = true;
      }
      else {
        this.currentChar = this.in.charAt(0);
        this.in = this.in.substr(1);
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
     *  if there is no next char, throw a ScanErrorException with the given reason
     */
    getNextCharOrError(error: string)
    {
        if (this.hasNext())
        {
            this.getNextChar();
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
     *  if the expected character does not match the current character
     */
    eat(expected: string)
    {
        if (expected == this.currentChar)
        {
            this.getNextChar();
        }
        else
        {
            throw new ScanErrorException(
                    "Unexpected character " + this.currentChar +
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
    hasNext()
    {
        return !this.eof;
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
    static isDigit(c: string)
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
    static isLetter(l: string)
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
    static isOperand(o: string)
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
               o == ',' ||
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
    static isWhiteSpace(w: string)
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
     *  if the first character is not a digit, or if the digits are directly followed
     *  by letters
     */
    scanNumber()
    {
        var res = "";

        if (Scanner.isDigit(this.currentChar))
        {
            res += this.currentChar;
            this.getNextChar();

            while (this.hasNext() && !Scanner.isWhiteSpace(this.currentChar))
            {
                if (Scanner.isDigit(this.currentChar))
                {
                    res += this.currentChar;
                    this.getNextChar();
                }
                else if (Scanner.isOperand(this.currentChar))
                {
                    return res;
                }
                else
                {
                    throw new ScanErrorException(
                            "Unexpected character '" + this.currentChar +
                            "', expected a digit character");
                }
            }
        }
        else
        {
            throw new ScanErrorException("Unexpected character '" +
                    this.currentChar + "', expected a digit");
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
     *  if the first character is not a letter, or if it is followed by an invalid
     *  character
     */
    scanIdentifier()
    {
        var res = "";

        if (Scanner.isLetter(this.currentChar))
        {
            res += this.currentChar;
            this.getNextChar();

            while (this.hasNext() && !Scanner.isWhiteSpace(this.currentChar))
            {
                if (Scanner.isDigit(this.currentChar) || Scanner.isLetter(this.currentChar))
                {
                    res += this.currentChar;
                    this.getNextChar();
                }
                else if (Scanner.isOperand(this.currentChar))
                {
                    return res;
                }
                else
                {
                    throw new ScanErrorException(
                            "Unexpected character '" + this.currentChar +
                            "', expected a digit or letter");
                }
            }
        }
        else
        {
            throw new ScanErrorException("Unexpected character '" +
                    this.currentChar + "', expected a letter");
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
     *  if the first character is not an operator, or if it is directly followed
     *  by an operator
     */
    scanOperand()
    {
        var res = "";

        if (this.hasNext() && Scanner.isOperand(this.currentChar))
        {
            res += this.currentChar;
            if (this.currentChar == '<' ||
                    this.currentChar == '>' ||
                    this.currentChar == '=' ||
                    this.currentChar == ':')
            {
                this.getNextChar();
                if (this.currentChar == '=')
                {
                    res += this.currentChar;
                    this.getNextChar();
                }
                else if (res == "<" && this.currentChar == '>')
                {
                    res += this.currentChar;
                    this.getNextChar();
                }
                else if(!Scanner.isOperand(this.currentChar))
                {
                    return res;
                }
                else
                {
                    throw new ScanErrorException("Unexpected token '" +
                            res + "', expected a valid operand");
                }
            }
            else
            {
                this.getNextChar();
            }
        }
        else
        {
            throw new ScanErrorException("Unexpected character '" +
                    this.currentChar + "', expected an operand");
        }
        return res;
    }

    /**
     * Scans a string, in single quotes, and returns it.
     *
     * @return the string, including the quotes
     * @throws ScanErrorException
     *  if the characters do not match the format of a string
     */
    scanString(): string
    {
        var res = "\'";

        if (this.hasNext() && this.currentChar == '\'')
        {
            this.readingComment = true;
            this.eat('\'');
            while (this.currentChar != '\'')
            {
                if (!this.hasNext())
                {
                    throw new ScanErrorException("String never closed");
                }
                res += this.currentChar;
                this.getNextChar();
            }
            this.eat('\'');
            this.readingComment = false;
            return res + "\'";
        }
        throw new ScanErrorException("Unexpected character '" +
                this.currentChar + "', expected \"\'\"");
    }

    /**
     * Method: nextToken
     *
     * Skips leading whitespace then returns the next token.
     *
     * @return the string with the next token
     * @throws ScanErrorException
     *  if the token is invalid
     */
    nextToken(): string
    {
        while (this.hasNext() && Scanner.isWhiteSpace(this.currentChar))
        {
            this.getNextChar();
        }

        if (this.currentChar == '\'')
        {
            return this.scanString();
        }

        if (Scanner.isLetter(this.currentChar))
        {
            return this.scanIdentifier();
        }
        else if (Scanner.isDigit(this.currentChar))
        {
            return this.scanNumber();
        }

        if (!this.hasNext())
        {
            return "";
        }
        var op = this.scanOperand();
        if (op.charAt(0) == '/' && this.currentChar == '/')
        {
            while (this.currentChar != "\n" && this.hasNext())
            {
                this.currentChar = "";
                this.getNextChar();
            }
            if (!this.hasNext())
            {
                return "";
            }
            return this.nextToken();
        }
        else if (op.charAt(0) == '(' && this.currentChar == '*')
        {
            this.readingComment = true;
            this.getNextCharOrError("Multiline comment never closed.");
            var lastChar;

            var commentLevel = 1;
            while (commentLevel > 0)
            {
                lastChar = this.currentChar;
                this.currentChar = "";
                this.getNextCharOrError("Multiline comment never closed.");
                if (lastChar == '*' && this.currentChar == ')')
                {
                    commentLevel--;
                    if (commentLevel > 0)
                    {
                        this.getNextCharOrError("Multiline comment never closed.");
                    }
                    else
                    {
                        this.getNextChar();
                        this.readingComment = false;
                        return this.nextToken();
                    }
                }
                else if (lastChar == '(' && this.currentChar == '*')
                {
                    commentLevel++;
                    this.getNextCharOrError("Multiline comment never closed.");
                }
            }
            this.getNextChar();
            this.readingComment = false;
            return this.nextToken();
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
     */
    static async test()
    {
        var filename = __dirname + "/ScannerTest.txt";
        try
        {
            var lex = new Scanner(filename);

            var tokens = [];
            while (lex.hasNext())
            {
                try
                {
                    var token = lex.nextToken();
                    if (token == null)
                    {
                        return;
                    }
                    console.log(token);
                    tokens.push(token);
                }
                catch (e)
                {
                    console.log("[ERROR] " + e.toString());
                    return;
                }
            }
        }
        catch (e)
        {
            console.log(e);
            process.exit(1);
        }
    }
}

// Main method
if (require.main === module)
{
  Scanner.test();
}
