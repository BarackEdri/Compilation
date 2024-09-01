import java.io.*;
import java.io.PrintWriter;
import java_cup.runtime.Symbol;
import AST.*;

public class Main
{
	static public void main(String argv[])
	{
		Lexer l;
		Parser p;
		Symbol s;
		AST_PROGRAM AST;
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
			
			try {
				/*******************************/
				/* [4] Initialize a new parser */
				/*******************************/
				p = new Parser(l);
	
				/***********************************/
				/* [5] 3 ... 2 ... 1 ... Parse !!! */
				/***********************************/
				AST = (AST_PROGRAM) p.parse().value;
	
				
				/*************************/
				/* [6] Print the AST ... */
				/*************************/
				AST.PrintMe(); // THIS LINE TO DO "//" IF WE DONT WANT TO PRINT THE TREE
				file_writer.print("OK");
			}
			catch (symErr symerr){
				file_writer.print("ERROR(");
				file_writer.print(l.getLine());
				file_writer.print(")");
				file_writer.close();
			}
			catch (lexerErr lexerr){
				file_writer.print("ERROR");
				file_writer.close();
			}
			
			/*************************/
			/* [7] Close output file */
			/*************************/
			file_writer.close();
			
			/*************************************/
			/* [8] Finalize AST GRAPHIZ DOT file */
			/*************************************/
			AST_GRAPHVIZ.getInstance().finalizeFile(); // THIS LINE TO DO "//" IF WE DONT WANT TO PRINT THE TREE
		}    
		catch (Exception e)
		{	
			e.printStackTrace();
		}
	}
}
class symErr extends RuntimeException 
{
	public symErr(String str){
		super(str);
	}
}

class lexerErr extends RuntimeException 
{
	public lexerErr(String str){
		super(str);
	}
}
