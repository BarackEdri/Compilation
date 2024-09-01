package AST;

import TYPES.*;
import SYMBOL_TABLE.*;

public abstract class AST_EXP extends AST_Node {
	public void PrintMe() {
        	System.out.print("UNKNOWN AST_EXP");
    }
    
    public TYPE SemantMe()
    {
        return null;
    }
}
