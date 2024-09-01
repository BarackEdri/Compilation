package AST;

import IR.*;
import TEMP.*;
import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_STMT_EXP_LIST extends AST_STMT {
    public String id;
    public AST_EXP_LIST2 list;
    public TYPE_FUNCTION func;

    public AST_STMT_EXP_LIST(String id, AST_EXP_LIST2 list, int line) {
        SerialNumber = AST_Node_Serial_Number.getFresh();
        this.id = id;
        this.list = list;
        this.line = line;
        System.out.print("stmt -> ID(expList);\n");
    }

    public void PrintMe() {
        System.out.format("AST STMT EXP LIST NODE\n");
        if (list != null) {list.PrintMe();}
        AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("STMT EXP LIST(%s)", id));
        if (list != null) {AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, list.SerialNumber);}
    }

    public TYPE SemantMe() {
        System.out.println("STMT EXP LIST - SemantMe");
        TYPE t = signature(id, list, this.line);
        this.func = (TYPE_FUNCTION) (SYMBOL_TABLE.getInstance().find(id));
        return t;
    }

    public TEMP IRme() {
        System.out.println("STMT EXP LIST - IRme");
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