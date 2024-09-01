package AST;

public class AST_CFIELD extends AST_Node
{
      	public AST_DEC d;
      
      	public AST_CFIELD(AST_DEC d)
      	{
            		/******************************/
            		/* SET A UNIQUE SERIAL NUMBER */
            		/******************************/
            		SerialNumber = AST_Node_Serial_Number.getFresh();
            
            		/***************************************/
            		/* PRINT CORRESPONDING DERIVATION RULE */
            		/***************************************/
                System.out.print("cField -> varDec | funcDec\n");
            
            		/*******************************/
            		/* COPY INPUT DATA MEMBERS ... */
            		/*******************************/
            		this.d = d;
      	}
      
      	public void PrintMe()
      	{
            		System.out.print("AST_CFIELD\n");

            		if (d != null) { d.PrintMe(); }
            		
            		/**********************************/
            		/* PRINT to AST GRAPHVIZ DOT file */
            		/**********************************/
            		AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("CFIELD"));
            		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, d.SerialNumber);
            	}
}
