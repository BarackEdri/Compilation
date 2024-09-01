package AST;

import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_STMT_ASSIGN_NEW extends AST_STMT
{
	/***************/
	/*  var := exp */
	/***************/
	public AST_VAR var;
	public AST_NEW_EXP nexp;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_STMT_ASSIGN_NEW(AST_VAR var, AST_NEW_EXP nexp)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("stmt -> var ASSIGN newExp ;\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.var = var;
		this.nexp = nexp;
	}

	/*********************************************************/
	/* The printing message for an assign statement AST node */
	/*********************************************************/
	public void PrintMe()
	{
		/********************************************/
		/* AST NODE TYPE = AST ASSIGNMENT STATEMENT */
		/********************************************/
		System.out.print("AST NODE ASSIGN NEW EXP\n");

		/***********************************/
		/* RECURSIVELY PRINT VAR + EXP ... */
		/***********************************/
		if (var != null) var.PrintMe();
		if (nexp != null) nexp.PrintMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"ASSIGN\nleft := right\n");
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,nexp.SerialNumber);
	}

	public TYPE SemantMe() 
	{
        TYPE varType = var.SemantMe();

        if (varType == TYPE_VOID.getInstance()
            || (!varType.isClass() && !varType.isArray()))
            throw new RuntimeException(String.format("%d", lineNum));

        TYPE expType = nexp.SemantMe();
        if (expType.isClass() && varType.isClass())
        {
            if (nexp.e != null)
                throw new RuntimeException(String.format("%d", lineNum));
            if (!((TYPE_CLASS)expType).isSubClassOf(((TYPE_CLASS)varType)))
                throw new RuntimeException(String.format("%d", lineNum));
        }
        else if (varType.isArray())
        {
            if (nexp.e == null)
                throw new RuntimeException(String.format("%d", lineNum));
            if (((TYPE_ARRAY)varType).type.isClass() && expType.isClass())
            {
                if (!((TYPE_CLASS)expType).isSubClassOf((TYPE_CLASS)((TYPE_ARRAY)varType).type))
                    throw new RuntimeException(String.format("%d", lineNum));
            }
            else if (((TYPE_ARRAY)varType).type != expType)
                throw new RuntimeException(String.format("%d", lineNum));
        }
        else if (expType != varType)
            throw new RuntimeException(String.format("%d", lineNum));
        
        return null;
	}
}
