package AST;

import IR.*;
import TEMP.*;
import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_STMT_ID extends AST_STMT {
    public String id;
    public TYPE_FUNCTION func;

    public AST_STMT_ID(String id, int line) {
        SerialNumber = AST_Node_Serial_Number.getFresh();
        this.id = id;
        this.line = line;
        System.out.print("stmt -> ID();\n");
    }

    public void PrintMe() {
        System.out.format("AST STMT ID NODE\n");
        AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("STMT ID(%s)", id));
    }

    public TYPE SemantMe() {
        System.out.println("STMT ID - SemantMe");
        TYPE t = signature(id, null, this.line);
        this.func = (TYPE_FUNCTION) (SYMBOL_TABLE.getInstance().find(id));
        return t;
    }

    public TEMP IRme() {
        System.out.println("STMT ID - IRme");
        TEMP t = TEMP_FACTORY.getInstance().getFreshTEMP();
        String startLabel;
        if (id.equals("PrintInt")) {startLabel = "PrintInt";} 
        else if (id.equals("PrintString")) {startLabel = "PrintString";}
        else {startLabel = this.func.startLabel;}
        IR.getInstance().Add_IRcommand(new IRcommand_Call_Func1(t, startLabel, null));
        return t;
    }
}