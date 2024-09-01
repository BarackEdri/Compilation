package AST;

import IR.*;
import TEMP.*;
import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_EXP_LIST1 extends AST_EXP {
    public String id;
    public AST_EXP_LIST2 list;
    public TYPE_FUNCTION func;

    public AST_EXP_LIST1(String id, AST_EXP_LIST2 list, int line) {
        SerialNumber = AST_Node_Serial_Number.getFresh();
        this.line = line;
        System.out.print("exp -> ID (expList)\n");
        this.id = id;
        this.list = list;
    }

    public void PrintMe() {
        System.out.format("AST EXP LIST 1 NODE\n");
        if (list != null) list.PrintMe();
        AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("EXP LIST 1(%s)", id));
        if (list != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, list.SerialNumber);
    }

    public TYPE SemantMe() {
        System.out.println("EXP LIST 1 - SemantMe");
        TYPE t = signature(id, list, this.line);
        this.func = (TYPE_FUNCTION) (SYMBOL_TABLE.getInstance().find(id));
        return t;
    }

    public TEMP IRme() {
        System.out.println("EXP LIST 2 - IRme");
        TEMP t = TEMP_FACTORY.getInstance().getFreshTEMP();
        TEMP_LIST resTempsList = null;
        for (AST_EXP_LIST2 it = list; it != null; it = it.tail) {
            TEMP curr = it.head.IRme();
            resTempsList = new TEMP_LIST(curr, resTempsList);
        }
        if (resTempsList != null) {resTempsList = resTempsList.reverseList();}
        String startLabel;
        if (id.equals("PrintInt")) {startLabel = "PrintInt";}
        else if (id.equals("PrintString")) {startLabel = "PrintString";}
        else {startLabel = this.func.startLabel;}
        IR.getInstance().Add_IRcommand(new IRcommand_Call_Func1(t, startLabel, resTempsList));
        return t;
    }
}