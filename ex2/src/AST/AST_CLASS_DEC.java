package AST;

public class AST_CLASS_DEC extends AST_DEC 
{
        public String id;
        public String superid;
        public AST_CFIELD_LIST list;

        public AST_CLASS_DEC(String id, String superid, AST_CFIELD_LIST list) 
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
}
