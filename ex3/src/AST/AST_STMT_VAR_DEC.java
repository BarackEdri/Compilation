package AST;

import TYPES.*;
import SYMBOL_TABLE.*;

/**
 * AST_STMT_VARDEC
 */
public class AST_STMT_VAR_DEC extends AST_STMT
{

    public AST_VAR_DEC var;

    public AST_STMT_VAR_DEC(AST_VAR_DEC var)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("stmt -> varDec\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.var = var;
	}


    public void PrintMe()
	{
		/*************************************/
		/* AST NODE TYPE = AST STMT VARDEC   */
		/*************************************/
		System.out.print("AST STMT_VAR_DEC\n");

		/*************************************/
		/* RECURSIVELY PRINT             ... */
		/*************************************/
		if (var != null) var.PrintMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
        AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("STMT(VAR_DEC)"));


        if (var != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, var.SerialNumber);
	}
    
	public TYPE SemantMe()
	{
		TYPE type = var.SemantMe();
		return type;
	}
}