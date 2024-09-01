package AST;

import TYPES.*;
import SYMBOL_TABLE.*;

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
    
        public TYPE SemantMe()
        {
            TYPE returnType = SYMBOL_TABLE.getInstance().funcReturnType;

            if (returnType == null)
                throw new RuntimeException(String.format("%d", lineNum));
            if (ret == null && returnType != TYPE_VOID.getInstance())
                throw new RuntimeException(String.format("%d", lineNum));
            if (ret != null && returnType == TYPE_VOID.getInstance())
                throw new RuntimeException(String.format("%d", lineNum));
            if (ret != null && returnType != TYPE_VOID.getInstance())
            {
                TYPE expType = ret.SemantMe();
				if (expType!=null){

                if ((!expType.isClass() || !returnType.isClass()) && returnType != expType)
                    throw new RuntimeException(String.format("%d", lineNum));
                if ((expType.isClass() && returnType.isClass())
                    && !((TYPE_CLASS)expType).isSubClassOf(((TYPE_CLASS)returnType)))
                    throw new RuntimeException(String.format("%d", lineNum));
            }
		}
            return null;
        }
}
