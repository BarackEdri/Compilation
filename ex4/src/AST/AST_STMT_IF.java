package AST;

import IR.*;
import TEMP.*;
import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_STMT_IF extends AST_STMT {
    public AST_EXP cond;
    public AST_STMT_LIST body;
    public boolean infunc;

    public AST_STMT_IF(AST_EXP cond, AST_STMT_LIST body, int line) {
        SerialNumber = AST_Node_Serial_Number.getFresh();
        this.cond = cond;
        this.body = body;
        this.line = line;
    }

    public void PrintMe() {
        System.out.format("AST STMT IF NODE\n");
        if (cond != null) cond.PrintMe();
        if (body != null) body.PrintMe();
        AST_GRAPHVIZ.getInstance().logNode(SerialNumber, "STMT IF");
        if (cond != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, cond.SerialNumber);
        if (body != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, body.SerialNumber);
    }

    public TYPE SemantMe() {
        System.out.format("STMT IF - SemantMe\n");
        if (cond.SemantMe() != TYPE_INT.getInstance()) {
            System.out.format(">> ERROR [%d:%d] condition inside IF is not integral\n", 2, 2);
            printError(this.line);
        }
        SYMBOL_TABLE.getInstance().beginScope("if");
        body.SemantMe();
        SYMBOL_TABLE.getInstance().endScope();
        infunc = SYMBOL_TABLE.getInstance().inFuncScope();
        return TYPE_INT.getInstance();
    }

    public TEMP IRme() {
        System.out.format("STMT IF - IRme\n");
        String label_end = IRcommand.getFreshLabel("end");
        String label_start = IRcommand.getFreshLabel("start");
        IR.getInstance().Add_IRcommand(new IRcommand_Label(label_start));
        TEMP cond_temp = cond.IRme();
        IR.getInstance().Add_IRcommand(new IRcommand_Jump_beqz(cond_temp, label_end));
        if (infunc) ifScope(body);
        else body.IRme();
        IR.getInstance().Add_IRcommand(new IRcommand_Label(label_end));
        return null;
    }
}
