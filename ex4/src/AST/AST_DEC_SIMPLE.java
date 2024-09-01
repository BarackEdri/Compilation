package AST;

import java.util.*;

import IR.*;
import TEMP.*;
import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_DEC_SIMPLE extends AST_DEC_CLASS1 {

    public AST_DEC_SIMPLE(String id, AST_CFIELD_LIST data_members, int line) {
        SerialNumber = AST_Node_Serial_Number.getFresh();
        this.data_members = data_members;
        this.id = id;
        this.line = line;
        this.father = null;
        if (data_members != null) {System.out.format("classDec -> CLASS ID:%s LBRACE cFieldList RBRACE\n", id);}
    }

    public void PrintMe() {
        System.out.format("AST DEC SIMPLE NODE\n");
        if (data_members != null) {data_members.PrintMe();}
        AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("DEC SIMPLE(%s)", id));
        if (data_members != null) {AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, data_members.SerialNumber);}
    }

    public TYPE SemantMe() {
        System.out.format("DEC SIMPLE - SemantMe\n");
        TYPE isExist = SYMBOL_TABLE.getInstance().findInCurrScope(id);
        if (isExist != null) {System.out.format(">> ERROR [%d] already exist a variable with the (class) name " + id + " in the same scope", line);printError(line);}
        SYMBOL_TABLE.getInstance().beginScope("class-" + id);
        AST_ARG_LIST fields = null;
        AST_TYPE_LIST funcs = null;
        TYPE t;
        for (AST_CFIELD_LIST it = data_members; it != null; it = it.tail) {
            t = it.head.SemantMe();
            AST_TYPE currType;
            if (it.head instanceof AST_CFIELD_VAR) {
                switch (t.name) {
                    case "int": {currType = new AST_TYPE_INT(line);break;}
                    case "string": {currType = new AST_TYPE_STRING(line);break;}
                    case "void": {System.out.println(">> ERROR [" + line + "] void variable is illegal");printError(line);}
                    default: {currType = new AST_TYPE_ID(t.name, line);break;}
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
        SYMBOL_TABLE.getInstance().beginScope("class-" + id);
        for (AST_CFIELD_LIST it = data_members; it != null; it = it.tail) {it.head.SemantMe();}
        SYMBOL_TABLE.getInstance().endScope();
        return null;
    }

    public TEMP IRme() {
        ArrayList<ArrayList<String>> funclist = new ArrayList<>();
        Map<String, Integer> funcOff = new HashMap<>();
        int fieldCnt = 0, funcCnt = 0;
        for (AST_CFIELD_LIST it = data_members; it != null; it = it.tail) {
            AST_CFIELD field = it.head;
            if (field instanceof AST_CFIELD_VAR) {fieldCnt += 1;continue;}
            if (field instanceof AST_CFIELD_FUNC) {
                AST_CFIELD_FUNC cf = (AST_CFIELD_FUNC) field;
                AST_DEC_FUNC2 cf2 = cf.func;
                offsets.put(id + "_" + cf2.id, id + "_" + cf2.id);
            }
            AST_CFIELD_FUNC cf = null;
            if (field instanceof AST_CFIELD_FUNC) {cf = (AST_CFIELD_FUNC) field;}
            AST_DEC_FUNC2 cf2 = null;
            if (cf != null) {cf2 = cf.func;}
            ArrayList<String> function = new ArrayList<>();
            if (cf2 != null) {function.add(cf2.id);}
            function.add(id);
            funclist.add(function);
            if (cf2 != null) {funcOff.put(cf2.id, funcCnt * 4);}
            funcCnt += 1;
        }
        classFuncsOff.put(id, funcOff);
        ArrayList<ArrayList<ArrayList<String>>> fields = new ArrayList<>();
        fieldCnt = 0;
        ArrayList<String> fieldslist = new ArrayList<>();
        for (AST_CFIELD_LIST it = data_members; it != null; it = it.tail) {
            AST_CFIELD field = it.head;
            if (field instanceof AST_CFIELD_VAR) {
                AST_CFIELD_VAR var = (AST_CFIELD_VAR) field;
                AST_VAR_DEC cf2 = var.varDec;
                String off = String.valueOf(fieldCnt * 4 + 4);
                offsets.put(id + "_" + cf2.id, off);
                fieldslist.add(cf2.id);
                fieldCnt += 1;
                ArrayList<String> field1 = new ArrayList<>();
                field1.add(cf2.id);
                if (cf2.type instanceof AST_TYPE_STRING) field1.add("1");
                else field1.add("0");
                ArrayList<ArrayList<String>> fieldandclass = new ArrayList<>();
                fieldandclass.add(field1);
                ArrayList<String> classname = new ArrayList<>();
                classname.add(id);
                fieldandclass.add(classname);
                fields.add(fieldandclass);
            }
        }
        for (AST_CFIELD_LIST it = data_members; it != null; it = it.tail) {
            if (it.head instanceof AST_CFIELD_FUNC) it.head.IRme();
        }
        classSize.put(id, fields.size() * 4 + 4);
        IR.getInstance().Add_IRcommand(new IRcommand_Call_Class(id, funclist));
        for (AST_CFIELD_LIST it = data_members; it != null; it = it.tail) {
            if (it.head instanceof AST_CFIELD_VAR) it.head.IRme();
        }
        classfields.put(id, fieldslist);
        return null;
    }
}