package AST;

import IR.*;
import TEMP.*;
import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_VAR_DEC_SIMPLE extends AST_VAR_DEC {
    public String inclass;
    public TYPE t;
    public boolean inFunc;

    public AST_VAR_DEC_SIMPLE(AST_TYPE type, String id, int line) {
        SerialNumber = AST_Node_Serial_Number.getFresh();
        this.type = type;
        this.id = id;
        this.line = line;
        if (type != null) {System.out.print("varDec -> type ID SEMICOLON\n");}
    }

    public void PrintMe() {
        System.out.print(String.format("AST VAR DEC SIMPLE NODE\n"));
        if (type != null) {type.PrintMe();}
        AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("VAR DEC SIMPLE(%s)", id));
        if (type != null) {AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, type.SerialNumber);}
    }

    public TYPE SemantMe() {
        System.out.println("VAR DEC SIMPLE - SemantMe");
        TYPE t1 = findType(type.typeName);
        t = t1;
        if (t1 == null || t1 instanceof TYPE_VOID || t1 instanceof TYPE_NIL) {
            System.out.format(">> ERROR [%d] type " + type.typeName + " does not exist", line);
            printError(line);
        }
        TYPE t2 = SYMBOL_TABLE.getInstance().findInCurrScope(id);
        if (t2 != null) {
            System.out.format(">> [%d] ERROR variable " + id + " already exist", line);
            printError(line);
        }
        isOverride();
        SYMBOL_TABLE.getInstance().enter(id, t1);
        scope = SYMBOL_TABLE.getInstance().getScope();
        inclass = SYMBOL_TABLE.getInstance().inClassScope();
        inFunc = SYMBOL_TABLE.getInstance().inFuncScope();
        return t1;
    }

    public TEMP IRme() {
        System.out.println("VAR DEC SIMPLE - IRme");
        if (scope.equals("global")) {
            if (type instanceof AST_TYPE_STRING) {IR.getInstance().Add_IRcommand(new IRcommand_Call_String(id, "barak"));} 
            else if (type instanceof AST_TYPE_INT) {IR.getInstance().Add_IRcommand(new IRcommand_Call_Int(id, 0));} 
            else {IR.getInstance().Add_IRcommand(new IRcommand_Call_Object(id));}
        }
        else {if (!inFunc && inclass != null) {String namec = inclass + "_" + id;IR.getInstance().Add_IRcommand(new IRcommand_Call_Object(namec));}}
        return null;
    }
}