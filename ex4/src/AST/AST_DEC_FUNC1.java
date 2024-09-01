package AST;

import TEMP.*;
import TYPES.*;

public class AST_DEC_FUNC1 extends AST_DEC {
    public AST_DEC_FUNC2 func;

    public AST_DEC_FUNC1(AST_DEC_FUNC2 func) {
        this.func = func;
        SerialNumber = AST_Node_Serial_Number.getFresh();
        if (func != null) System.out.print("dec -> funcDec\n");
    }

    public void PrintMe() {
        System.out.format("AST DEC FUNC 1 NODE\n");
        if (func != null) {func.PrintMe();}
        AST_GRAPHVIZ.getInstance().logNode(SerialNumber, "DEC FUNC 1");
        if (func != null) {AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, func.SerialNumber);}
    }

    public TYPE SemantMe() {
        System.out.println("DEC FUNC 1- SemantMe");
        func.SemantMe();
        return null;
    }

    public TEMP IRme() {
        System.out.println("DEC FUNC 1 - IRme");
        func.IRme();
        return null;
    }
}
