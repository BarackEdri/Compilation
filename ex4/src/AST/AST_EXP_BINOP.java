package AST;

import IR.*;
import TEMP.*;
import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_EXP_BINOP extends AST_EXP {
    public AST_ARG_BINOP binop;

    public AST_EXP_BINOP(AST_ARG_BINOP binop) {
        SerialNumber = AST_Node_Serial_Number.getFresh();
        System.out.print("exp -> exp BINOP exp\n");
        this.binop = binop;
    }

    public void PrintMe() {
        System.out.print("AST EXP BINOP NODE\n");
        if (binop != null) binop.PrintMe();
        AST_GRAPHVIZ.getInstance().logNode(SerialNumber, "EXP BINOP");
        if (binop != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, binop.SerialNumber);
    }

    public TYPE SemantMe() {
        System.out.println("EXP BINOP - SemantMe");
        return binop.SemantMe();
    }

    public TEMP IRme() {
        System.out.println("EXP BINOP - IRme");
        return binop.IRme();
    }
}