package AST;

import IR.*;
import TEMP.*;
import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_STMT_EXP_NEW extends AST_STMT {
    public AST_VAR var;
    public AST_EXP_NEW2 exp;
    public TYPE scope;
    public String inclass;

    public AST_STMT_EXP_NEW(AST_VAR var, AST_EXP_NEW2 exp, int line) {
        SerialNumber = AST_Node_Serial_Number.getFresh();
        this.var = var;
        this.exp = exp;
        this.line = line;
        System.out.print("stmt -> var:v ASSIGN newExp:ne SEMICOLON\n");
    }

    public void PrintMe() {
        System.out.print(String.format("AST STMT EXP NEW NODE\n"));
        if (var != null) var.PrintMe();
        if (exp != null) exp.PrintMe();
        AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("STMT EXP NEW"));
        if (var != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, var.SerialNumber);
        if (exp != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, exp.SerialNumber);
    }

    public TYPE SemantMe() {
        System.out.println("STMT EXP NEW - SemantMe");
        TYPE t1;
        TYPE t2;
        if (var == null || exp == null) {printError(this.line);}
        t1 = var.SemantMe();
        t2 = exp.SemantMe();
        if (t1 == null || t2 == null) {System.out.format(">> ERROR [%d] non existing type %s %s (STMTEXPNEW)\n", line, t1, t2);printError(line);}
        if (!(type_equals(t1, t2))) {
            if (!isArrayAssignable(t1, t2) && !isExtendingClass(t1, t2)) {
                System.out.format(">> ERROR [%d] type mismatch for type id = newExp; --- %s %s (STMTEXPNEW)\n", line, t1.name, t2.name);
                printError(this.line);
            }
        }
        if (var instanceof AST_VAR_SIMPLE && SYMBOL_TABLE.getInstance().inFuncScope()) {scope = SYMBOL_TABLE.getInstance().findInFuncScope(((AST_VAR_SIMPLE) var).name);}
        inclass = SYMBOL_TABLE.getInstance().inClassScope();
        return null;
    }

    public TEMP IRme() {
        System.out.println("STMT EXP NEW - IRme");
        TEMP value = exp.IRme();
        if (var instanceof AST_VAR_SIMPLE) {
            String name = ((AST_VAR_SIMPLE) var).name;
            if (((AST_VAR_SIMPLE) var).inGlobal == 1){IR.getInstance().Add_IRcommand(new IRcommand_Store_Global(value, name));}
            else {
                if (scope != null){
                    String varName = ((AST_VAR_SIMPLE) var).name;
                    IRcommand cRcommand = new IRcommand_Store_Local(varName, value);
                    IR.getInstance().Add_IRcommand(cRcommand);
                    cRcommand.offset = GetOffset(varName);
                } else if (inclass != null) {
                    String varName = inclass + "_" + ((AST_VAR_SIMPLE) var).name;
                    IRcommand c = new IRcommand_Field_Store(inclass, varName, value);
                    c.offset = GetOffset(varName);
                    IR.getInstance().Add_IRcommand(c);
                } else {
                    System.out.println("!");
                }
            }
        } else if (var instanceof AST_VAR_FIELD) {
            TEMP t1 = ((AST_VAR_FIELD) var).var.IRme();
            String f_name = ((AST_VAR_FIELD) var).fieldName;
            String c = ((AST_VAR_FIELD) var).classN;
            IRcommand r = new IRcommand_Field_New(t1, f_name, value);
            r.offset = GetOffset(c + "_" + f_name);
            IR.getInstance().Add_IRcommand(r);
        } else {
            AST_VAR_SUBSCRIPT subVar = (AST_VAR_SUBSCRIPT) var;
            TEMP array = subVar.var.IRme();
            TEMP index = subVar.subscript.IRme();
            IR.getInstance().Add_IRcommand(new IRcommand_Array_Assign(array, index, value));
        }
        return null;
    }
}