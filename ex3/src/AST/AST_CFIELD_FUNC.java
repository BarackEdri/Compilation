package AST;

import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_CFIELD_FUNC extends AST_CFIELD
{
      	public AST_FUNC_DEC func;
      
      	public AST_CFIELD_FUNC(AST_FUNC_DEC func)
      	{
            		/******************************/
            		/* SET A UNIQUE SERIAL NUMBER */
            		/******************************/
            		SerialNumber = AST_Node_Serial_Number.getFresh();
            
            		/***************************************/
            		/* PRINT CORRESPONDING DERIVATION RULE */
            		/***************************************/
                        System.out.print("cField -> funcDec\n");
            
            		/*******************************/
            		/* COPY INPUT DATA MEMBERS ... */
            		/*******************************/
            		this.func = func;
      	}
      
      	public void PrintMe()
      	{
            		System.out.print("AST_CFIELD_FUNC\n");

            		if (func != null) { func.PrintMe(); }
            		
            		/**********************************/
            		/* PRINT to AST GRAPHVIZ DOT file */
            		/**********************************/
            		AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("CFIELD_FUNC"));
            		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, func.SerialNumber);
            }

    public TYPE SemantMe()
    {
        TYPE type = func.SemantMe();
        return new TYPE_CLASS_VAR_DEC(type, func.id);
    }
}
