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

	/**********************************************/
	/* Enable token position extraction from main */
	/**********************************************/
	public int getTokenStartPosition() { return yycolumn + 1; } 
%}

/***********************/
/* MACRO DECALARATIONS */
/***********************/
LineTerminator		= \r|\n|\r\n
WhiteSpace		= {LineTerminator} | [ \t]
INTEGER			= (0 | [1-9][0-9]*)
LETTER          = [a-zA-Z]
ID			= {LETTER}({LETTER}|{INTEGER})*
TBLE        = [(\)\{\}?!+\-\*/.;\[\]]
ALLOWED_COMMENT_CHAR 	= [a-zA-Z0-9 \t\r\n\(\)\{\}?!+\-\*/.;\[\]]
TypeTwoComment = {ALLOWED_COMMENT_CHAR}|{WhiteSpace}
ONE                 = ({LETTER}|{TBLE}|{INTEGER}|[ \t])
TypeOneComment        = "//"({ONE}*)
NonTypeOneComment     = "//".*
TypeTwoCommentBegin   = "/*"
TypeTwoCommentEnd     = "*/"
ERRORS                = (0{INTEGER})
STRING 			      = "\""{LETTER}*"\""
%state TYPE_TWO

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
{ERRORS}       { return symbol(TokenNames.ERROR);}
"+"				{ return symbol(TokenNames.PLUS);}
"-"				{ return symbol(TokenNames.MINUS);}
"*" 				{ return symbol(TokenNames.TIMES);}

"/"				{ return symbol(TokenNames.DIVIDE);}
"("				{ return symbol(TokenNames.LPAREN);}
")"				{ return symbol(TokenNames.RPAREN);}
"{"				{ return symbol(TokenNames.LBRACE);}
"}"				{ return symbol(TokenNames.RBRACE);}
"["				{ return symbol(TokenNames.LBRACK);}
"]"				{ return symbol(TokenNames.RBRACK);}

";"				{ return symbol(TokenNames.SEMICOLON);}
"."				{ return symbol(TokenNames.DOT);}
","				{ return symbol(TokenNames.COMMA);}
":="			{ return symbol(TokenNames.ASSIGN);}
"="				{ return symbol(TokenNames.EQ);}
"<"				{ return symbol(TokenNames.LT);}
">"				{ return symbol(TokenNames.GT);}

"class"             	{ return symbol(TokenNames.CLASS);}
"nil"             		{ return symbol(TokenNames.NIL);}
"array"           		{ return symbol(TokenNames.ARRAY);}
"while"           		{ return symbol(TokenNames.WHILE);}
"int"              		{ return symbol(TokenNames.TYPE_INT);}
"void"             		{ return symbol(TokenNames.TYPE_VOID);}
"extends"          		{ return symbol(TokenNames.EXTENDS);}
"return"           		{ return symbol(TokenNames.RETURN);}
"new"              		{ return symbol(TokenNames.NEW);}
"if"               		{ return symbol(TokenNames.IF);}
"string"          		{ return symbol(TokenNames.TYPE_STRING);}


{STRING}        		{ return symbol(TokenNames.STRING, yytext()); }
\"\t*\"       		        { return symbol(TokenNames.ERROR); }       

{INTEGER}  {
		                if (yytext().matches("0[0-9]+")) { return symbol(TokenNames.ERROR); } 
				else {
		                	try {
				                int value = Integer.parseInt(yytext());
		                        if (value < 0 || value > 32767) { return symbol(TokenNames.ERROR); } 
				                else { return symbol(TokenNames.INT, value); }
		                    } catch (NumberFormatException e) {
		                        return symbol(TokenNames.ERROR);
		                    }
		                  }
		                }


{ID}				{ return symbol(TokenNames.ID, new String(yytext()));}  


{WhiteSpace}			{  }




{TypeOneComment }    	{ }
{NonTypeOneComment}      {return symbol(TokenNames.ERROR);}
{TypeTwoCommentBegin} { yybegin(TYPE_TWO); }


<<EOF>>				{ return symbol(TokenNames.EOF);}
[^]				{ return symbol(TokenNames.ERROR);}
}

<TYPE_TWO> {
	{TypeTwoCommentEnd} { yybegin(YYINITIAL); }
	{TypeTwoComment} {  }
	<<EOF>>				{ return symbol(TokenNames.ERROR);}
	[^]				{ return symbol(TokenNames.ERROR);}
}
