package AST;

public class AST_EXP_BINOP extends AST_EXP
{
	public AST_EXP e1;
	public String operation;
	public AST_EXP e2;

	public AST_EXP_BINOP(AST_EXP e1, String operation, AST_EXP e2)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("exp -> exp BINOP exp\n");

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.e1 = e1;
		this.operation = operation;
		this.e2 = e2;
	}

	public void PrintMe()
	{
		System.out.print("AST_EXP_BINOP\n");

		/**************************************/
		/* RECURSIVELY PRINT e1, e2       ... */
		/**************************************/
		if (e1 != null) e1.PrintMe();
		if (e2 != null) e2.PrintMe();
		
		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("BINOP(%s)",operation));
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (e1 != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, e1.SerialNumber);
		if (e2 != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, e2.SerialNumber);
	}
}
