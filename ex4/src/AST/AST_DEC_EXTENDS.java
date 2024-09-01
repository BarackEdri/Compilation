package AST;

import java.util.*;

import IR.*;
import TEMP.*;
import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_DEC_EXTENDS extends AST_DEC_CLASS1 {
    String fatherName;

    public AST_DEC_EXTENDS(String classId, String fatherClassId, AST_CFIELD_LIST classFieldList, int line) {
        this.id = classId;
        this.father = (TYPE_CLASS) SYMBOL_TABLE.getInstance().find(fatherClassId);
        this.fatherName = fatherClassId;
        this.data_members = classFieldList;
        this.line = line;
        System.out.format("classDec -> CLASS ID:%s EXTENDS ID:%s LBRACE cFieldList:cl RBRACE\n", classId, fatherClassId);
        SerialNumber = AST_Node_Serial_Number.getFresh();
    }

    public void PrintMe() {
        System.out.format("AST DEC EXTENDS NODE\n");
        if (data_members != null) data_members.PrintMe();
        AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("DEC(%s) EXTENDS(%s)", id, father));
        if (data_members != null) {AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, data_members.SerialNumber);}
    }

    public TYPE SemantMe() {
        System.out.format("DEC EXTENDS - SemantMe\n");
        this.father = (TYPE_CLASS) SYMBOL_TABLE.getInstance().find(this.fatherName);
        if (this.father == null) {
            System.out.format(">> ERROR [%d] cannot extend " + this.fatherName + " because it isn't a known class ", line);
            printError(line);
        }
        TYPE isExist = SYMBOL_TABLE.getInstance().findInCurrScope(id);
        if (isExist != null) {
            System.out.format(">> ERROR [%d] already exist a variable with the (class) name " + id + " in the same scope", line);
            printError(line);
        }
        SYMBOL_TABLE.getInstance().beginScope("class-" + id + "-extends-" + this.father.name);
        AST_ARG_LIST fields = null;
        AST_TYPE_LIST funcs = null;
        TYPE t;
        for (AST_CFIELD_LIST it = data_members; it != null; it = it.tail) {
            System.out.println("BEFORE");
            t = it.head.SemantMe();
            System.out.println("AFTER");
            AST_TYPE currType;
            if (it.head instanceof AST_CFIELD_VAR) {
                switch (t.name) {
                    case "int": {
                        currType = new AST_TYPE_INT(line);
                        break;
                    }
                    case "string": {
                        currType = new AST_TYPE_STRING(line);
                        break;
                    }
                    case "void": {
                        System.out.println(">> ERROR [" + line + "] void variable is illegal");
                        printError(line);
                    }
                    default: {
                        currType = new AST_TYPE_ID(t.name, line);
                        break;
                    }
                }
                AST_ARG curr = new AST_ARG(currType, ((AST_CFIELD_VAR) it.head).varDec.id);
                fields = new AST_ARG_LIST(curr, fields);
            }
            if (it.head instanceof AST_CFIELD_FUNC) {
                AST_TYPE_NAME curr = new AST_TYPE_NAME(t, ((AST_CFIELD_FUNC) it.head).func.id);
                funcs = new AST_TYPE_LIST(curr, funcs);
            }
        }
        SYMBOL_TABLE.getInstance().cleanGarbage();
        TYPE_CLASS classType = new TYPE_CLASS(father, id, fields, funcs);
        SYMBOL_TABLE.getInstance().enterClassDec(id, classType);
        SYMBOL_TABLE.getInstance().beginScope("class-" + id + "-extends-" + this.father.name);
        for (AST_CFIELD_LIST it = data_members; it != null; it = it.tail) {
            it.head.SemantMe(); // enter real values to the stack
        }
        SYMBOL_TABLE.getInstance().endScope();
        return null;
    }

    public TEMP IRme() {
        TYPE_CLASS fatherClass = father;
        boolean f = false;
        ArrayList<ArrayList<ArrayList<String>>> fieldlist = new ArrayList<>();
        ArrayList<ArrayList<String>> funclist = new ArrayList<>();
        while (fatherClass != null) {
            for (AST_TYPE_LIST it = fatherClass.functions; it != null; it = it.tail) {
                int n = funclist.size();
                for (int i = 0; i < n; i++) {
                    f = false;
                    if (((funclist.get(i)).get(0)).equals(it.head.name)) {
                        ArrayList<String> temp = funclist.get(i);
                        funclist.remove(i);
                        funclist.add(0, temp);
                        break;
                    }
                }
                if (!f) {
                    ArrayList<String> funcOfFather = new ArrayList<>();
                    funcOfFather.add(it.head.name);
                    funcOfFather.add(fatherClass.name);
                    funclist.add(0, funcOfFather);
                }
            }
            for (AST_ARG_LIST it = fatherClass.data_members; it != null; it = it.tail) {
                f = false;
                int n = fieldlist.size();
                for (int i = 0; i < n; i++) {
                    if ((((fieldlist.get(i)).get(0)).get(0)).equals(it.head.id)) {
                        f = true;
                        ArrayList<ArrayList<String>> temp = fieldlist.get(i);
                        fieldlist.remove(i);
                        fieldlist.add(0, temp);
                        break;
                    }
                }
                if (f) continue;
                ArrayList<String> fieldOfFather = new ArrayList<>();
                fieldOfFather.add(it.head.id);
                if (it.head.type instanceof AST_TYPE_STRING) fieldOfFather.add("1");
                else fieldOfFather.add("0");
                ArrayList<ArrayList<String>> field = new ArrayList<>();
                field.add(fieldOfFather);
                ArrayList<String> fatherarray = new ArrayList<>();
                fatherarray.add(fatherClass.name);
                field.add(fatherarray);
                fieldlist.add(0, field);
            }
            fatherClass = fatherClass.father;
        }
        for (AST_CFIELD_LIST it = data_members; it != null; it = it.tail) {
            AST_CFIELD field = it.head;
            if (field instanceof AST_CFIELD_VAR) {
                f = false;
                AST_CFIELD_VAR a = (AST_CFIELD_VAR) field;
                AST_VAR_DEC b = a.varDec;
                for (ArrayList<ArrayList<String>> arrayLists : fieldlist) {
                    if (((arrayLists.get(0)).get(0)).equals(b.id)) {
                        f = true;
                        (arrayLists.get(1)).set(0, id);
                        break;
                    }
                }
                if (!f) {
                    ArrayList<String> fieldOfMe = new ArrayList<>();
                    fieldOfMe.add(b.id);
                    if (b.type instanceof AST_TYPE_STRING) fieldOfMe.add("1");
                    else fieldOfMe.add("0");
                    ArrayList<ArrayList<String>> field2 = new ArrayList<>();
                    field2.add(fieldOfMe);
                    ArrayList<String> mearray = new ArrayList<>();
                    mearray.add(id);
                    field2.add(mearray);
                    fieldlist.add(field2);
                }
                continue;
            }
            if (field instanceof AST_CFIELD_FUNC) {
                f = false;
                AST_CFIELD_FUNC a = (AST_CFIELD_FUNC) field;
                AST_DEC_FUNC2 b = a.func;
                int n = funclist.size();
                for (ArrayList<String> strings : funclist) {
                    if ((strings.get(0)).equals(b.id)) {
                        f = true;
                        strings.set(1, id);
                        break;
                    }
                }
                if (!f) {
                    ArrayList<String> funcOfMe = new ArrayList<>();
                    funcOfMe.add(b.id);
                    funcOfMe.add(id);
                    funclist.add(funcOfMe);
                }

            }
        }
        int fieldCnt = 0;
        Map<String, Integer> funcOff = new HashMap<>();
        ArrayList<String> fields = new ArrayList<>();
        for (ArrayList<ArrayList<String>> lists : fieldlist) {
            // fieldCnt += 1;
            String off = String.valueOf(fieldCnt * 4 + 4);
            offsets.put(id + "_" + ((lists.get(0)).get(0)), off);
            fieldCnt += 1;
            fields.add((lists.get(0)).get(0));
        }
        for (int i = 0; i < funclist.size(); i++) {
            offsets.put(id + "_" + ((funclist.get(i)).get(0)), ((funclist.get(i)).get(1)) + "_" + ((funclist.get(i)).get(0)));
            funcOff.put(((funclist.get(i)).get(0)), i * 4);
        }
        classFuncsOff.put(id, funcOff);
        for (AST_CFIELD_LIST it = data_members; it != null; it = it.tail) {
            if (it.head instanceof AST_CFIELD_FUNC) it.head.IRme();
        }
        IR.getInstance().Add_IRcommand(new IRcommand_Call_Class(id, funclist));
        AST_CFIELD_LIST temp = data_members;
        for (ArrayList<ArrayList<String>> arrayLists : fieldlist) {
            String tf = arrayLists.get(1).get(0);
            String n = arrayLists.get(0).get(0);
            if (!(tf.equals(id))) {
                String v = defaultFields.get(tf + "_" + n);
                if (v == null) IR.getInstance().Add_IRcommand(new IRcommand_Call_Object(id + "_" + n));
                else if (arrayLists.get(0).get(1).equals("1")) IR.getInstance().Add_IRcommand(new IRcommand_Call_String(id + "_" + n, v));
                else IR.getInstance().Add_IRcommand(new IRcommand_Call_Int(id + "_" + n, Integer.parseInt(v)));
            } else {
                for (AST_CFIELD_LIST it = temp; it != null; it = it.tail) {
                    if (it.head instanceof AST_CFIELD_VAR && ((AST_CFIELD_VAR) it.head).varDec.id.equals(n)) {
                        it.head.IRme();
                        temp = data_members;
                        break;
                    }
                }
            }
        }
        classSize.put(id, fieldlist.size() * 4 + 4);
        classfields.put(id, fields);
        return null;
    }
}
