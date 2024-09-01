package AST;

import IR.*;
import TEMP.*;
import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_EXP_ID extends AST_EXP {
    public String id;
    public TYPE_FUNCTION func;

    public AST_EXP_ID(String id, int line) {
        SerialNumber = AST_Node_Serial_Number.getFresh();
        System.out.print("exp -> id()\n");
        this.id = id;
        this.line = line;
    }

    public void PrintMe() {
        System.out.format("AST EXP ID NODE\n");
        AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("EXP ID(%s)", id));
    }

    public TYPE SemantMe() {
        System.out.println("EXP ID - SemantMe");
        TYPE t = signature(id, null, this.line);
        TYPE foundId = SYMBOL_TABLE.getInstance().find(id);
        if (!foundId.isFunc()) {System.out.format(">> ERROR [%d] %s is not a function in this scope\n", line, id);printError(this.line);}
        this.func = (TYPE_FUNCTION) (SYMBOL_TABLE.getInstance().find(id));
        return t;
    }

    public TEMP IRme() {
        System.out.println("EXP ID - IRme");
        TEMP t = TEMP_FACTORY.getInstance().getFreshTEMP();
        String startLabel;
        if (id.equals("PrintInt")) {startLabel = "PrintInt";}
        else if (id.equals("PrintString")) {startLabel = "PrintString";}
        else {startLabel = this.func.startLabel;}
        IR.getInstance().Add_IRcommand(new IRcommand_Call_Func1(t, startLabel, null));
        return t;
    }
}
