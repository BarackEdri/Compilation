package AST;

import IR.*;
import TEMP.*;
import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_VAR_DEC_EXP_NEW extends AST_VAR_DEC {
    public AST_EXP_NEW2 exp;
    public TYPE t;

    public AST_VAR_DEC_EXP_NEW(AST_TYPE type, String id, AST_EXP_NEW2 exp, int line) {
        SerialNumber = AST_Node_Serial_Number.getFresh();
        this.type = type;
        this.id = id;
        this.exp = exp;
        this.line = line;
        if (type != null && exp != null) System.out.print("varDec -> type ID ASSIGN newExp SEMICOLON\n");
    }

    public void PrintMe() {
        System.out.format("AST VAR DEC EXP NEW NODE\n");
        if (type != null) type.PrintMe();
        if (exp != null) exp.PrintMe();
        AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("VAR DEC EXP NEW(%s)", id));
        if (type != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, type.SerialNumber);
        if (exp != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, exp.SerialNumber);
    }

    public TYPE SemantMe() {
        System.out.println("VAR DEC EXP NEW - SemantMe");
        TYPE t1 = type.SemantMe();
        TYPE t2 = exp.SemantMe();
        if (t1 == null || t2 == null || t1 instanceof TYPE_NIL || t1 instanceof TYPE_VOID) {
            System.out.format(">> ERROR [%d] non existing type %s %s (VARDEC_EXPNEW)\n", line, t1, t2);
            printError(this.line);
        }
        if (!(type_equals(t1, t2))) {
            if (!isArrayAssignable(t1, t2) && !isExtendingClass(t1, t2)) {
                System.out.format(">> ERROR [%d] type mismatch for type id = newExp;  --- %s %s  (VARDEC_EXPNEW)\n", line, t1.name, t2.name);
                printError(this.line);
            }
        }
        String scope = SYMBOL_TABLE.getInstance().getScope();
        if (scope.equals("class")) {
            System.out.format(">> ERROR [%d] cant declare non primitive variable\n", line);
            printError(this.line);
        }
        TYPE res = SYMBOL_TABLE.getInstance().findInCurrScope(id);
        if (res != null) {
            System.out.format(">> ERROR [%d] %s is already declared.\n", line, id);
            printError(this.line);
        } else {
            isOverride();
            SYMBOL_TABLE.getInstance().enter(id, t1);
        }
        t = t1;
        this.scope = scope;
        return t1;
    }

    public TEMP IRme() {
        System.out.println("VAR DEC EXP NEW - IRme");
        TEMP t = exp.IRme();
        if (scope.equals("global")) {
            printError(1);
        } else {
            IRcommand command = new IRcommand_Store_Local(id, t);
            command.offset = GetOffset(id);
            IR.getInstance().Add_IRcommand(command);
        }
        return null;
    }
}