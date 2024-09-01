package AST;

public class AST_VAR_DEC extends AST_DEC
{
      	public AST_TYPE type;
        public String id;
        public AST_EXP exp;

      	public AST_VAR_DEC(AST_TYPE type, String id, AST_EXP exp)
      	{
      		/******************************/
      		/* SET A UNIQUE SERIAL NUMBER */
      		/******************************/
      		SerialNumber = AST_Node_Serial_Number.getFresh();
      
      		/***************************************/
      		/* PRINT CORRESPONDING DERIVATION RULE */
      		/***************************************/
          if (exp == null) { System.out.format("varDec -> TYPE ID(%s);\n", id); }
      		else     { System.out.format("varDec -> TYPE ID(%s) ::= EXP;\n", id); }
      
      		/*******************************/
      		/* COPY INPUT DATA MEMBERS ... */
      		/*******************************/
      		this.type = type;
          this.id = id;
          this.exp = exp;
      	}
      
      	public void PrintMe()
      	{
      		System.out.print("AST_VAR_DEC\n");
      
      		/**********************************/
      		/* RECURSIVELY PRINT AST_VAR_DEC */
      		/**********************************/
      		type.PrintMe();
                  if (exp != null) { exp.PrintMe(); }
      
      		/**********************************/
      		/* PRINT to AST GRAPHVIZ DOT file */
      		/**********************************/
      		AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("VAR_DEC"));
      		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,type.SerialNumber);
          if (exp != null) { AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,exp.SerialNumber); }
      	}
}
