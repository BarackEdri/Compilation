package AST;

import IR.*;
import TEMP.*;
import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_TYPE_ID extends AST_TYPE {

    public AST_TYPE_ID(String id, int line) {
        SerialNumber = AST_Node_Serial_Number.getFresh();
        this.typeName = id;
        this.line = line;
        System.out.print("type -> ID\n");
    }

    public void PrintMe() {
        System.out.format("AST TYPE ID NODE\n");
        AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("%s", typeName));
    }

    public TYPE SemantMe() {
        System.out.println("TYPE ID - SemantMe");
        TYPE res = findType(typeName);
        if (res == null) {
            System.out.format(">> ERROR(%d) non existing type %s (type_id)\n", line, res);
            printError(this.line);
        }
        if (!res.name.equals(typeName)) {
            System.out.format(">> ERROR [%d]- type name isn't declared correctly! %s %s", line, res.name, typeName);
            printError(this.line);
        }
        return res;
    }
}