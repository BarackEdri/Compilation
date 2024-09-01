package AST;

import IR.*;
import TEMP.*;
import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_EXP_DOT_LIST extends AST_EXP {
    public AST_VAR var;
    public String id;
    public AST_EXP_LIST2 list;
    public TYPE_CLASS tl;

    public AST_EXP_DOT_LIST(AST_VAR var, String id, AST_EXP_LIST2 list, int line) {
        SerialNumber = AST_Node_Serial_Number.getFresh();
        System.out.print("exp -> vardot_explist\n");
        this.var = var;
        this.id = id;
        this.list = list;
        this.line = line;
    }

    public void PrintMe() {
        System.out.print("AST NODE EXP DOT LIST\n");
        if (var != null) var.PrintMe();
        if (list != null) list.PrintMe();
        AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("EXP DOT LIST(%s)", id));
        if (var != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, var.SerialNumber);
        if (list != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, list.SerialNumber);

    }

    public TYPE SemantMe() {
        System.out.println("EXP DOT LIST - SemantMe");
        TYPE t1 = var.SemantMe();
        if (!(t1 instanceof TYPE_CLASS)) {System.out.println(">> ERROR [" + line + "] var.dot is of wrong class");printError(line);}
        if (t1 instanceof TYPE_CLASS) {tl = (TYPE_CLASS) t1;}
        TYPE t2 = isFuncOfClass(t1.name, id, list, this.line);
        if (t2 == null) {System.out.println(">> ERROR [" + line + "] var.dot is wrong");printError(line);}
        return t2;
    }

    public TEMP_LIST IRme(int ignore) {
        System.out.println("EXP DOT LIST - IRme");
        if (list == null) {return new TEMP_LIST(var.IRme(), null);}
        return new TEMP_LIST(var.IRme(), list.IRme(0));
    }

    public TEMP IRme() {
        System.out.println("EXP DOT LIST- IRme");
        return vardotIR(var, list, tl, id);
    }
}