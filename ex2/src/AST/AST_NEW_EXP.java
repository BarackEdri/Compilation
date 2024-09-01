package AST;

public class AST_NEW_EXP extends AST_EXP
{
      	public AST_TYPE t;
      	public AST_EXP e;
  
      	public AST_NEW_EXP(AST_TYPE t, AST_EXP e)
      	{
            		/******************************/
            		/* SET A UNIQUE SERIAL NUMBER */
            		/******************************/
            		SerialNumber = AST_Node_Serial_Number.getFresh();
            
            		/***************************************/
            		/* PRINT CORRESPONDING DERIVATION RULE */
            		/***************************************/
            		if (e == null) { System.out.format("newExp -> NEW type [exp]\n"); }
                        else { System.out.format("newExp -> NEW type\n"); }
                
            		/*******************************/
            		/* COPY INPUT DATA NENBERS ... */
            		/*******************************/
            		this.t = t;
                        this.e = e;
      	}
      
      	public void PrintMe()
      	{
            		/*******************************/
            		/* AST NODE TYPE = AST INT EXP */
            		/*******************************/
            		System.out.format("AST_NEW_EXP\n");
                if (t != null) t.PrintMe();
                if (e != null) e.PrintMe();
            
            		/*********************************/
            		/* Print to AST GRAPHIZ DOT file */
            		/*********************************/
            		AST_GRAPHVIZ.getInstance().logNode(SerialNumber,String.format("EXP_NEW"));
                if (t != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, t.SerialNumber);
                if (e != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, e.SerialNumber);
      	}
}
