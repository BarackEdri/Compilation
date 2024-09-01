package AST;

import SYMBOL_TABLE.*;
import TYPES.*;

public class AST_ARRAY_TYPEDEF extends AST_DEC
{
	public AST_TYPE type;
	public String id;

	public AST_ARRAY_TYPEDEF(AST_TYPE type, String id)
	{
		SerialNumber = AST_Node_Serial_Number.getFresh();
		System.out.format("arrayTypedef -> ARRAY ID(%s) = TYPE(%s)[];\n", id, type.type);

		this.type = type;
		this.id = id;
	}

	public void PrintMe()
	{
		System.out.print("AST_ARRAY_TYPEDEF\n");

		if (type != null) { type.PrintMe(); }

		AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("ARRAY_TYPE_DEF %s", id));
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,type.SerialNumber);
	}

	public TYPE SemantMe() 
	{	
        TYPE arrayType = type.SemantMe();
        if (arrayType == TYPE_VOID.getInstance())
        {
            throw new RuntimeException(String.format("%d", lineNum));
        }

        SYMBOL_TABLE.getInstance().enter(id, new TYPE_ARRAY(arrayType, id));

        return null;
	}
}
