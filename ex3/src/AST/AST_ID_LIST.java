package AST;

import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_ID_LIST extends AST_Node {
    public AST_TYPE type;
    public AST_ID_LIST tail;
    public String name;

    public AST_ID_LIST(AST_TYPE type, String name, AST_ID_LIST tail) {

        SerialNumber = AST_Node_Serial_Number.getFresh();

        if (tail == null) { System.out.print("IDlist -> type ID\n"); }
        else { System.out.print("IDlist -> type IDlist\n"); }
        this.type=type;
        this.name=name;
        this.tail=tail;

    }

    public void PrintMe() {

        System.out.format("AST_ID_LIST");

        if (type != null) type.PrintMe();
        if (tail != null) tail.PrintMe();

        /**********************************/
        /* PRINT to AST GRAPHVIZ DOT file */
        /**********************************/
        AST_GRAPHVIZ.getInstance().logNode(SerialNumber, "ID_LIST");

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        if (type != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, type.SerialNumber);
        if (tail != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, tail.SerialNumber);
    }
    
    public TYPE SemantMe(){ return null; }
}
