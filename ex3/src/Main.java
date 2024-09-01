   
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

        boolean debug = false;
		
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
			
			/*******************************/
			/* [4] Initialize a new parser */
			/*******************************/
			p = new Parser(l,file_writer);

			/***********************************/
			/* [5] 3 ... 2 ... 1 ... Parse !!! */
			/***********************************/
			AST = (AST_PROGRAM) p.parse().value;
			
			/*************************/
			/* [6] Print the AST ... */
			/*************************/
			if (debug)
                AST.PrintMe();

			/**************************/
			/* [7] Semant the AST ... */
			/**************************/
            try
            {
			    AST.SemantMe();
                file_writer.print("OK\n");
            }

			catch (RuntimeException e)
            {
                file_writer.print("ERROR(" + e.getMessage() + ")\n");
                System.out.println("ERROR(" + e.getMessage() + ")");
                e.printStackTrace();
            }
		
			/*************************/
			/* [8] Close output file */
			/*************************/
			file_writer.close();

			/*************************************/
			/* [9] Finalize AST GRAPHIZ DOT file */
			/*************************************/
			if (debug)
                AST_GRAPHVIZ.getInstance().finalizeFile();
    	}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}




