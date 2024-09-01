package AST;

import TEMP.*;
import TYPES.*;

public class AST_DEC_LIST extends AST_Node {
    public AST_DEC head;
    public AST_DEC_LIST tail;

    public AST_DEC_LIST(AST_DEC head, AST_DEC_LIST tail) {
        SerialNumber = AST_Node_Serial_Number.getFresh();
        if (tail != null){System.out.print("decs -> dec decs\n");}
        if (tail == null){System.out.print("decs -> dec\n");}
        this.head = head;
        this.tail = tail;
    }

    public void PrintMe() {
        System.out.format("AST DEC LIST NODE\n");
        if (head != null) head.PrintMe();
        if (tail != null) tail.PrintMe();
        AST_GRAPHVIZ.getInstance().logNode(SerialNumber, "DEC LIST");
        if (head != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, head.SerialNumber);
        if (tail != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, tail.SerialNumber);

    }

    public TYPE SemantMe() {
        System.out.println("DEC LIST - SemantMe");
        if (head != null) {head.SemantMe();}
        if (tail != null) {tail.SemantMe();}
        return null;
    }

    public TEMP IRme() {
        System.out.println("DEC LIST - IRme");
        if (head != null) head.IRme();
        if (tail != null) tail.IRme();
        return null;
    }
}
