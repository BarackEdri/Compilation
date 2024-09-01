package AST;

public class AST_STMT_RETURN extends AST_STMT
{
      	public AST_EXP ret;
      
      	public AST_STMT_RETURN(AST_EXP ret)
      	{
            		/******************************/
            		/* SET A UNIQUE SERIAL NUMBER */
            		/******************************/
            		SerialNumber = AST_Node_Serial_Number.getFresh();
            
            		/***************************************/
            		/* PRINT CORRESPONDING DERIVATION RULE */
            		/***************************************/
              	if (ret == null) { System.out.print("stmt -> RETURN\n"); }
                else { System.out.print("stmt -> RETURN exp\n"); }
            
            		/*******************************/
            		/* COPY INPUT DATA NENBERS ... */
            		/*******************************/
            		this.ret = ret;
      	}
      
      	public void PrintMe()
      	{
      		System.out.print("AST_STMT_RETURN\n");
      
      		if (ret != null) ret.PrintMe();

      		/***************************************/
      		/* PRINT Node to AST GRAPHVIZ DOT file */
      		/***************************************/
      		AST_GRAPHVIZ.getInstance().logNode(SerialNumber, "STMT_RETURN");
      		
      		/****************************************/
      		/* PRINT Edges to AST GRAPHVIZ DOT file */
      		/****************************************/
      		if (ret != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, ret.SerialNumber);
      	}
}
