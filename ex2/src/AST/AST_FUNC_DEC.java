package AST;

public class AST_FUNC_DEC extends AST_DEC
{
	public AST_TYPE type;
	public String id;
	public AST_TYPEID_LIST tiList;
	public AST_STMT_LIST sList;

	public AST_FUNC_DEC(AST_TYPE type, String id, AST_TYPEID_LIST tiList, AST_STMT_LIST sList)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.format("funcDec -> TYPE ID(%s)\n", id);

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.type = type;
		this.id = id;
		this.tiList = tiList;
		this.sList = sList;
	}

	public void PrintMe()
	{
		System.out.print("AST_FUNC_DEC\n");

		/**********************************/
		/* RECURSIVELY PRINT AST_DEC_NODE */
		/**********************************/
		type.PrintMe();
		if (tiList != null) { tiList.PrintMe(); }
		sList.PrintMe();

		/**********************************/
		/* PRINT to AST GRAPHVIZ DOT file */
		/**********************************/
		AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("FUNC_DEC"));
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,type.SerialNumber);
		if (tiList != null) { AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,tiList.SerialNumber); }
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,sList.SerialNumber);
	}
}
