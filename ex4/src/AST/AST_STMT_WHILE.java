package AST;

import IR.*;
import TEMP.*;
import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_STMT_WHILE extends AST_STMT {
    public AST_EXP cond;
    public AST_STMT_LIST body;
    public boolean inFunc;

    public AST_STMT_WHILE(AST_EXP cond, AST_STMT_LIST body, int line) {
        SerialNumber = AST_Node_Serial_Number.getFresh();
        this.cond = cond;
        this.body = body;
        this.line = line;
    }

    public void PrintMe() {
        System.out.format("AST STMT WHILE NODE\n");
        if (cond != null) cond.PrintMe();
        if (body != null) body.PrintMe();
        AST_GRAPHVIZ.getInstance().logNode(SerialNumber, "STMT WHILE");
        if (cond != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, cond.SerialNumber);
        if (body != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, body.SerialNumber);
    }

    public TYPE SemantMe() {
        System.out.format("STMT WHILE - SemantMe\n");
        if (cond.SemantMe() != TYPE_INT.getInstance()) {
            System.out.format(">> ERROR [%d:%d] condition inside WHILE is not integral\n", 2, 2);
            printError(this.line);
        }
        SYMBOL_TABLE.getInstance().beginScope("while");
        body.SemantMe();
        SYMBOL_TABLE.getInstance().endScope();
        inFunc = SYMBOL_TABLE.getInstance().inFuncScope();
        return TYPE_INT.getInstance();
    }

    public TEMP IRme() {
        System.out.format("STMT WHILE - IRme\n");
        String label_end = IRcommand.getFreshLabel("end");
        String label_start = IRcommand.getFreshLabel("start");
        IR.getInstance().Add_IRcommand(new IRcommand_Label(label_start));
        TEMP cond_temp = cond.IRme();
        IR.getInstance().Add_IRcommand(new IRcommand_Jump_beqz(cond_temp, label_end));
        if (inFunc) ifScope(body);
        else body.IRme();
        IR.getInstance().Add_IRcommand(new IRcommand_Jump_Label(label_start));
        IR.getInstance().Add_IRcommand(new IRcommand_Label(label_end));
        return null;
    }
}
