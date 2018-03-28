package scanner;
/**
* This file defines a simple lexer for the compilers course 2017-2018
* 
* Automatically generates a Scanner which scans through a program using JFlex.
*
* @author David Melisso
* @version 3/28/18
*/
import java.io.*;


%%
/* lexical functions */
/* specify that the class will be called Scanner and the function to get the next
 * token is called nextToken.
 */
%class Scanner
%unicode
%line
%public
%function nextToken
/*  return String objects - the actual lexemes */
/*  returns the String "END: at end of file */
%type String
%eofval{
return "END";
%eofval}
/* use switch statement to encode DFA */
/*%switch*/

/**
 * Pattern definitions
 */
Operator = "=" | "+" | "-" | "*" | "\/" | "%" | "(" | ")" | ";"
LineTerminator = \r|\n|\r\n
WhiteSpace = {LineTerminator} | [ \t\f]




%%
/**
 * lexical rules
 */
 {Operator}			{return "OP: " + yytext(); }
 [a-z|A-Z][0-9|a-z|A-Z]*	{return "ID: " + yytext();}
 [0-9]+			{return "NUM:" + yytext();}
 {WhiteSpace}		{}
 . {}
