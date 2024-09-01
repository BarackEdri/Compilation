package SYMBOL_TABLE;

import java.io.*;
import java.util.*;

import AST.*;
import TYPES.*;

public class SYMBOL_TABLE {
    public static int n = 0;
    private int top_index = 0;
    private int hashArraySize = 13;
    private SYMBOL_TABLE_ENTRY top;
    private static SYMBOL_TABLE instance = null;
    private SYMBOL_TABLE_ENTRY[] table = new SYMBOL_TABLE_ENTRY[hashArraySize];
    
    protected SYMBOL_TABLE() {}

    public static SYMBOL_TABLE getInstance() {
        if (instance == null) {
            instance = new SYMBOL_TABLE();
            instance.enter("int",TYPE_INT.getInstance());
            instance.enter("string",TYPE_STRING.getInstance());
            instance.enter("void", TYPE_VOID.getInstance());
            instance.enter("PrintInt",new TYPE_FUNCTION(TYPE_VOID.getInstance(),"PrintInt",new TYPE_LIST(TYPE_INT.getInstance(),null)));
            instance.enter("PrintString",new TYPE_FUNCTION(TYPE_VOID.getInstance(),"PrintString",new TYPE_LIST(TYPE_STRING.getInstance(),null)));
        }
        return instance;
    }
    
