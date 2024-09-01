package AST;

import IR.*;
import TEMP.*;
import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_STMT_RETURN extends AST_STMT {

    public AST_STMT_RETURN(int line) {
        SerialNumber = AST_Node_Serial_Number.getFresh();
        this.line = line;
        System.out.print("stmt -> return;\n");
    }

    public void PrintMe() {
        System.out.format("AST STMT RETURN NODE\n");
        AST_GRAPHVIZ.getInstance().logNode(SerialNumber, "STMT RETURN");
    }

    public TYPE SemantMe() {
        System.out.println("STMT RETURN - SemantMe");
        int a = SYMBOL_TABLE.getInstance().findFunc("void");
        if (a == 0) {
            System.out.format("Error[%d] in return statement!", line);
            printError(line);
        }
        return TYPE_VOID.getInstance();
    }

    public TEMP IRme() {
        IR.getInstance().Add_IRcommand(new IRcommand_Set_Return(null));
        return null;
    }
}