package AST;

import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_TYPE extends AST_Node 
{
        public String type;
        public Boolean id;

        public AST_TYPE(String type, Boolean id) 
        {
                /******************************/
            	/* SET A UNIQUE SERIAL NUMBER */
            	/******************************/
                SerialNumber = AST_Node_Serial_Number.getFresh();

                /***************************************/
            	/* PRINT CORRESPONDING DERIVATION RULE */
            	/***************************************/
                if (id) { System.out.format("type -> ID(%s)\n", type); }
                else    { System.out.format("type -> %s\n", type);     }

                /*******************************/
            	/* COPY INPUT DATA MEMBERS ... */
            	/*******************************/
                this.type = type;
                this.id = id;
        }

        public void PrintMe() 
        {
		System.out.print("AST_TYPE\n");

        /**********************************/
  		/* PRINT to AST GRAPHVIZ DOT file */
  		/**********************************/
                if (id) { AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("ID(%s)", type)); }
                else    { AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("%s", type));     }
        }

	public TYPE SemantMe()
	{
		TYPE type2;
        if (type.equals("TYPE_INT")) { return TYPE_INT.getInstance(); }
        if (type.equals("TYPE_STRING")) { return TYPE_STRING.getInstance(); }
		if (type.equals("TYPE_VOID")) { return TYPE_VOID.getInstance(); }
        if (id)
            {
                type2 = SYMBOL_TABLE.getInstance().find(type);
                if (type2 != null && (type2.isClass() || type2.isArray()))
                    return type2;
            }
            throw new RuntimeException(String.format("%d", lineNum));
	}
}
