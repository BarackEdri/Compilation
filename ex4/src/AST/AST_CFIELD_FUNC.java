package AST;

import TEMP.*;
import TYPES.*;

public class AST_CFIELD_FUNC extends AST_CFIELD {
    public AST_DEC_FUNC2 func;

    public AST_CFIELD_FUNC(AST_DEC_FUNC2 func) {
        SerialNumber = AST_Node_Serial_Number.getFresh();
        this.func = func;
    }

    public void PrintMe() {
        System.out.format("AST CFIELD FUNC NODE\n");
        if (func != null) {func.PrintMe();}
        AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("CFIELD FUNC"));
        if (func != null) {AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, func.SerialNumber);}
    }

    public TYPE SemantMe() {
        System.out.println("CFIELD FUNC SemantMe");
        return func.SemantMe();
    }

    public TEMP IRme() {
        System.out.println("CFIELD FUNC - IRme");
        func.IRme();
        return null;
    }
}
