package AST;

import IR.*;
import TEMP.*;
import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_VAR_FIELD extends AST_VAR {
    public AST_VAR var;
    public String fieldName;
    public String classN;

    public AST_VAR_FIELD(AST_VAR var, String fieldName, int line) {
        SerialNumber = AST_Node_Serial_Number.getFresh();
        System.out.format("var -> var.ID( %s )\n", fieldName);
        this.var = var;
        this.fieldName = fieldName;
        this.line = line;
    }

    public void PrintMe() {
        System.out.format("AST VAR FIELD, with field name %s NODE\n", fieldName);
        if (var != null) {var.PrintMe();}
        AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("VAR FIELD -> %s\n", fieldName));
        if (var != null) {AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, var.SerialNumber);}
    }

    public TYPE SemantMe() {
        System.out.format("VVAR FIELD - SemantMe\n");
        TYPE res = null;
        TYPE t1 = var.SemantMe();
        classN = t1.name;
        if (t1 == null || !(t1 instanceof TYPE_CLASS)) {
            System.out.format(">> ERROR [%d] %s is not declared.\n", line, var);
            printError(line);
        }
        String className = SYMBOL_TABLE.getInstance().inClassScope();
        TYPE fieldType = SYMBOL_TABLE.getInstance().findInClassScope(fieldName);
        if (className != null && className.equals(t1.name) && fieldType != null) return fieldType;
        else if (className != null && className.equals(t1.name)) {
            String fatherName = SYMBOL_TABLE.getInstance().findExtendsClass(className);
            if (fatherName != null) {
                TYPE_CLASS fatherClass = (TYPE_CLASS) SYMBOL_TABLE.getInstance().find(fatherName);
                while (fatherClass != null) {
                    for (AST_ARG_LIST it = fatherClass.data_members; it != null; it = it.tail) {
                        if (it.head.id.equals(fieldName)) {String resName = it.head.type.typeName;return res = SYMBOL_TABLE.getInstance().find(resName);}
                    }
                    fatherClass = fatherClass.father;
                }
            }
            System.out.println(">> ERROR [" + line + "] no such class field");
            printError(line);
        }
        while (t1 != null) {
            AST_ARG_LIST t1_data_members = ((TYPE_CLASS) t1).data_members;
            for (AST_ARG_LIST it = t1_data_members; it != null; it = it.tail) {
                if (it.head.id.equals(fieldName)) {String resName = it.head.type.typeName;return res = SYMBOL_TABLE.getInstance().find(resName);}
            }
            t1 = ((TYPE_CLASS) t1).father;
        }
        System.out.println(">> ERROR [" + line + "] no such class field");
        printError(line);
        return null;
    }

    public TEMP IRme() {
        System.out.format("VAR FIELD - IRme\n");
        TEMP t1 = var.IRme();
        TEMP t2 = TEMP_FACTORY.getInstance().getFreshTEMP();
        IRcommand r = new IRcommand_Field_Access(t2, t1, fieldName);
        IR.getInstance().Add_IRcommand(r);
        r.offset = GetOffset(classN + "_" + fieldName);
        return t2;
    }
}
