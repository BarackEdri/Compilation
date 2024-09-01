package AST;

import IR.*;
import TEMP.*;
import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_STMT_DOT_LIST extends AST_STMT {
    public AST_VAR var;
    public String id;
    public AST_EXP_LIST2 list;
    public TYPE_CLASS tl;

    public AST_STMT_DOT_LIST(AST_VAR var, String id, AST_EXP_LIST2 list, int line) {
        SerialNumber = AST_Node_Serial_Number.getFresh();
        this.var = var;
        this.id = id;
        this.list = list;
        this.line = line;
        System.out.print("stmt -> var.ID(expList);\n");
    }

    public void PrintMe() {
        System.out.format("AST STMT DOT LIST NODE\n");
        if (var != null) var.PrintMe();
        if (list != null) list.PrintMe();
        AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("STMT DOT LIST(%s)", id));
        if (var != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, var.SerialNumber);
        if (list != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, list.SerialNumber);
    }

    public TYPE SemantMe() {
        System.out.println("STMT DOT LIST - SemantMe");
        TYPE t1 = var.SemantMe();
        if (t1 == null || !(t1 instanceof TYPE_CLASS)) {System.out.println(">> ERROR [" + line + "] var.dot is of wrong class");printError(line);}
        tl = (TYPE_CLASS) t1;
        TYPE t2 = isFuncOfClass(t1.name, id, list, this.line);
        if (t2 == null) {System.out.println(">> ERROR [" + line + "] var.dot is wrong");printError(line);}
        return t2;
    }

    public TEMP IRme() {
        System.out.println("STMT DOT LIST - IRme");
        return vardotIR(var, list, tl, id);
    }
}