    public void PrintMe() {
        int i = 0;
        int j = 0;
        String dirname = "./output/";
        String filename = String.format("SYMBOL_TABLE_%d_IN_GRAPHVIZ_DOT_FORMAT.txt", n++);
        try {
            PrintWriter fileWriter = new PrintWriter(dirname + filename);
            fileWriter.print("digraph structs {\n");
            fileWriter.print("rankdir = LR\n");
            fileWriter.print("node [shape=record];\n");
            fileWriter.print("hashTable [label=\"");
            for (i = 0; i < hashArraySize - 1; i++) {fileWriter.format("<f%d>\n%d\n|", i, i);}
            fileWriter.format("<f%d>\n%d\n\"];\n", hashArraySize - 1, hashArraySize - 1);
            for (i = 0; i < hashArraySize; i++) {
                if (table[i] != null) {fileWriter.format("hashTable:f%d -> node_%d_0:f0;\n", i, i);}
                j = 0;
                for (SYMBOL_TABLE_ENTRY it = table[i]; it != null; it = it.next) {
                    fileWriter.format("node_%d_%d ", i, j);
                    fileWriter.format("[label=\"<f0>%s|<f1>%s|<f2>prevtop=%d|<f3>next\"];\n",it.name,it.type.name,it.prevtop_index);
                    if (it.next != null) {
                        fileWriter.format("node_%d_%d -> node_%d_%d [style=invis,weight=10];\n",i,j,i,j+1);
                        fileWriter.format("node_%d_%d:f3 -> node_%d_%d:f0;\n",i,j,i,j+1);
                    }
                    j++;
                }
            }
            fileWriter.print("}\n");
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int hash(String s) {
        if (s.charAt(0) == 'l') {return 1;}
        if (s.charAt(0) == 'm') {return 1;}
        if (s.charAt(0) == 'r') {return 3;}
        if (s.charAt(0) == 'i') {return 6;}
        if (s.charAt(0) == 'd') {return 6;}
        if (s.charAt(0) == 'k') {return 6;}
        if (s.charAt(0) == 'f') {return 6;}
        if (s.charAt(0) == 'S') {return 6;}
        return 12;
    }

    public void enter(String name, TYPE t) {
        int hashValue = hash(name);
        SYMBOL_TABLE_ENTRY next = table[hashValue];
        SYMBOL_TABLE_ENTRY e = new SYMBOL_TABLE_ENTRY(name,t,hashValue,next,top,top_index++);
        top = e;
        table[hashValue] = e;
        PrintMe();
    }

    public void enterClassDec(String name, TYPE t) {
        int hashValue = hash(name);
        SYMBOL_TABLE_ENTRY next = table[hashValue];
        SYMBOL_TABLE_ENTRY e = new SYMBOL_TABLE_ENTRY(name, t, hashValue, next, top, top_index++);
        e.isInstance = false;
        top = e;
        table[hashValue] = e;
        PrintMe();
    }

    public TYPE find(String name) {
        SYMBOL_TABLE_ENTRY e;
        for (e = table[hash(name)]; e != null; e = e.next) {if (name.equals(e.name)) {return e.type;}}
        return null;
    }

    public TYPE findInstance(String name) {
        SYMBOL_TABLE_ENTRY e;
        for (e = table[hash(name)]; e != null; e = e.next) {if (name.equals(e.name) && e.isInstance) {return e.type;}}
        return null;
    }

    public String findExtendsClass(String className) {
        SYMBOL_TABLE_ENTRY curr = top;
        while (curr != null) {
            if (curr.name.startsWith("SCOPE-BOUNDARY-class")) {
                String[] splited = curr.name.split("-");
                if (splited.length > 5 && splited[3].equals(className)){return curr.name.split("-")[5];}
                return null;
            }
            curr = curr.prevtop;
        }
        return null;
    }

    public TYPE findInFuncScope(String name) {
        SYMBOL_TABLE_ENTRY curr = top;
        while (curr != null && !curr.name.startsWith("SCOPE-BOUNDARY-func")) {
            if (curr.name.equals(name)) {return curr.type;}
            curr = curr.prevtop;
        }
        return null;
    }

    public TYPE findInClassScope(String name) {
        SYMBOL_TABLE_ENTRY curr = top;
        TYPE res = null;
        while (curr != null && !curr.name.startsWith("SCOPE-BOUNDARY-class")) {
            if (curr.name.equals(name)) {res = curr.type;}
            curr = curr.prevtop;
        }
        return res;
    }

    public TYPE findInCurrScope(String name) {
        SYMBOL_TABLE_ENTRY curr = top;
        TYPE res = null;
        while (curr != null && !curr.name.startsWith("SCOPE-BOUNDARY")) {
            if (curr.name.equals(name)) {res = curr.type;}
            curr = curr.prevtop;
        }
        return res;
    }

    public int findFunc(String returnType) {
        SYMBOL_TABLE_ENTRY a = top;
        AST_TYPE_VOID af = new AST_TYPE_VOID(-1);
        while (a != null) {
            if (a.name.startsWith("SCOPE-BOUNDARY-func")) {
                String[] splited = a.name.split("-");
                if (splited[splited.length - 1].equals(returnType)){return 1;}
                TYPE name = af.findType(returnType);
                if (name.isClass()) {
                    TYPE_CLASS fa = ((TYPE_CLASS) name).father;
                    while (fa != null) {
                        if (fa.name.equals(splited[splited.length - 1])){return 1;}
                        fa = fa.father;
                    }
                    String f = findExtendsClass(name.name);
                    TYPE_CLASS ex = (TYPE_CLASS) SYMBOL_TABLE.getInstance().find(f);
                    fa = ex.father;
                    while (fa != null) {
                        if (fa.name.equals(splited[splited.length - 1])){return 1;}
                            fa = fa.father;
                    }
                }
            }
            if (a.name.startsWith("SCOPE-BOUNDARY-class")){return 0;}
            a = a.prevtop;
        }
        return 0;
    }

    public TYPE findClass(String name) {
        SYMBOL_TABLE_ENTRY a = top;
        while (a.name != null) {
            if (a.name.equals(name)){return a.type;}
            if (a.name.startsWith("SCOPE-BOUNDARY-class")){if (a.name.split("-")[3].equals(name)){return new TYPE_CLASS(null, name, null, null);}}
            a = a.prevtop;
        }
        return null;
    }

    public void beginScope(String name) {
        String boundary = "SCOPE-BOUNDARY-" + name;
        enter(boundary,new TYPE_FOR_SCOPE_BOUNDARIES("NONE"));
        PrintMe();
    }

    public void endScope() {
        while (!(top.name.startsWith("SCOPE-BOUNDARY"))) {
            table[top.index] = top.next;
            top_index = top_index - 1;
            top = top.prevtop;
        }
        table[top.index] = top.next;
        top_index = top_index - 1;
        top = top.prevtop;
        PrintMe();
    }
        
    public String getScope() {
        SYMBOL_TABLE_ENTRY curr = top;
        while (curr != null) {
            if (curr.name.startsWith("SCOPE-BOUNDARY")) {return curr.name.split("-")[2];}
            curr = curr.prevtop;
        }
        return "global";
    }

    public String inClassScope() {
        SYMBOL_TABLE_ENTRY curr = top;
        while (curr != null) {
            if (curr.name.startsWith("SCOPE-BOUNDARY-class")){return curr.name.split("-")[3];}
            curr = curr.prevtop;
        }
        return null;
    }

    public boolean inFuncScope() {
        SYMBOL_TABLE_ENTRY curr = top;
        while (curr != null) {
            if (curr.name.startsWith("SCOPE-BOUNDARY-func")){return true;}
            curr = curr.prevtop;
        }
        return false;
    }

    public TYPE getReturnTypeOfFunc() {
        String retTypeName = null;
        SYMBOL_TABLE_ENTRY a = top;
        AST_TYPE_VOID af = new AST_TYPE_VOID(-1);
        while (a != null) {
            if (a.name.startsWith("SCOPE-BOUNDARY-func")) {
                String[] splited = a.name.split("-");
                retTypeName = splited[splited.length - 1];
                return af.findType(retTypeName);
            }
            if (a.name.startsWith("SCOPE-BOUNDARY-class")){return null;}
            a = a.prevtop;
        }
        return null;
    }

    public TYPE isRealFunc(String name, AST_EXP_LIST2 calledFuncParamExpList) {
        SYMBOL_TABLE_ENTRY currSymbol = top;
        while (currSymbol != null && currSymbol.name != null) {
            if (currSymbol.type.isFunc()) {
                if (!(currSymbol.name.equals(name))) {currSymbol = currSymbol.prevtop;continue;}
                TYPE_LIST declaredFuncParams = ((TYPE_FUNCTION) currSymbol.type).params;
                for (AST_EXP_LIST2 currCalledParam = calledFuncParamExpList; currCalledParam != null; currCalledParam = currCalledParam.tail) {
                    if (declaredFuncParams == null){return null;}
                    TYPE currDeclaredParam = declaredFuncParams.head;
                    if (currDeclaredParam == null){return null;}
                    TYPE calledFuncParam = currCalledParam.head.SemantMe();
                    if (calledFuncParam.name.equals("nil") && (currDeclaredParam.isClass() || currDeclaredParam.isArray())) {declaredFuncParams = declaredFuncParams.tail;continue;}
                    if (Objects.equals(calledFuncParam.name, currDeclaredParam.name)) {declaredFuncParams = declaredFuncParams.tail;continue;}
                    boolean isGoodParam = false;
                    if (calledFuncParam.isClass() && currDeclaredParam.isClass()) {
                        TYPE_CLASS father = ((TYPE_CLASS) calledFuncParam).father;
                        while (father != null) {if (father.name.equals(currDeclaredParam.name)) {isGoodParam = true;break;}father = father.father;}
                        if (isGoodParam) {declaredFuncParams = declaredFuncParams.tail;continue;}
                    }
                    return null;
                }
                if (declaredFuncParams == null || declaredFuncParams.head == null){return ((TYPE_FUNCTION) currSymbol.type).returnType;}
                return null;
            }
            currSymbol = currSymbol.prevtop;
        }
        return null;
    }

    public TYPE compareFuncs(TYPE_FUNCTION realFunc, AST_EXP_LIST2 params, int line) {
        TYPE_LIST args = realFunc.params;
        for (AST_EXP_LIST2 it = params; it != null; it = it.tail) {
            if (args == null){return null;}
            TYPE argType = args.head;
            if (argType == null){return null;}
            TYPE paramType = it.head.SemantMe();
            if (!(params.type_equals(argType, paramType))){return null;}
            args = args.tail;
        }
        if (args == null || args.head == null){return realFunc.returnType;}
        return null;
    }

    public void cleanGarbage() {
        while (!(top.name.startsWith("SCOPE-BOUNDARY-class"))) {
            table[top.index] = top.next;
            top_index = top_index - 1;
            top = top.prevtop;
        }
        table[top.index] = top.next;
        top_index = top_index - 1;
        top = top.prevtop;
    }
}
