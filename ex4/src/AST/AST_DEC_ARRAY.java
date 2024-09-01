package AST;

import TEMP.*;
import TYPES.*;

public class AST_DEC_ARRAY extends AST_DEC {
    public AST_ARG_ARRAY array;

    public AST_DEC_ARRAY(AST_ARG_ARRAY array) {
        this.array = array;
        SerialNumber = AST_Node_Serial_Number.getFresh();
        if (array != null) {System.out.print("dec -> arrayTypeDef\n");}
    }

    public void PrintMe() {
        System.out.format("AST DEC ARRAY NODE\n");
        if (array != null) {array.PrintMe();}
        AST_GRAPHVIZ.getInstance().logNode(SerialNumber, "DEC ARRAY");
        if (array != null) {AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, array.SerialNumber);}
    }

    public TYPE SemantMe() {
        System.out.println("DEC ARRAY - SemantMe");
        array.SemantMe();
        return null;
    }

    public TEMP IRme() {
        System.out.println("DEC ARRAY - IRme");
        array.IRme();
        return null;
    }
}