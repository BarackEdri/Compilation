package AST;

import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_CLASS_DEC extends AST_DEC 
{
        public String id;
        public String superid;
        public AST_CFIELD_LIST list;
        public int lineNum;

        public AST_CLASS_DEC(String id, String superid, AST_CFIELD_LIST list,int lineNum) 
        {
                /******************************/
                /* SET A UNIQUE SERIAL NUMBER */
                /******************************/
                SerialNumber = AST_Node_Serial_Number.getFresh();
        
                /***************************************/
                /* PRINT CORRESPONDING DERIVATION RULE */
                /***************************************/
                if (superid == null)          { System.out.format("classDec -> ID(%s)\n", id); }
                else { System.out.format("TypeIDList -> ID(%s) extends ID(%s)\n", id, superid); }
            
                /*******************************/
                /* COPY INPUT DATA MEMBERS ... */
                /*******************************/
                this.id = id;
                this.superid = superid;
                this.list = list;
                this.lineNum = lineNum;
        }
    
        public void PrintMe() 
        {
                System.out.print("AST_CLASS_DEC\n");

            	/**********************************/
		/* RECURSIVELY PRINT AST_TYPEID_LIST */
		/**********************************/
                if(superid == null)          { System.out.format("AST_CLASS_DEC ID(%s)\n", id); }
		else { System.out.format("AST_CLASS_DEC ID(%s) extends ID(%s)\n", id, superid); }
                list.PrintMe();
        
                /**********************************/
                /* PRINT to AST GRAPHVIZ DOT file */
                /**********************************/
                AST_GRAPHVIZ.getInstance().logNode(SerialNumber, "CLASS_DEC");
        
                /****************************************/
                /* PRINT Edges to AST GRAPHVIZ DOT file */
                /****************************************/
                if (superid == null) { AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("CLASS DEC ID(%s)", id)); }
		else { AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("CLASS DEC ID(%s) EXTENDS ID(%s)", id, superid)); }
                AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, list.SerialNumber);
        }
    
        public TYPE SemantMe()
        {
            TYPE father = null;
            TYPE alreadyDefined = SYMBOL_TABLE.getInstance().find(id);
            if (alreadyDefined != null)
                throw new RuntimeException(String.format("%d", lineNum));
            if (superid != null)
            {
                father = SYMBOL_TABLE.getInstance().find(superid);
                if (father == null || !(father instanceof TYPE_CLASS))
                    throw new RuntimeException(String.format("%d", lineNum));
            }

            TYPE_CLASS t = new TYPE_CLASS((TYPE_CLASS)father, id, null);
            SYMBOL_TABLE.getInstance().enter(id, t);
            int scopeCounter = FatherS((TYPE_CLASS)father) + 1;
            SYMBOL_TABLE.getInstance().beginScope();
            list.SemantMe(t);
            for (int i = 0; i < scopeCounter; i++)
                SYMBOL_TABLE.getInstance().endScope();
            
            return null;
        }

        private int FatherS(TYPE_CLASS father)
        {
            if (father == null)
                return 0;
            
            TYPE_CLASS nextF = father.father;
            int count = FatherS(nextF);
            
            SYMBOL_TABLE.getInstance().beginScope();
            for (TYPE_CLASS_VAR_DEC_LIST it = ((TYPE_CLASS)father).data_members; it  != null; it = it.tail)
            {
                SYMBOL_TABLE.getInstance().enter(it.head.name, it.head.type);
            }
            
            count = count + 1;
            return count;
        }
}
