package AST;

import TYPES.*;

public class AST_TYPE_STRING extends AST_TYPE {

    public AST_TYPE_STRING(int line) {
        SerialNumber = AST_Node_Serial_Number.getFresh();
        this.line = line;
        this.typeName = "string";
        System.out.print("type -> TYPE STRING\n");
    }

    public void PrintMe() {
        System.out.format("AST TYPE STRING NODE\n");
        AST_GRAPHVIZ.getInstance().logNode(SerialNumber, "string");
    }

    public TYPE SemantMe() {
        System.out.format("TYPE STRING - SemantMe\n");
        return TYPE_STRING.getInstance();
    }
}