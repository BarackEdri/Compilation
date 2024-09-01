package AST;

import TEMP.*;
import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_PROGRAM extends AST_Node {
    public AST_DEC_LIST list;

    public AST_PROGRAM(AST_DEC_LIST list, String file) {
        SerialNumber = AST_Node_Serial_Number.getFresh();
        this.list = list;
        getFile(file);
        System.out.print("program -> decs\n");
    }

    public void PrintMe() {
        System.out.print("AST PROGRAM NODE\n");
        list.PrintMe();
        AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("PROGRAM"));
        AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, list.SerialNumber);
        SYMBOL_TABLE.getInstance().PrintMe();
    }

    public TYPE SemantMe() {
        System.out.println("PROGRAM - SemantMe\n");
        list.SemantMe();
        return null;
    }

    public TEMP IRme() {
        System.out.println("PROGRAM - IRme\n");
        list.IRme();
        return null;
    }
}
