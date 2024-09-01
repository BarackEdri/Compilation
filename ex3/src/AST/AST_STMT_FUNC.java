package AST;

import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_STMT_FUNC extends AST_STMT
{
        public AST_VAR var;
      	public String id;
      	public AST_EXP_LIST elist;
      
      	public AST_STMT_FUNC(AST_VAR var, String id, AST_EXP_LIST elist)
      	{
            		/******************************/
            		/* SET A UNIQUE SERIAL NUMBER */
            		/******************************/
            		SerialNumber = AST_Node_Serial_Number.getFresh();
            
            		/***************************************/
            		/* PRINT CORRESPONDING DERIVATION RULE */
            		/***************************************/
                if ((var == null) && (elist == null)) { System.out.printf("stmt -> %s ()\n", id); }
                if ((var == null) && (elist != null)) { System.out.printf("stmt -> %s (exps)\n", id); }
                if ((var != null) && (elist == null)) { System.out.printf("stmt -> var. %s ()\n", id); }
                if ((var != null) && (elist != null)) { System.out.printf("stmt -> var. %s (exps)\n", id); }
          
            		/*******************************/
            		/* COPY INPUT DATA MEMBERS ... */
            		/*******************************/
            		this.var = var;
            		this.id = id;
            		this.elist = elist;
      	}

      	public void PrintMe()
      	{
            		System.out.print("AST_STMT_FUNC\n");
          
            		if (var != null) var.PrintMe();
            		if (elist != null) elist.PrintMe();
            		
            		/***************************************/
            		/* PRINT Node to AST GRAPHVIZ DOT file */
            		/***************************************/
            		AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("STMT_FUNC(%s)",id));
            		
            		/****************************************/
            		/* PRINT Edges to AST GRAPHVIZ DOT file */
            		/****************************************/
            		if (var != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, var.SerialNumber);
            		if (elist != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, elist.SerialNumber);
      	}
    
        public TYPE SemantMe()
        {
            TYPE functionType = null;
            if (var == null)
            {
                functionType = SYMBOL_TABLE.getInstance().find(id);
                if (functionType == null || !functionType.isFunction())
                    throw new RuntimeException(String.format("%d", lineNum));
            }
            else
            {
                TYPE varType = var.SemantMe();
                if (!varType.isClass())
                    throw new RuntimeException(String.format("%d", lineNum));
                TYPE_CLASS classType = (TYPE_CLASS) varType;
                while (classType != null && functionType == null)
                {
                    for (TYPE_CLASS_VAR_DEC_LIST data_member_list = classType.data_members; data_member_list != null; data_member_list = data_member_list.tail)
                    {
                        if (data_member_list.head.name.equals(id))
                        {
                            functionType = data_member_list.head.type;
                            break;
                        }
                    }
                    classType = classType.father;
                }
                if (functionType == null || !functionType.isFunction())
                    throw new RuntimeException(String.format("%d", lineNum));
            }

            TYPE_LIST paramsTypes = null;
            if (elist != null)
                paramsTypes = elist.SemantMe();
            TYPE_LIST funcParamsTypes = ((TYPE_FUNCTION)functionType).params;
            while (paramsTypes != null && funcParamsTypes != null)
            {
                if (paramsTypes.head == null) // nil
                {
                    if (!funcParamsTypes.head.isClass() && !funcParamsTypes.head.isArray())
                        throw new RuntimeException(String.format("%d", lineNum));
                }
                else if (funcParamsTypes.head.isClass())
                {
                    if (!paramsTypes.head.isClass())
                        throw new RuntimeException(String.format("%d", lineNum));
                    if (!((TYPE_CLASS)paramsTypes.head).isSubClassOf((TYPE_CLASS)funcParamsTypes.head))
                        throw new RuntimeException(String.format("%d", lineNum));
                }
                else if (paramsTypes.head != funcParamsTypes.head)
                    throw new RuntimeException(String.format("%d", lineNum));
                paramsTypes = paramsTypes.tail;
                funcParamsTypes = funcParamsTypes.tail;
            }
            
            return null;
        }
}
