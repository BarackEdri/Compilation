/***************************/
/* FILE NAME: LEX_FILE.lex */
/***************************/

/*************/
/* USER CODE */
/*************/
import java_cup.runtime.*;

/******************************/
/* DOLAR DOLAR - DON'T TOUCH! */
/******************************/

%%

/************************************/
/* OPTIONS AND DECLARATIONS SECTION */
/************************************/
   
/*****************************************************/ 
/* Lexer is the name of the class JFlex will create. */
/* The code will be written to the file Lexer.java.  */
/*****************************************************/ 
%class Lexer

/********************************************************************/
/* The current line number can be accessed with the variable yyline */
/* and the current column number with the variable yycolumn.        */
/********************************************************************/
%line
%column

/*******************************************************************************/
/* Note that this has to be the EXACT same name of the class the CUP generates */
/*******************************************************************************/
%cupsym TokenNames

/******************************************************************/
/* CUP compatibility mode interfaces with a CUP generated parser. */
/******************************************************************/
%cup

/****************/
/* DECLARATIONS */
/****************/
/*****************************************************************************/   
/* Code between %{ and %}, both of which must be at the beginning of a line, */
/* will be copied verbatim (letter to letter) into the Lexer class code.     */
/* Here you declare member variables and functions that are used inside the  */
/* scanner actions.                                                          */  
/*****************************************************************************/   
%{
	/*********************************************************************************/
	/* Create a new java_cup.runtime.Symbol with information about the current token */
	/*********************************************************************************/
	private Symbol symbol(int type)               {return new Symbol(type, yyline, yycolumn);}
	private Symbol symbol(int type, Object value) {return new Symbol(type, yyline, yycolumn, value);}

	/*******************************************/
	/* Enable line number extraction from main */
	/*******************************************/
	public int getLine() { return yyline + 1; }
	public int getCharPos() { return yycolumn; }

	/****************************************/
	/* Functionc for check if we got number */
	/****************************************/
	public int intIsNumber(String numStr){
		if (numStr.length() > 5){return -1;}
		if(numStr.charAt(0)=='0'){
			if(numStr.length()>1){return -1;}
			return 0;
		}
		int num = new Integer(numStr);
		if (num >= 0 && num <= 32767){return num;}
		return -1;
	}
%}

/***********************/
/* MACRO DECALARATIONS */
/***********************/
LineTerminator		  = \r|\n|\r\n
WhiteSpace			  = {LineTerminator} | [ \t\f]
INTEGER				  = [0-9]+
NOT_INTEGER    		  = 0[0-9]+ | 3276[8-9] | [0-9][0-9][0-9][0-9][0-9][0-9]+
CHAR           	  	  = [A-Za-z]
ID					  = {CHAR} ({CHAR} | [0-9])*
STRING				  = \"{CHAR}*\"
CLASS       		  = class
CHARS 				  = \( | \) | \{ | \} | \[ | \] | \? | \! | \+ | \- | \. | \; | {CHAR} | [0-9]
CHARS_WITH_WHITESPACE = \( | \) | \{ | \} | \[ | \] | \? | \! | \+ | \- | \. | \; | {CHAR} | [0-9] | {WhiteSpace}
TIMES_CHARS 		  = \* ({CHARS_WITH_WHITESPACE}  | \* )* ({CHARS_WITH_WHITESPACE})+
FINISH 				  = [\*]+ \/
COMMENT_WHITSPACHE    = \/\* ({CHARS_WITH_WHITESPACE} | \/ | {TIMES_CHARS})* {FINISH}
COMMENT_NOWHITSPACHE  = \/\/ ({CHARS} | \* | \/ | [ \t\f])* {LineTerminator}
COMMENT 			  = {COMMENT_WHITSPACHE} | {COMMENT_NOWHITSPACHE}
ID_PLUS 			  = {CHAR} | [0-9]
BRACE 				  = "(" | ")" | "[" | "]" | "{" | "}"
SIGN 				  = "?" | "!" | "." | ";"
ADDSUB 				  = "+" | "-"
COMMENT1 			  = {WhiteSpace} | {ID_PLUS} | {BRACE} | {SIGN} | {ADDSUB} | "/"
COMMENT2 			  = {WhiteSpace} | {ID_PLUS} | {BRACE} | {SIGN} | {ADDSUB} | "*"
COMMENT3 			  = {ID_PLUS} | {BRACE} | {SIGN} | {ADDSUB} | "/" | "*" | [\t] | [ \t\f]
COMMENT4 			  = "//"{COMMENT3}* {LineTerminator}
COMMENT5 			  = "/*" ( {COMMENT1} | "*"{COMMENT2} )*  (("*")*)"*/"
ILLEGAL 			  = \" | \, | \= | \, | \> | \: | \<
ILLEGAL_COMMENT 	  = \/\/ ({CHARS} | \/ | \* | [ \t\f] | {ILLEGAL})* {ILLEGAL}+ ({CHARS} | \/ | \* | [ \t\f] | {ILLEGAL})* {LineTerminator}
ILLEGAL_COMMENT2 	  = \/\* ({CHARS_WITH_WHITESPACE} | \/ | {TIMES_CHARS})* {ILLEGAL}+ ({CHARS_WITH_WHITESPACE} | \/ | {TIMES_CHARS})* {FINISH}
ERROR 				  = \n|.

