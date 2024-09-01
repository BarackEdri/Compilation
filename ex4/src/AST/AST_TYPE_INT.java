package AST;

import TYPES.*;

public class AST_TYPE_INT extends AST_TYPE {

    public AST_TYPE_INT(int line) {
        SerialNumber = AST_Node_Serial_Number.getFresh();
        this.line = line;
        this.typeName = "int";
        System.out.print("type -> TYPE_INT\n");
    }

    public void PrintMe() {
        System.out.format("AST TYPE INT NODE\n");
        AST_GRAPHVIZ.getInstance().logNode(SerialNumber, "int");
    }

    public TYPE SemantMe() {
        System.out.format("TYPE INT - SemantMe\n");
        return TYPE_INT.getInstance();
    }
}