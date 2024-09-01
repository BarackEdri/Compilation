package AST;

import TYPES.*;
import SYMBOL_TABLE.*;

public abstract class AST_DEC extends AST_Node {
    
	public TYPE SemantMe() { return null; }
	
	public void PrintMe() { System.out.print("UNKNOWN AST_DEC");}
}
