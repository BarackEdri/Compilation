package AST;

import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_CFIELD_VAR extends AST_CFIELD
{
      	public AST_VAR_DEC var;
      
      	public AST_CFIELD_VAR(AST_VAR_DEC var)
      	{
            		/******************************/
            		/* SET A UNIQUE SERIAL NUMBER */
            		/******************************/
            		SerialNumber = AST_Node_Serial_Number.getFresh();
            
            		/***************************************/
            		/* PRINT CORRESPONDING DERIVATION RULE */
            		/***************************************/
                        System.out.print("cField -> varDec\n");
            
            		/*******************************/
            		/* COPY INPUT DATA MEMBERS ... */
            		/*******************************/
            		this.var = var;
      	}
      
      	public void PrintMe()
      	{
            		System.out.print("AST_CFIELD_VAR\n");

            		if (var != null) { var.PrintMe(); }
            		
            		/**********************************/
            		/* PRINT to AST GRAPHVIZ DOT file */
            		/**********************************/
            		AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("CFIELD_VAR"));
            		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, var.SerialNumber);
            }

    public TYPE SemantMe()
    {
		TYPE alreadyDefined = SYMBOL_TABLE.getInstance().global(var.id);
		TYPE scopeAlreadyDefined = SYMBOL_TABLE.getInstance().inScope(var.id);
		if (scopeAlreadyDefined != null && alreadyDefined != null ){
            throw new RuntimeException(String.format("%d", lineNum));
		}
        TYPE t = var.SemantMe();
        if (var instanceof AST_VAR_DEC_EXP)
        {
            AST_EXP exp = ((AST_VAR_DEC_EXP)var).exp;
            if (exp == null || exp instanceof AST_EXP_INT || exp instanceof AST_EXP_STRING || exp instanceof AST_EXP_NIL) 
			{ 
				return new TYPE_CLASS_VAR_DEC(t, var.id);
			 }
        }
        throw new RuntimeException(String.format("%d", lineNum));
    }
}
