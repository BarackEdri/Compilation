package AST;

public class AST_BINOP extends AST_EXP
{
      	public String operation;
      
      	public AST_BINOP(String operation)
      	{
            		/******************************/
            		/* SET A UNIQUE SERIAL NUMBER */
            		/******************************/
            		SerialNumber = AST_Node_Serial_Number.getFresh();
            
            		/***************************************/
            		/* PRINT CORRESPONDING DERIVATION RULE */
            		/***************************************/
            		System.out.print("binop -> PLUS|MINUS|TIMES|DIVIDE|LT|GT|EQ\n");

            		/*******************************/
            		/* COPY INPUT DATA MEMBERS ... */
            		/*******************************/
            		this.operation = operation;
      	}

      	public void PrintMe()
      	{
            		System.out.print("AST_BINOP\n");
                        		
            		/***************************************/
            		/* PRINT Node to AST GRAPHVIZ DOT file */
            		/***************************************/
            		AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("BINOP(%s)",operation));
      	}
}