/******************************/
/* DOLAR DOLAR - DON'T TOUCH! */
/******************************/

%%

/************************************************************/
/* LEXER matches regular expressions to actions (Java code) */
/************************************************************/

/**************************************************************/
/* YYINITIAL is the state at which the lexer begins scanning. */
/* So these regular expressions will only be matched if the   */
/* scanner is in the start state YYINITIAL.                   */
/**************************************************************/

<YYINITIAL> {
"+"					{ return symbol(TokenNames.PLUS);}
"-"					{ return symbol(TokenNames.MINUS);}
{COMMENT4}    		{/* just skip what was found, do nothing */ }
{COMMENT5}      	{/* just skip what was found, do nothing */ }
"*"  				{ return symbol(TokenNames.TIMES);}
"/" 				{ return symbol(TokenNames.DIVIDE);}
"("					{ return symbol(TokenNames.LPAREN);}
")"					{ return symbol(TokenNames.RPAREN);}
"int"               {return symbol(TokenNames.TYPE_INT);}
"<"                 { return symbol(TokenNames.LT);}
">"                 { return symbol(TokenNames.GT);}
";"                 { return symbol(TokenNames.SEMICOLON);}
":="                { return symbol(TokenNames.ASSIGN);}
"="                 { return symbol(TokenNames.EQ);}
"."                 { return symbol(TokenNames.DOT);}
","                 { return symbol(TokenNames.COMMA);}
"}"                 { return symbol(TokenNames.RBRACE);}
"{"                 { return symbol(TokenNames.LBRACE);}
"]"                 { return symbol(TokenNames.RBRACK);}
"["                 { return symbol(TokenNames.LBRACK);}
"array"             { return symbol(TokenNames.ARRAY);}
"extends"           { return symbol(TokenNames.EXTENDS);}
"return"            { return symbol(TokenNames.RETURN);}
"while"             { return symbol(TokenNames.WHILE);}
"if"                { return symbol(TokenNames.IF);}
"new"               { return symbol(TokenNames.NEW);}
"nil"               { return symbol(TokenNames.NIL);}
"string"            { return symbol(TokenNames.TYPE_STRING);}
"class" 	        { return symbol(TokenNames.CLASS);}
"void"				{ return symbol(TokenNames.TYPE_VOID);}
{NOT_INTEGER}       { return symbol(TokenNames.ERROR);}
{INTEGER}			{ int num = intIsNumber(yytext());
					  if(num==-1){return symbol(TokenNames.ERROR);}
					  else{return symbol(TokenNames.INT,num);}}
{ID}				{ return symbol(TokenNames.ID,new String(yytext()));}
{STRING}			{ return symbol(TokenNames.STRING,new String(yytext()));}
{WhiteSpace}		{ /* just skip what was found, do nothing */ }
{ERROR}	    	    { return symbol(TokenNames.ERROR);}
<<EOF>>				{ return symbol(TokenNames.EOF);}
"/*"                { return symbol(TokenNames.ERROR);}
"//"                { return symbol(TokenNames.ERROR);}
.                   { return symbol(TokenNames.ERROR);}
}