package AST;

import IR.*;
import TEMP.*;
import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_STMT_EXP extends AST_STMT {
    public AST_EXP e;

    public AST_STMT_EXP(AST_EXP e, int line) {
        SerialNumber = AST_Node_Serial_Number.getFresh();
        this.e = e;
        this.line = line;
        System.out.print("stmt -> RETURN exp SEMICOLON\n");
    }

    public void PrintMe() {
        System.out.format("AST STMT EXP NODE\n");
        if (e != null) {e.PrintMe();}
        AST_GRAPHVIZ.getInstance().logNode(SerialNumber, "STMT EXP");
        if (e != null) {AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, e.SerialNumber);}
    }

    public TYPE SemantMe() {
        System.out.println("STMT EXP - SemantMe");
        TYPE ty = e.SemantMe();
        if (ty == null) {return null;}
        TYPE retTypeOfFunc = SYMBOL_TABLE.getInstance().getReturnTypeOfFunc();
        if (!isAssignable(retTypeOfFunc, ty)) {System.out.println("Error in return statement!");printError(line);}
        return ty;
    }

    public TEMP IRme() {
        TEMP retVal = e.IRme();
        IR.getInstance().Add_IRcommand(new IRcommand_Set_Return(retVal));
        return null;
    }
}