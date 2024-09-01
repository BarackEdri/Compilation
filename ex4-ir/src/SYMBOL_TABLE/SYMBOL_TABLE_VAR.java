package SYMBOL_TABLE;

import TYPES.*;

public class SYMBOL_TABLE_VAR extends SYMBOL_TABLE_ENTRY{

    /**********/
	/* IR_var */
	/**********/
    public IR.IRVariableID IR_var;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
    public SYMBOL_TABLE_VAR(String name, TYPE type, int index, SYMBOL_TABLE_ENTRY next, SYMBOL_TABLE_ENTRY prevtop, int prevtop_index) {
        super(name, type, index, next, prevtop, prevtop_index);
        this.IR_var = new IR.IRVariableID(name);
    }
}
