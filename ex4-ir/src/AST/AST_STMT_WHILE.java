package AST;

import IR.*;
import TEMP.*;
import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_STMT_WHILE extends AST_STMT
{
	public AST_EXP cond;
	public AST_STMT_LIST body;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_STMT_WHILE(AST_EXP cond,AST_STMT_LIST body)
	{
		this.cond = cond;
		this.body = body;
	}
	public TEMP IRme()
	{
		/*******************************/
		/* [1] Allocate 2 fresh labels */
		/*******************************/
		String label_end   = IRcommand.getFreshLabel("end");
		String label_start = IRcommand.getFreshLabel("start");
	
		/*********************************/
		/* [2] entry label for the while */
		/*********************************/
		IR.
		getInstance().
		Add_IRcommand(new IRcommand_Label(label_start));

		/********************/
		/* [3] cond.IRme(); */
		/********************/
		TEMP cond_temp = cond.IRme();

		/******************************************/
		/* [4] Jump conditionally to the loop end */
		/******************************************/
		IR.
		getInstance().
		Add_IRcommand(new IRcommand_Jump_If_Eq_To_Zero(cond_temp,label_end));		

		/*******************/
		/* [5] body.IRme() */
		/*******************/
		body.IRme();
		/******************************/
		/* [6] Jump to the loop entry */
		/******************************/
		cond_temp = cond.IRme();
		IR.
		getInstance().
		Add_IRcommand(new IRcommand_Jump_start(cond_temp,label_start));

		IR.
		getInstance().
		Add_IRcommand(new IRcommand_Label(label_end));		

		/**********************/
		/* [7] Loop end label */
		/**********************/

		/*******************/
		/* [8] return null */
		/*******************/
		return null;
	}

	public TYPE SemantMe() {
		TYPE cond_t = cond.SemantMe();

		SYMBOL_TABLE.getInstance().beginScope();
		SYMBOL_TABLE.getInstance().enter("BLOCK-SCOPE", new TYPE_FOR_SCOPE_BOUNDARIES("WHILE"));

		body.SemantMe();
		SYMBOL_TABLE.getInstance().endScope();

		return null;
	}
}