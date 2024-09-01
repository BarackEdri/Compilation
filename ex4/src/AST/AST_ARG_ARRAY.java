package AST;

import SYMBOL_TABLE.*;
import TEMP.*;
import TYPES.*;

public class AST_ARG_ARRAY extends AST_Node {
    public AST_TYPE type;
    public String id;

    public AST_ARG_ARRAY(String id, AST_TYPE type, int line) {
        SerialNumber = AST_Node_Serial_Number.getFresh();
        this.type = type;
        this.id = id;
        this.line = line;
        System.out.format("arrayTypedef ::= array %s = type[]; \n", id);
    }

    public void PrintMe() {
        System.out.format("AST ARG ARRAY NODE\n");
        if (type != null){type.PrintMe();}
        AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("ARG ARRAY(%s)", id));
        if (type != null){AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, type.SerialNumber);}
    }

    public TYPE SemantMe() {
        System.out.println("ARG ARRAY - SemantMe");
        TYPE typeType = type.SemantMe();
        if (!(SYMBOL_TABLE.getInstance().getScope().equals("global"))) {
            System.out.format(">> ERROR [%d] array declared not in the global scope\n", line);
            printError(line);
        }
        if (typeType == null || typeType instanceof TYPE_VOID || typeType instanceof TYPE_NIL) {
            System.out.format(">> ERROR [%d] non existing type\n", line);
            printError(line);
        }
        if (SYMBOL_TABLE.getInstance().find(id) != null) {
            System.out.format(">> ERROR [%d] %s is already declared.\n", line, id);
            printError(line);
        }
        TYPE_ARRAY thisTYPE = new TYPE_ARRAY(typeType, id);
        SYMBOL_TABLE.getInstance().enter(id, thisTYPE);
        return thisTYPE;
    }

    public TEMP IRme() {
        System.out.println("ARG ARRAY - IRme");
        return null;
    }
}