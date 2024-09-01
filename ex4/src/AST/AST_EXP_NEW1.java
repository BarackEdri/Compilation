package AST;

import IR.*;
import TEMP.*;
import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_EXP_NEW1 extends AST_EXP_NEW2 {
    public AST_TYPE t;
    public AST_EXP e;

    public AST_EXP_NEW1(AST_TYPE t, AST_EXP e, int line) {
        SerialNumber = AST_Node_Serial_Number.getFresh();
        this.t = t;
        this.e = e;
        this.line = line;
        System.out.print("newExp -> NEW type:t LBRACK exp:e RBRACK\n");
    }


    public void PrintMe() {
        System.out.format("AST EXP NEW 1 NODE\n");
        if (t != null) t.PrintMe();
        if (e != null) e.PrintMe();
        AST_GRAPHVIZ.getInstance().logNode(SerialNumber, "EXP NEW 1");
        if (t != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, t.SerialNumber);
        if (e != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, e.SerialNumber);
    }

    public TYPE SemantMe() {
        System.out.println("EXP NEW 1 - SemantMe");
        TYPE t1;
        TYPE t2;
        if (t == null || e == null || t.typeName.equals("nil") || t.typeName.equals("void")) {System.out.println(">> ERROR [" + line + "] cant declate type void/nil");printError(this.line);}
        t1 = t.SemantMe();
        t2 = e.SemantMe();
        if (t1 == null || t2 == null) {System.out.format(">> ERROR [%d] non existing type %s %s\n", line, t1, t2);printError(line);}
        if (!type_equals(t2, TYPE_INT.getInstance())) {
            if (t2 != null) {System.out.format(">> ERROR [%d] array subscript type is %s- new type[exp]; (newexp_exp)\n", line, t2.name);}
            printError(this.line);
        }
        if ((e instanceof AST_EXP_INT) && (((AST_EXP_INT) e).value <= 0)) {System.out.format(">> ERROR [%d] array subscript must be positive; (newexp_exp)\n", line);printError(this.line);}
        if (e instanceof AST_EXP_INT_MINUS) {System.out.format(">> ERROR [%d] array subscript must be positive; (newexp_exp)\n", line);printError(this.line);}
        if (t1 != null) {return new TYPE_ARRAY(t1, t1.name + "[]");}
        return null;
    }

    public TEMP IRme() {
        TEMP t1 = e.IRme();
        TEMP t2 = TEMP_FACTORY.getInstance().getFreshTEMP();
        IR.getInstance().Add_IRcommand(new IRcommand_Array_New(t2, t1));
        return t2;
    }
}