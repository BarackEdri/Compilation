package AST;

public class AST_EXP_NODE extends AST_EXP
{
      	public AST_EXP exp;
      
      	public AST_EXP_NODE(AST_EXP exp)
      	{
            		/******************************/
            		/* SET A UNIQUE SERIAL NUMBER */
            		/******************************/
            		SerialNumber = AST_Node_Serial_Number.getFresh();
            
            		/***************************************/
            		/* PRINT CORRESPONDING DERIVATION RULE */
            		/***************************************/
            		System.out.print("exp -> exp\n");
            
            		/*******************************/
            		/* COPY INPUT DATA NENBERS ... */
            		/*******************************/
            		this.exp = exp;
      	}
  
      	public void PrintMe()
      	{
            		System.out.print("AST_EXP_NODE\n");
            
            		/*****************************/
            		/* RECURSIVELY PRINT exp ... */
            		/*****************************/
            		if (exp != null) { exp.PrintMe(); }
            		
            		/*********************************/
            		/* Print to AST GRAPHIZ DOT file */
            		/*********************************/
            		AST_GRAPHVIZ.getInstance().logNode(SerialNumber, "EXP\n");
            
            		/****************************************/
            		/* PRINT Edges to AST GRAPHVIZ DOT file */
            		/****************************************/
            		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, exp.SerialNumber);
      	}
}
