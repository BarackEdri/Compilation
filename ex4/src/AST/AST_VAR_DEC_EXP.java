package AST;

import IR.*;
import TEMP.*;
import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_VAR_DEC_EXP extends AST_VAR_DEC {
    public AST_EXP exp;
    String scope, class_name;
    boolean inFunc;

    public AST_VAR_DEC_EXP(AST_TYPE type, String id, AST_EXP exp, int line) {
        SerialNumber = AST_Node_Serial_Number.getFresh();
        this.type = type;
        this.id = id;
        this.exp = exp;
        this.line = line;
        if (type != null && exp != null) {System.out.print("varDec -> type ID ASSIGN exp SEMICOLON\n");}
    }

    public void PrintMe() {
        System.out.print(String.format("AST VAR DEC EXP NODE\n"));
        if (exp != null) {exp.PrintMe();}
        if (type != null) {type.PrintMe();}
        AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("VAR DEC EXP(%s)", id));
        if (type != null) {AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, type.SerialNumber);}
        if (exp != null) {AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, exp.SerialNumber);}
    }

    public TYPE SemantMe() {
        System.out.println("VAR DEC EXP - SemantMe");
        TYPE t1;
        TYPE t2 = null;
        if (type == null || exp == null) {System.out.println(">>>ERROR type or name are null");printError(this.line);}
        t1 = findType(type.typeName);
        t2 = exp.SemantMe();
        if (t1 == null || (!(type_equals(t1, t2)) && !isExtendingClass(t1, t2) && !isArrayAssignable(t1, t2)) || t1 instanceof TYPE_VOID || t1 instanceof TYPE_NIL) {
            String tname = "non-existing";
            if (t1 != null) tname = t1.name;
            System.out.format(">> ERROR [%d] type mismatch for var := exp. %s vs %s\n", line, tname, t2.name);
            printError(this.line);
        }
        TYPE res = SYMBOL_TABLE.getInstance().findInCurrScope(id);
        if (res != null) {
            System.out.format(">> ERROR [%d] %s is already declared.\n", line, id);
            printError(this.line);
        }
        class_name = SYMBOL_TABLE.getInstance().inClassScope();
        scope = SYMBOL_TABLE.getInstance().getScope();
        inFunc = SYMBOL_TABLE.getInstance().inFuncScope();
        if (scope.equals("class") && !(exp instanceof AST_EXP_INT || exp instanceof AST_EXP_NIL || exp instanceof AST_EXP_STRING || exp instanceof AST_EXP_INT_MINUS)) {
            System.out.println(">> ERROR[" + line + "] cant declare non primitive variable in class");
            printError(line);
        }
        isOverride();
        SYMBOL_TABLE.getInstance().enter(id, t1);
        return t1;
    }

    public TEMP IRme() {
        System.out.println("VAR DEC EXP - IRme");
        String isID = id;
        if (scope.equals("global") || (!inFunc && class_name != null)) {
            if (!inFunc && class_name != null) {isID = class_name + "_" + id;}
            if (type instanceof AST_TYPE_STRING) {
                String value = ((AST_EXP_STRING) exp).s;
                IR.getInstance().Add_IRcommand(new IRcommand_Call_String(isID, value));
                if (class_name != null) {defaultFields.put(class_name + "_" + id, value);}
            } else if (type instanceof AST_TYPE_INT) {
                System.out.println(exp.getClass().getSimpleName());
                int value = 0;
                if (exp instanceof AST_EXP_INT) {value = ((AST_EXP_INT) exp).value;}
                else if (exp instanceof AST_EXP_INT_MINUS) {value = ((AST_EXP_INT_MINUS) exp).value;}
                IR.getInstance().Add_IRcommand(new IRcommand_Call_Int(isID, value));
                if (class_name != null) {defaultFields.put(class_name + "_" + id, String.valueOf(value));}
            } 
            else {IR.getInstance().Add_IRcommand(new IRcommand_Call_Object(isID));}
        }
        else {
            TEMP t = exp.IRme();
            IRcommand command = new IRcommand_Store_Local(id, t);
            command.offset = GetOffset(id);
            IR.getInstance().Add_IRcommand(command);
        }
        return null;
    }
}