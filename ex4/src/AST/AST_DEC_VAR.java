package AST;

import IR.*;
import TEMP.*;
import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_DEC_VAR extends AST_DEC {
    public AST_VAR_DEC v;

    public AST_DEC_VAR(AST_VAR_DEC v) {
        SerialNumber = AST_Node_Serial_Number.getFresh();
        this.v = v;
        if (v != null) {System.out.print("dec -> varDec\n");}
    }

    public void PrintMe() {
        System.out.printf("AST DEC VAR NODE\n");
        if (v != null) v.PrintMe();
        AST_GRAPHVIZ.getInstance().logNode(SerialNumber, "DEC VAR");
        if (v != null) {AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, v.SerialNumber);}
    }

    public TYPE SemantMe() {
        System.out.println("DEC VAR - SemantMe");
        if (v != null) {return v.SemantMe();}
        return null;
    }

    public TEMP IRme() {
        System.out.println("DEC VAR - IRme");
        if (v != null) {return v.IRme();}
        return null;
    }
}