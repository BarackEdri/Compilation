package AST;

import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_EXP_BINOP extends AST_EXP
{
	public AST_EXP e1;
	public String operation;
	public AST_EXP e2;

	public AST_EXP_BINOP(AST_EXP e1, String operation, AST_EXP e2)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("exp -> exp BINOP exp\n");

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.e1 = e1;
		this.operation = operation;
		this.e2 = e2;
	}

	public void PrintMe()
	{
		System.out.print("AST_EXP_BINOP\n");

		/**************************************/
		/* RECURSIVELY PRINT e1, e2       ... */
		/**************************************/
		if (e1 != null) e1.PrintMe();
		if (e2 != null) e2.PrintMe();
		
		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("BINOP(%s)",operation));
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (e1 != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, e1.SerialNumber);
		if (e2 != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, e2.SerialNumber);
	}
    
    public TYPE SemantMe()
    {
        TYPE e1Type = e1.SemantMe();
        TYPE e2Type = e2.SemantMe();
        if (operation == "plus") // +
        {
            if (e1Type == TYPE_STRING.getInstance() && e2Type == TYPE_STRING.getInstance())
                return TYPE_STRING.getInstance();
            if (e1Type == TYPE_INT.getInstance() && e2Type == TYPE_INT.getInstance())
                return TYPE_INT.getInstance();
                
            throw new RuntimeException(String.format("%d", lineNum));
        }
        else if (operation == "eq") // =
        {
            if (e1Type == e2Type)
                return TYPE_INT.getInstance();
            
            if (e1 instanceof AST_EXP_NIL && (e2Type.isClass() || e2Type.isArray()))
                return TYPE_INT.getInstance();
            if (e2 instanceof AST_EXP_NIL && (e1Type.isClass() || e1Type.isArray()))
                return TYPE_INT.getInstance();
            
            if (e1Type.isClass() && e2Type.isClass()
                && (((TYPE_CLASS)e1Type).isSubClassOf(((TYPE_CLASS)e2Type))
                     || ((TYPE_CLASS)e2Type).isSubClassOf(((TYPE_CLASS)e1Type))))
                return TYPE_INT.getInstance();
        }
        else if (operation == "divide") // /
        {
            if (e1Type == TYPE_INT.getInstance() && e2Type == TYPE_INT.getInstance())
            {
                if (!(e2 instanceof AST_EXP_INT) || ((AST_EXP_INT)e2).value != 0)
                    return TYPE_INT.getInstance();
            }
        }
        else
        {
            if (e1Type == TYPE_INT.getInstance() && e2Type == TYPE_INT.getInstance())
                return TYPE_INT.getInstance();
        }

        throw new RuntimeException(String.format("%d", lineNum));
    }
}
