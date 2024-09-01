package AST;

import TYPES.*;

public class AST_TYPE_VOID extends AST_TYPE {
    
    public AST_TYPE_VOID(int line) {
        SerialNumber = AST_Node_Serial_Number.getFresh();
        this.line = line;
        this.typeName = "void";
        System.out.print("type -> TYPE_VOID\n");
    }

    public void PrintMe() {
        System.out.format("AST TYPE VOID NODE\n");
        AST_GRAPHVIZ.getInstance().logNode(SerialNumber, "void");
    }

    public TYPE SemantMe() {
        System.out.format("TYPE VOID - SemantMe\n");
        return TYPE_VOID.getInstance();
    }
}