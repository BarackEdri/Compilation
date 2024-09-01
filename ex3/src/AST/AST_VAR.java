package AST;

import TYPES.*;
import SYMBOL_TABLE.*;

public abstract class AST_VAR extends AST_Node
{
      public void PrintMe() {
        System.out.print("ERROR PRINTING AST_VAR");
    }
    
    public TYPE SemantMe()
    {
        return null;
    }
}
