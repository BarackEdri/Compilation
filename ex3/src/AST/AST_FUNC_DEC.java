package AST;

import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_FUNC_DEC extends AST_DEC
{
	public AST_TYPE type;
	public String id;
	public AST_ID_LIST tiList;
	public AST_STMT_LIST sList;

	public AST_FUNC_DEC(AST_TYPE type, String id, AST_ID_LIST tiList, AST_STMT_LIST sList)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.format("funcDec -> TYPE ID(%s)\n", id);

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.type = type;
		this.id = id;
		this.tiList = tiList;
		this.sList = sList;
	}

	public void PrintMe()
	{
		System.out.print("AST_FUNC_DEC\n");

		/**********************************/
		/* RECURSIVELY PRINT AST_DEC_NODE */
		/**********************************/
		type.PrintMe();
		if (tiList != null) { tiList.PrintMe(); }
		sList.PrintMe();

		/**********************************/
		/* PRINT to AST GRAPHVIZ DOT file */
		/**********************************/
		AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("FUNC_DEC"));
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,type.SerialNumber);
		if (tiList != null) { AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,tiList.SerialNumber); }
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,sList.SerialNumber);
	}
    
    public TYPE SemantMe()
    {
        TYPE t;
        TYPE returnType = null;
        returnType = type.SemantMe();
        TYPE alreadyDefined  = SYMBOL_TABLE.getInstance().inScope(id);
        
        if (alreadyDefined  != null)
            throw new RuntimeException(String.format("%d", type.lineNum));
        
        alreadyDefined  = SYMBOL_TABLE.getInstance().global(id);

        TYPE_FUNCTION thisType = new TYPE_FUNCTION(returnType, id, null);
        SYMBOL_TABLE.getInstance().enter(id, thisType);

        SYMBOL_TABLE.getInstance().funcReturnType = returnType;
    
        SYMBOL_TABLE.getInstance().beginScope();

        TYPE_LIST tail = null;
        for (AST_ID_LIST it = tiList; it  != null; it = it.tail)
        {
            t = it.type.SemantMe();

            TYPE alreadyDefinedVar = SYMBOL_TABLE.getInstance().inScope(it.name);
            if (alreadyDefinedVar != null)
                throw new RuntimeException(String.format("%d", it.lineNum));
    
            if (t == TYPE_VOID.getInstance())
                throw new RuntimeException(String.format("%d", it.lineNum));
            if (tail == null)
            {
                thisType.params = new TYPE_LIST(t, null);
                tail = thisType.params;
            }
            else
            {
                tail.tail = new TYPE_LIST(t, null);
                tail = tail.tail;
            }
            SYMBOL_TABLE.getInstance().enter(it.name, new TYPE_VAR(t, it.name));
        }
        
        if (alreadyDefined  != null)
        {
            if (!(alreadyDefined  instanceof TYPE_FUNCTION))
                throw new RuntimeException(String.format("%d", type.lineNum));
            TYPE_FUNCTION prevFunc = (TYPE_FUNCTION)alreadyDefined ;
            if (prevFunc.returnType != returnType)
                throw new RuntimeException(String.format("%d", type.lineNum));

            TYPE_LIST typeList = thisType.params;
            TYPE_LIST prevTypeList = prevFunc.params;
            while (typeList != null && prevTypeList != null)
            {
                if (typeList.head != prevTypeList.head)
                    throw new RuntimeException(String.format("%d", type.lineNum));
                typeList = typeList.tail;
                prevTypeList = prevTypeList.tail;
            }
            
            if (typeList != null || prevTypeList != null)
                throw new RuntimeException(String.format("%d", type.lineNum));
        }

        sList.SemantMe();
        SYMBOL_TABLE.getInstance().endScope();
        SYMBOL_TABLE.getInstance().funcReturnType = null;
        
        return thisType;
    }
}
