package AST;

import IR.*;
import TEMP.*;
import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_ARG_BINOP extends AST_Node {
    public AST_EXP left;
    public AST_EXP right;
    public TYPE leftType;
    int number;

    public AST_ARG_BINOP(int number, AST_EXP left, AST_EXP right, int line) {
        this.number = number;
        this.left = left;
        this.right = right;
        this.line = line;
        SerialNumber = AST_Node_Serial_Number.getFresh();
    }

    public void PrintMe() {
        System.out.format("AST ARG BINOP NODE\n");
        String s = "";
        switch (number) {
            case 0:
                s = "+";
            case 1:
                s = "-";
            case 2:
                s = "*";
            case 3:
                s = "/";
            case 4:
                s = ">";
            case 5:
                s = "<";
            case 6:
                s = "=";
        }
        if (left != null) left.PrintMe();
        if (right != null) right.PrintMe();
        if (left != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, left.SerialNumber);
        AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("%s", s));
        if (right != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, right.SerialNumber);
    }

    public TYPE SemantMe() {
        System.out.format("ARG BINOP - SemantMe\n");
        TYPE leftType = null;
        TYPE rightType = null;
        if (left != null) leftType = left.SemantMe();
        if (right != null) rightType = right.SemantMe();
        if (leftType == null || rightType == null || left == null || right == null) {
            System.out.format(">> ERROR [%d] BINOP has invalid arguments", line);
            printError(line);
            return null;
        }
        this.leftType = leftType;
        if (number == 6) {
            if (leftType == TYPE_VOID.getInstance() || rightType == TYPE_VOID.getInstance()) {
                System.out.format(">> ERROR [%d] can't compare void types", line);
                printError(line);
            }
            if (type_equals(leftType, rightType) || type_equals(rightType, leftType)) return TYPE_INT.getInstance();
            if (isExtendingClass(leftType, rightType) || isExtendingClass(rightType, leftType)) return TYPE_INT.getInstance();
            if ((leftType.isClass() || leftType.isArray()) && rightType instanceof TYPE_NIL) return TYPE_INT.getInstance();
            if ((rightType.isClass() || rightType.isArray()) && leftType instanceof TYPE_NIL) return TYPE_INT.getInstance();
            if ((leftType == TYPE_INT.getInstance()) || (rightType == TYPE_STRING.getInstance()) || (leftType == TYPE_STRING.getInstance()) || (rightType == TYPE_INT.getInstance())) {
                System.out.format(">> ERROR [%d] can't compare primitive to non-primitive", line);
                printError(line);
            }
            System.out.format(">> ERROR [%d] can't compare different types", line);
            printError(line);
        }
        if (number == 0) {
            if ((leftType == TYPE_INT.getInstance()) && (rightType == TYPE_INT.getInstance())) return TYPE_INT.getInstance();
            if ((leftType == TYPE_STRING.getInstance()) && (rightType == TYPE_STRING.getInstance())) return TYPE_STRING.getInstance();
            System.out.format(String.format(">> ERROR [%d] cannot add (+) non int/string types", line));
            printError(line);
        }
        if ((leftType != TYPE_INT.getInstance()) || (rightType != TYPE_INT.getInstance())) {
            System.out.format(">> ERROR [%d] trying binop between wrong types %s %s", line, leftType.name, rightType.name);
            printError(line);
        }
        if ((right instanceof AST_EXP_INT) && ((AST_EXP_INT) right).value == 0 && number == 3) {
            System.out.format(">> ERROR [%d] division by 0", line);
            printError(line);
        }
        return TYPE_INT.getInstance();
    }

    public TEMP IRme() {
        System.out.println("ARG BINOP - IRme");
        TEMP leftIR = null;
        TEMP rightIR = null;
        if (left != null) leftIR = left.IRme();
        if (right != null) rightIR = right.IRme();
        TEMP dst = TEMP_FACTORY.getInstance().getFreshTEMP();
        if (number == 0) {
            if (leftType == TYPE_STRING.getInstance()) {IR.getInstance().Add_IRcommand(new IRcommand_Binop_Add_Strings(dst, leftIR, rightIR));}
            else {IR.getInstance().Add_IRcommand(new IRcommand_Binop_Add_Integers(dst, leftIR, rightIR));}
        }
        if (number == 2) {IR.getInstance().Add_IRcommand(new IRcommand_Binop_Mul_Integers(dst, leftIR, rightIR));}
        if (number == 6) {
            if (leftType == TYPE_STRING.getInstance()) { IR.getInstance().Add_IRcommand(new IRcommand_Binop_EQ_Strings(dst, leftIR, rightIR));}
            else IR.getInstance().Add_IRcommand(new IRcommand_Binop_EQ_Integers(dst, leftIR, rightIR));
        }
        if (number == 5) {IR.getInstance().Add_IRcommand(new IRcommand_Binop_LT_Integers(dst, leftIR, rightIR));}
        if (number == 4) {IR.getInstance().Add_IRcommand(new IRcommand_Binop_GT_Integers(dst, leftIR, rightIR));}
        if (number == 1) {IR.getInstance().Add_IRcommand(new IRcommand_Binop_SUB_Integers(dst, leftIR, rightIR));}
        if (number == 3) {IR.getInstance().Add_IRcommand(new IRcommand_Binop_DIV_Integers(dst, leftIR, rightIR));}
        return dst;
    }
}