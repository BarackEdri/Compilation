package AST;

import TEMP.*;
import TYPES.*;

public class AST_DEC_CLASS2 extends AST_DEC {
    public AST_DEC_CLASS1 cd;

    public AST_DEC_CLASS2(AST_DEC_CLASS1 cd) {
        this.cd = cd;
        SerialNumber = AST_Node_Serial_Number.getFresh();
        if (cd != null) {System.out.print("dec -> classDec\n");}
    }

    public void PrintMe() {
        System.out.format("AST DEC CLASS 2 NODE\n");
        if (cd != null) {cd.PrintMe();}
        AST_GRAPHVIZ.getInstance().logNode(SerialNumber, "DEC CLASS 2");
        if (cd != null) {AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, cd.SerialNumber);}
    }

    public TYPE SemantMe() {
        System.out.println("DEC CLASS 2 - SemantMe");
        return cd.SemantMe();
    }

    public TEMP IRme() {
        System.out.println("DEC CLASS 2 - IRme");
        cd.IRme();
        return null;
    }
}
