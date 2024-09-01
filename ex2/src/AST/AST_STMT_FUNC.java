package AST;

public class AST_STMT_FUNC extends AST_STMT
{
        public AST_VAR var;
      	public String id;
      	public AST_EXP_LIST elist;
      
      	public AST_STMT_FUNC(AST_VAR var, String id, AST_EXP_LIST elist)
      	{
            		/******************************/
            		/* SET A UNIQUE SERIAL NUMBER */
            		/******************************/
            		SerialNumber = AST_Node_Serial_Number.getFresh();
            
            		/***************************************/
            		/* PRINT CORRESPONDING DERIVATION RULE */
            		/***************************************/
                if ((var == null) && (elist == null)) { System.out.printf("stmt -> %s ()\n", id); }
                if ((var == null) && (elist != null)) { System.out.printf("stmt -> %s (exps)\n", id); }
                if ((var != null) && (elist == null)) { System.out.printf("stmt -> var. %s ()\n", id); }
                if ((var != null) && (elist != null)) { System.out.printf("stmt -> var. %s (exps)\n", id); }
          
            		/*******************************/
            		/* COPY INPUT DATA MEMBERS ... */
            		/*******************************/
            		this.var = var;
            		this.id = id;
            		this.elist = elist;
      	}

      	public void PrintMe()
      	{
            		System.out.print("AST_STMT_FUNC\n");
          
            		if (var != null) var.PrintMe();
            		if (elist != null) elist.PrintMe();
            		
            		/***************************************/
            		/* PRINT Node to AST GRAPHVIZ DOT file */
            		/***************************************/
            		AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("STMT_FUNC(%s)",id));
            		
            		/****************************************/
            		/* PRINT Edges to AST GRAPHVIZ DOT file */
            		/****************************************/
            		if (var != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, var.SerialNumber);
            		if (elist != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, elist.SerialNumber);
      	}
}
