package AST;

import TYPES.*;

public class AST_STMT_ASSIGN extends AST_STMT
{
	public AST_VAR var;
	public AST_EXP exp;

	public AST_STMT_ASSIGN(AST_VAR var,AST_EXP exp)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("stmt -> var ASSIGN exp SEMICOLON\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.var = var;
		this.exp = exp;
	}

	public void PrintMe()
	{
		/********************************************/
		/* AST NODE TYPE = AST ASSIGNMENT STATEMENT */
		/********************************************/
		System.out.print("AST NODE ASSIGN STMT\n");

		/***********************************/
		/* RECURSIVELY PRINT VAR + EXP ... */
		/***********************************/
		if (var != null) var.PrintMe();
		if (exp != null) exp.PrintMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(SerialNumber, "ASSIGN\nleft := right\n");
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,exp.SerialNumber);
	}
    
    public TYPE SemantMe()
    {
        TYPE varType = var.SemantMe();
        TYPE expType = exp.SemantMe();
        
        if (exp instanceof AST_EXP_NIL)
        {
            if (!varType.isClass() && !varType.isArray())
                throw new RuntimeException(String.format("%d", lineNum));
        }
        else if ((!expType.isClass() || !varType.isClass()) && varType != expType)
            throw new RuntimeException(String.format("%d", lineNum));
        else if ((expType.isClass() && varType.isClass())
            && !((TYPE_CLASS)expType).isSubClassOf(((TYPE_CLASS)varType)))
            throw new RuntimeException(String.format("%d", lineNum));
            
        return null;
    }
}
