package AST;

import IR.*;
import TEMP.*;
import TYPES.*;

public class AST_VAR_SUBSCRIPT extends AST_VAR {
    public AST_VAR var;
    public AST_EXP subscript;

    public AST_VAR_SUBSCRIPT(AST_VAR var, AST_EXP subscript, int line) {
        SerialNumber = AST_Node_Serial_Number.getFresh();
        System.out.print("var -> var [ exp ]\n");
        this.var = var;
        this.subscript = subscript;
        this.line = line;
    }

    public void PrintMe() {
        System.out.print("AST VAR SUBSCRIPT NODE\n");
        if (var != null) {var.PrintMe();}
        if (subscript != null) {subscript.PrintMe();}
        AST_GRAPHVIZ.getInstance().logNode(SerialNumber, "VAR SUBSCRIPT\n...[...]");
        if (var != null) {AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, var.SerialNumber);}
        if (subscript != null) {AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, subscript.SerialNumber);}
    }

    public TYPE SemantMe() {
        System.out.println("VAR SUBSCRIPT - SemantMe");
        TYPE t1 = var.SemantMe();
        if (t1 == null) {System.out.format(">> ERROR [%d] var is not declared. (var_subscript)\n", line);printError(line);}
        if (!(t1.isArray())) {System.out.println(">> ERROR [" + line + "] var is not an array");printError(line);}
        TYPE t2 = subscript.SemantMe();
        if (!(t2.name.equals("int"))) {System.out.println(">> ERROR [" + line + "] array index is not int");printError(line);}
        return ((TYPE_ARRAY) t1).entryType;
    }

    public TEMP IRme() {
        System.out.println("VAR SUBSCRIPT- IRme");
        TEMP t1 = TEMP_FACTORY.getInstance().getFreshTEMP();
        TEMP t2 = var.IRme();
        TEMP t3 = subscript.IRme();
        IR.getInstance().Add_IRcommand(new IRcommand_Array_Access(t1, t2, t3));
        return t1;
    }
}
