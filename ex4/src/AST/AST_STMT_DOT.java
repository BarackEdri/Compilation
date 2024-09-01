package AST;

import TEMP.*;
import TYPES.*;

public class AST_STMT_DOT extends AST_STMT {
    public AST_VAR var;
    public String id;
    public TYPE_CLASS tl;

    public AST_STMT_DOT(AST_VAR var, String id, int line) {
        SerialNumber = AST_Node_Serial_Number.getFresh();
        this.var = var;
        this.id = id;
        this.line = line;
    }

    public void PrintMe() {
        System.out.format("AST STMT DOT NODE\n");
        if (var != null) var.PrintMe();
        AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("STMT DOT(%s)", id));
        if (var != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, var.SerialNumber);
    }

    public TYPE SemantMe() {
        System.out.println("STMT DOT - SemantMe");
        TYPE t1 = var.SemantMe();
        if (t1 == null || !(t1 instanceof TYPE_CLASS)){
            System.out.format(">> ERROR [%d] var.dot is of wrong class", line);
            printError(line);
        }
        tl = (TYPE_CLASS) t1;
        TYPE t2 = isFuncOfClass(t1.name, id, null, this.line);
        if (t2 == null) {
            System.out.format(">> ERROR [%d] var.dot is wrong", line);
            printError(line);
        }
        return t2;
    }

    public TEMP IRme() {
        System.out.println("STMT DOT - IRme");
        return vardotIR(var, null, tl, id);
    }
}