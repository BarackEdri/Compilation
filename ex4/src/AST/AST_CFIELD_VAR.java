package AST;

import TEMP.*;
import TYPES.*;

public class AST_CFIELD_VAR extends AST_CFIELD {
    public AST_VAR_DEC varDec;

    public AST_CFIELD_VAR(AST_VAR_DEC varDec) {
        this.varDec = varDec;
        SerialNumber = AST_Node_Serial_Number.getFresh();
        if (varDec != null) {System.out.print("cfield -> varDec\n");}
    }

    public void PrintMe() {
        System.out.format("AST CFIELD VAR NODE\n");
        if (varDec != null) {varDec.PrintMe();}
        AST_GRAPHVIZ.getInstance().logNode(SerialNumber, "CFIELD VAR");
        if (varDec != null) {AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, varDec.SerialNumber);}
    }

    public TYPE SemantMe() {
        System.out.println("CFIELD VAR - SemantMe");
        return varDec.SemantMe();

    }

    public TEMP IRme() {
        System.out.println("CFIELD VAR - IRme");
        varDec.IRme();
        return null;
    }

}
