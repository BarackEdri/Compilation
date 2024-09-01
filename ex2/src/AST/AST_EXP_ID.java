package AST;

public class AST_EXP_ID extends AST_EXP
{
        public AST_VAR var;
      	public String id;
      	public AST_EXP_LIST elist;
      
      	public AST_EXP_ID(AST_VAR var, String id, AST_EXP_LIST elist)
      	{
            		/******************************/
            		/* SET A UNIQUE SERIAL NUMBER */
            		/******************************/
            		SerialNumber = AST_Node_Serial_Number.getFresh();
            
            		/***************************************/
            		/* PRINT CORRESPONDING DERIVATION RULE */
            		/***************************************/
                        if ((var == null) && (elist == null)) { System.out.printf("exp -> %s ()\n", id); }
                        if ((var != null) && (elist == null)) { System.out.printf("exp -> var. %s ()\n", id); }
                        if ((var == null) && (elist != null)) { System.out.printf("exp -> %s (exps)\n", id); }
                        if ((var != null) && (elist != null)) { System.out.printf("exp -> var. %s (exps)\n", id); }
          
            		/*******************************/
            		/* COPY INPUT DATA MEMBERS ... */
            		/*******************************/
            		this.var = var;
            		this.id = id;
            		this.elist = elist;
      	}

      	public void PrintMe()
      	{
            		System.out.print("AST_EXP_ID\n");
          
            		if (var != null) var.PrintMe();
            		if (elist != null) elist.PrintMe();
            		
            		/***************************************/
            		/* PRINT Node to AST GRAPHVIZ DOT file */
            		/***************************************/
            		AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("EXP_ID(%s)",id));
            		
            		/****************************************/
            		/* PRINT Edges to AST GRAPHVIZ DOT file */
            		/****************************************/
            		if (var != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, var.SerialNumber);
            		if (elist != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, elist.SerialNumber);
      	}
}
