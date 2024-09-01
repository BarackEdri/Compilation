package AST;
import TYPES.*;
import SYMBOL_TABLE.*;

public abstract class AST_Node
{
	/*******************************************/
	/* The serial number is for debug purposes */
	/* In particular, it can help in creating  */
	/* a graphviz dot format of the AST ...    */
	/*******************************************/
	public int SerialNumber;
	public int lineNum;
	/***********************************************/
	/* The default message for an unknown AST node */
	/***********************************************/
	public void PrintMe()
	{
		System.out.print("AST NODE UNKNOWN\n");
	}
 
 public void setCurrentLine (int line)
 {
	 this.lineNum = line;
 }
 
 public TYPE SemantMe()
 {
	 return null;
 }
}
