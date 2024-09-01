package AST;

import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_DEC_LIST extends AST_Node
{
	public AST_DEC head;
	public AST_DEC_LIST tail;

	public AST_DEC_LIST(AST_DEC head, AST_DEC_LIST tail)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
	    	if (tail == null) { System.out.print("declist -> dec\n"); }
		else { System.out.print("declist -> dec declist\n"); }

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.head = head;
    		this.tail = tail;
	}

	public void PrintMe()
	{
		System.out.print("AST_DEC_LIST\n");
    
		/**********************************/
		/* RECURSIVELY PRINT AST_DEC_LIST */
		/**********************************/
		if (head != null) { head.PrintMe(); }
		if (tail != null) { tail.PrintMe(); }
		
		/**********************************/
		/* PRINT to AST GRAPHVIZ DOT file */
		/**********************************/
		AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("DEC_LIST"));
		if (head != null) { AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, head.SerialNumber); }
		if (tail != null) { AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, tail.SerialNumber); } 
	}
    
    public TYPE SemantMe()
    {
        head.SemantMe();
        if (tail != null) tail.SemantMe();
        return null;
    }
}
