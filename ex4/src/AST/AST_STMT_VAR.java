package AST;

import IR.*;
import TEMP.*;
import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_STMT_VAR extends AST_STMT {
    public AST_VAR_DEC v;

    public AST_STMT_VAR(AST_VAR_DEC v) {
        SerialNumber = AST_Node_Serial_Number.getFresh();
        this.v = v;
        System.out.print("stmt -> varDec\n");
    }

    public void PrintMe() {
        System.out.format("AST STMT VAR NODE\n");
        if (v != null) v.PrintMe();
        AST_GRAPHVIZ.getInstance().logNode(SerialNumber, "STMT VAR");
        if (v != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, v.SerialNumber);
    }

    public TYPE SemantMe() {
        System.out.println("STMT VAR - SemantMe");
        v.SemantMe();
        return null;
    }

    public TEMP IRme() {
        System.out.println("STMT VAR - IRme");
        v.IRme();
        return null;
    }
}