package AST;

import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_VAR_FIELD extends AST_VAR
{
	public AST_VAR var;
	public String fieldName;

	public AST_VAR_FIELD(AST_VAR var, String fieldName)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.format("var -> var DOT ID( %s )\n",fieldName);

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.var = var;
		this.fieldName = fieldName;
	}

	/*************************************************/
	/* The printing message for a field var AST node */
	/*************************************************/
	public void PrintMe()
	{
		/*********************************/
		/* AST NODE TYPE = AST FIELD VAR */
		/*********************************/
		System.out.print("AST NODE FIELD VAR\n");

		/**********************************************/
		/* RECURSIVELY PRINT VAR, then FIELD NAME ... */
		/**********************************************/
		if (var != null) { var.PrintMe(); }
		System.out.format("FIELD NAME( %s )\n",fieldName);

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("FIELD\nVAR\n...->%s",fieldName));
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (var != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);
	}

	public String getName(){
		return fieldName;
	}

    public TYPE SemantMe()
    {
        TYPE varType = var.SemantMe();
        if (varType == null || !varType.isClass())
            throw new RuntimeException(String.format("%d", lineNum));
        
        TYPE_CLASS classType = (TYPE_CLASS) varType;
        TYPE fieldType = null;
        while (classType != null && fieldType == null)
        {
            for (TYPE_CLASS_VAR_DEC_LIST data_member_list = classType.data_members; data_member_list != null; data_member_list = data_member_list.tail)
            {
                if (data_member_list.head.name.equals(fieldName))
                {
                    fieldType = data_member_list.head.type;
                    break;
                }
            }
            classType = classType.father;
        }
        if (fieldType == null || fieldType.isFunction())
            throw new RuntimeException(String.format("%d", lineNum));
        
        return ((TYPE_VAR)fieldType).type;
    }
}
