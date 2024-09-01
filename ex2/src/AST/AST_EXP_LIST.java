package AST;

public class AST_EXP_LIST extends AST_Node
{
      	public AST_EXP head;
      	public AST_EXP_LIST tail;
      
      	public AST_EXP_LIST(AST_EXP head, AST_EXP_LIST tail)
      	{
            		/******************************/
            		/* SET A UNIQUE SERIAL NUMBER */
            		/******************************/
            		SerialNumber = AST_Node_Serial_Number.getFresh();
            
            		/***************************************/
            		/* PRINT CORRESPONDING DERIVATION RULE */
            		/***************************************/
            	  if (tail == null) { System.out.print("expList -> exp\n"); }
            		else { System.out.print("expList -> exp expList\n"); }
            
            		/*******************************/
            		/* COPY INPUT DATA MEMBERS ... */
            		/*******************************/
            		this.head = head;
                this.tail = tail;
      	}
      
      	public void PrintMe()
      	{
            		System.out.print("AST_EXP_LIST\n");
                
            		/**********************************/
            		/* RECURSIVELY PRINT AST_DEC_LIST */
            		/**********************************/
            		if (head != null) { head.PrintMe(); }
            		if (tail != null) { tail.PrintMe(); }
            		
            		/**********************************/
            		/* PRINT to AST GRAPHVIZ DOT file */
            		/**********************************/
            		AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("EXP_LIST"));
            		if (head != null) { AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, head.SerialNumber); }
            		if (tail != null) { AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, tail.SerialNumber); } 
            	}
}
