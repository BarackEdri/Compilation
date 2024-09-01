   
import java.io.*;

import java_cup.runtime.Symbol;
   
public class Main
{
	static public void main(String argv[])
	{
		Lexer l;
		Symbol s;
		FileReader file_reader;
		PrintWriter file_writer;
		String inputFilename = argv[0];
		String outputFilename = argv[1];
		
		try
		{
			/********************************/
			/* [1] Initialize a file reader */
			/********************************/
			file_reader = new FileReader(inputFilename);

			/********************************/
			/* [2] Initialize a file writer */
			/********************************/
			file_writer = new PrintWriter(outputFilename);
			
			/******************************/
			/* [3] Initialize a new lexer */
			/******************************/
			l = new Lexer(file_reader);

			/***********************/
			/* [4] Read next token */
			/***********************/
			s = l.next_token();

			/********************************/
			/* [5] Main reading tokens loop */
			/********************************/
			while (s.sym != TokenNames.EOF)
			{
				/************************/
				/* [6] Print to console */
				/************************/
				
				// Before changes :
				/* 
				System.out.print("[");
				System.out.print(l.getLine());
				System.out.print(",");
				System.out.print(l.getTokenStartPosition());
				System.out.print("]:");
				System.out.print(getTokenName(s.sym));
		                if (s.value != null) {
		                    System.out.print("(");
		                    System.out.print(s.value);
		                    System.out.print(")");
		                }
				System.out.print("\n");
				*/
				
				/*
				// According to the assigment (after changes) :
				System.out.print(getTokenName(s.sym));
		                if (s.value != null) {
		                    System.out.print("(");
		                    System.out.print(s.value);
		                    System.out.print(")");
		                }
				System.out.print("[");
				System.out.print(l.getLine());
				System.out.print(",");
				System.out.print(l.getTokenStartPosition());
				System.out.print("]");
				System.out.print("\n");
				*/
				/*********************/
				/* [7] Print to file */
				/*********************/
				// Before changes :
				/* 
				file_writer.print(l.getLine());
				file_writer.print(": ");
				file_writer.print(getTokenName(s.sym));
                		if (s.value != null) {
                   		   file_writer.print("(");
                    		   file_writer.print(s.value);
                  		   file_writer.print(")");
               	 		}
				file_writer.print("\n");
				

				// According to the assigment (after changes) :
				if (s.value != null) {
					file_writer.print(l.getLine());
					file_writer.print(": ");
                    	S		file_writer.print(s.value);
					file_writer.print("\n");
                		}
				*/
				if(getTokenName(s.sym).equals("ERROR")){
					file_writer = new PrintWriter(new FileOutputStream(outputFilename, false));
					file_writer.print("ERROR");
					break;
				}
				file_writer.print(getTokenName(s.sym));
		                if (s.value != null) {
		                    file_writer.print("(");
				    file_writer.print(s.value);
		                    file_writer.print(")");
		                }

						file_writer.print("[");
						file_writer.print(l.getLine());
						file_writer.print(",");
						file_writer.print(l.getTokenStartPosition());
						file_writer.print("]");
				file_writer.print("\n");
				
				/***********************/
				/* [8] Read next token */
				/***********************/
				s = l.next_token();
			}
			
			/******************************/
			/* [9] Close lexer input file */
			/******************************/
			l.yyclose();

			/**************************/
			/* [10] Close output file */
			/**************************/
			file_writer.close();
    	}
			     
		catch (Exception e)
		{
			e.printStackTrace();

		}
	}

	private static String getTokenName(int token) {
        switch (token) {
            case TokenNames.EOF: return "EOF";
            case TokenNames.PLUS: return "PLUS";
            case TokenNames.MINUS: return "MINUS";
            case TokenNames.TIMES: return "TIMES";
            case TokenNames.DIVIDE: return "DIVIDE";
            case TokenNames.LPAREN: return "LPAREN";
            case TokenNames.RPAREN: return "RPAREN";
            case TokenNames.LBRACK: return "LBRACK";
            case TokenNames.RBRACK: return "RBRACK";
            case TokenNames.LBRACE: return "LBRACE";
            case TokenNames.RBRACE: return "RBRACE";
            case TokenNames.SEMICOLON: return "SEMICOLON";
            case TokenNames.DOT: return "DOT";
            case TokenNames.COMMA: return "COMMA";
            case TokenNames.ASSIGN: return "ASSIGN";
            case TokenNames.EQ: return "EQ";
            case TokenNames.LT: return "LT";
            case TokenNames.GT: return "GT";
            case TokenNames.TYPE_INT: return "TYPE_INT";
            case TokenNames.TYPE_VOID: return "TYPE_VOID";
            case TokenNames.TYPE_STRING: return "TYPE_STRING";
            case TokenNames.CLASS: return "CLASS";
            case TokenNames.NIL: return "NIL";
            case TokenNames.ARRAY: return "ARRAY";
            case TokenNames.WHILE: return "WHILE";
            case TokenNames.IF: return "IF";
            case TokenNames.EXTENDS: return "EXTENDS";
            case TokenNames.RETURN: return "RETURN";
            case TokenNames.NEW: return "NEW";
            case TokenNames.NUMBER: return "NUMBER";
	    case TokenNames.INT: return "INT";
            case TokenNames.ID: return "ID";
            case TokenNames.STRING: return "STRING";
            case TokenNames.ERROR: return "ERROR";
            default: return "UNKNOWN";
        }
    }
}
