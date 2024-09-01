package AST;

public class AST_PROGRAM extends AST_Node
{
	public AST_DEC_LIST declist;
	
	public AST_PROGRAM(AST_DEC_LIST declist)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("Program -> [declist]+\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.declist = declist;
	}
	
	public void PrintMe()
	{
		System.out.print("AST_PROGRAM\n");

		/*********************************/
		/* RECURSIVELY PRINT AST_PROGRAM */
		/*********************************/
		declist.PrintMe();
		
		/**********************************/
		/* PRINT to AST GRAPHVIZ DOT file */
		/**********************************/
		AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("PROGRAM"));
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,declist.SerialNumber);
	}
}
