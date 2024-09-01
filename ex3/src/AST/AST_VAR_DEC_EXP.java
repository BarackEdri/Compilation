package AST;

import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_VAR_DEC_EXP extends AST_VAR_DEC
{

    public AST_TYPE type;
    public AST_EXP exp;

    /******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_VAR_DEC_EXP(AST_TYPE type ,String id, AST_EXP exp)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("dec -> varDec\n");
		if (exp != null) System.out.format("varDec -> type ID( %s ) ASSIGN exp\n", id);
        if (exp == null) System.out.format("varDec -> type ID( %s )\n", id);

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.type = type;
		this.id = id;
        this.exp = exp;
	}

	/*************************************************/
	/* The printing message for a field var AST node */
	/*************************************************/
	public void PrintMe()
	{
		/*********************************/
		/* AST NODE TYPE = AST FIELD VAR */
		/*********************************/
		System.out.print("AST VAR DEC EXP\n");

		/**********************************************/
		/* RECURSIVELY PRINT type, then id ... */
		/**********************************************/
        
		if (type != null) type.PrintMe();
        if (exp != null) exp.PrintMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("VarDec(%s)", id));
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (type != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,type.SerialNumber);
        if (exp  != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,exp.SerialNumber);
    }

	public TYPE SemantMe()
	{
        TYPE alreadyDefined = SYMBOL_TABLE.getInstance().inScope(id);
        if (alreadyDefined != null)
		throw new RuntimeException(String.format("%d", lineNum));
        TYPE varType = type.SemantMe();
        if (varType == TYPE_VOID.getInstance())
		throw new RuntimeException(String.format("%d",lineNum));
        if (exp != null && exp instanceof AST_EXP_NIL && !varType.isClass() && !varType.isArray())
		throw new RuntimeException(String.format("%d",lineNum));
        
        if (exp != null && !(exp instanceof AST_EXP_NIL && (varType.isClass() || varType.isArray())))
        {
            TYPE expType = exp.SemantMe();
            if (expType.isClass() && varType.isClass())
            {
                if (!((TYPE_CLASS)expType).isSubClassOf(((TYPE_CLASS)varType)))
				throw new RuntimeException(String.format("%d",lineNum));
            }
            else if (expType != varType)
			throw new RuntimeException(String.format("%d",lineNum));
        }
		SYMBOL_TABLE.getInstance().enter(id, new TYPE_VAR(varType, id));
        return new TYPE_VAR(varType, id);
	}

}
