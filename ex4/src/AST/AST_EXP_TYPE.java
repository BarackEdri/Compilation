package AST;

import IR.*;
import TEMP.*;
import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_EXP_TYPE extends AST_EXP_NEW2 {
    public AST_TYPE t;

    public AST_EXP_TYPE(AST_TYPE t, int line) {
        SerialNumber = AST_Node_Serial_Number.getFresh();
        this.t = t;
        this.line = line;
        System.out.print("newExp -> NEW type:t\n");
    }

    public void PrintMe() {
        System.out.format("AST EXP TYPE NODE\n");
        if (t != null) t.PrintMe();
        AST_GRAPHVIZ.getInstance().logNode(SerialNumber, "EXP TYPE");
        if (t != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, t.SerialNumber);
    }

    public TYPE SemantMe() {
        System.out.println("EXP TYPE - SemantMe");
        if (t.typeName.equals("void") || t.typeName.equals("nil")) {
            System.out.println(">> ERROR [" + line + "] cant declare type void/nil");
            printError(line);
        }
        if (t.typeName.equals("int")) {return TYPE_INT.getInstance();}
        if (t.typeName.equals("string")) {return TYPE_STRING.getInstance();}
        TYPE cl = SYMBOL_TABLE.getInstance().findClass(t.typeName);
        if (cl == null) {System.out.println(">> ERROR [" + line + "] cant declare " + t.typeName + " type");printError(line);}
        return cl;
    }

    public TEMP IRme() {
        System.out.println("EXP TYPE- IRme");
        TEMP t1 = TEMP_FACTORY.getInstance().getFreshTEMP();
        IR.getInstance().Add_IRcommand(new IRcommand_Set_Class(t1, t.typeName));
        return t1;
    }
}