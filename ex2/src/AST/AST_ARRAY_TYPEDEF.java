package AST;

public class AST_ARRAY_TYPEDEF extends AST_DEC
{
	public AST_TYPE type;
	public String id;

	public AST_ARRAY_TYPEDEF(AST_TYPE type, String id)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.format("arrayTypedef -> ARRAY ID(%s) = type [];\n", id);

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.type = type;
		this.id = id;
	}

	public void PrintMe()
	{
		System.out.print("AST_ARRAY_TYPEDEF\n");

		/**********************************/
		/* RECURSIVELY PRINT AST_DEC_NODE */
		/**********************************/
		type.PrintMe();

		/**********************************/
		/* PRINT to AST GRAPHVIZ DOT file */
		/**********************************/
		AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("ARRAY_TYPE_DEF"));
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,type.SerialNumber);
	}
}
