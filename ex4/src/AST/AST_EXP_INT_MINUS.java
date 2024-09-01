package AST;

import IR.*;
import TEMP.*;
import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_EXP_INT_MINUS extends AST_EXP {
    public int value;

    public AST_EXP_INT_MINUS(int value) {
        SerialNumber = AST_Node_Serial_Number.getFresh();
        this.value = -value;
        System.out.format("exp -> -INT(%d)\n", value);
    }


    public void PrintMe() {
        System.out.format("AST NODE MINUS INT(%d)\n", value);
        AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("%d", value));
    }

    public TYPE SemantMe() {
        System.out.println("EXP MINUS INT - SemantMe");
        return TYPE_INT.getInstance();
    }

    public TEMP IRme() {
        System.out.println("EXP MINUS INT - IRme");
        TEMP t = TEMP_FACTORY.getInstance().getFreshTEMP();
        IR.getInstance().Add_IRcommand(new IRcommand_Const_Int(t, value));
        return t;
    }
}
