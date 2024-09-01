package AST;
import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_DEC_NODE extends AST_DEC
{
	public AST_DEC dec;

	public AST_DEC_NODE(AST_DEC dec)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("dec -> varDec | funcDec | classDec | arrayTypeDef\n");

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.dec = dec;
	}

	public void PrintMe()
	{
		System.out.print("AST_DEC_NODE\n");

		/**********************************/
		/* RECURSIVELY PRINT AST_DEC_NODE */
		/**********************************/
		dec.PrintMe();

		/**********************************/
		/* PRINT to AST GRAPHVIZ DOT file */
		/**********************************/
		AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("DEC_NODE"));
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,dec.SerialNumber);
	}
    
    public TYPE SemantMe()
    {
        return null;
    }
    

}
