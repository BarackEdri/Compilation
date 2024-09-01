package AST;

import IR.*;
import TEMP.*;
import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_VAR_SIMPLE extends AST_VAR {
    public String name;
    public String className = null;
    public TYPE thisT;
    public boolean cfgVar = false;
    int inGlobal = 0;

    public AST_VAR_SIMPLE(String name, int line) {
        this.line = line;
        this.name = name;
        SerialNumber = AST_Node_Serial_Number.getFresh();
        System.out.format("var -> ID\n");
    }

    public void PrintMe() {
        System.out.format("AST NODE VAR SIMPLE( %s )\n", name);
        AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("VAR SIMPLE(%s)", name));
    }

    public TYPE SemantMe() {
        String currScope = SYMBOL_TABLE.getInstance().getScope();
        className = SYMBOL_TABLE.getInstance().inClassScope();
        TYPE res = null;
        if (currScope.equals("global")) {res = SYMBOL_TABLE.getInstance().findInstance(name);inGlobal = 1;}
        else if (className != null) {
            if (SYMBOL_TABLE.getInstance().inFuncScope()) {
                res = SYMBOL_TABLE.getInstance().findInFuncScope(name);
                if (res == null) {
                    res = SYMBOL_TABLE.getInstance().findInClassScope(name);
                    if (res == null) {
                        String fatherName = SYMBOL_TABLE.getInstance().findExtendsClass(className);
                        if (fatherName != null) {
                            TYPE_CLASS fatherClass = (TYPE_CLASS) SYMBOL_TABLE.getInstance().find(fatherName);
                            while (fatherClass != null) {
                                for (AST_ARG_LIST it = fatherClass.data_members; it != null; it = it.tail) {
                                    if (it.head.id.equals(name)) {String resName = it.head.type.typeName;return SYMBOL_TABLE.getInstance().find(resName);}
                                }
                                fatherClass = fatherClass.father;
                            }
                        }
                        res = SYMBOL_TABLE.getInstance().findInstance(name);
                        inGlobal = 1;
                    }
                }
            } else {
                res = SYMBOL_TABLE.getInstance().findInClassScope(name);
                if (res == null) {
                    String fatherName = SYMBOL_TABLE.getInstance().findExtendsClass(className);
                    if (fatherName != null) {
                        TYPE_CLASS fatherClass = (TYPE_CLASS) SYMBOL_TABLE.getInstance().find(fatherName);
                        while (fatherClass != null) {
                            for (AST_ARG_LIST it = fatherClass.data_members; it != null; it = it.tail) {
                                if (it.head.id.equals(name)) { String resName = it.head.type.typeName;return SYMBOL_TABLE.getInstance().find(resName);}
                            }
                            fatherClass = fatherClass.father;
                        }
                    }
                    res = SYMBOL_TABLE.getInstance().findInstance(name);
                    inGlobal = 1;
                }
            }
        }
        else if (SYMBOL_TABLE.getInstance().inFuncScope()) {
            res = SYMBOL_TABLE.getInstance().findInFuncScope(name);
            if (res == null) {res = SYMBOL_TABLE.getInstance().findInstance(name);inGlobal = 1;
            }
        }
        if (res == null) {System.out.println(">> ERROR[" + line + "] unknown variable");printError(line);}
        else if (res.isFunc()) {System.out.println(">> ERROR[" + line + "] variable name cant be function");printError(line);}
        thisT = res;
        return res;
    }

    public TEMP IRme() {
        System.out.format("VAR SIMPLE - IRme (%s)\n", name);
        TEMP t = TEMP_FACTORY.getInstance().getFreshTEMP();
        if (inGlobal == 1) {IR.getInstance().Add_IRcommand(new IRcommand_Load_Global(t, name));}
        else {
            String realN = name;
            boolean c = false;
            IRcommand command;
            if (className != null && offsets.get(name) == null) {c = true;realN = className + "_" + name;}
            if (c && (!(thisT instanceof TYPE_CLASS) || !(thisT.name.equals(className)))) {command = new IRcommand_Set_Dot(realN, t);((IRcommand_Set_Dot) command).cfg = cfgVar;}
            else {command = new IRcommand_Load_Local(realN, t);}
            command.offset = GetOffset(realN);
            IR.getInstance().Add_IRcommand(command);
        }
        return t;
    }
}