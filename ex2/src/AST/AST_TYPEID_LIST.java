package AST;

public class AST_TYPEID_LIST extends AST_Node 
{
        public AST_TYPE type;
        public String head;
        public AST_TYPEID_LIST tail;

        public AST_TYPEID_LIST(AST_TYPE type, String head, AST_TYPEID_LIST tail) 
        {
                /******************************/
                /* SET A UNIQUE SERIAL NUMBER */
                /******************************/
                SerialNumber = AST_Node_Serial_Number.getFresh();
        
                /***************************************/
                /* PRINT CORRESPONDING DERIVATION RULE */
                /***************************************/
                if (tail == null) { System.out.print("TypeIDList -> type ID\n"); }
                else      { System.out.print("TypeIDList -> type ID, IDList\n"); }
            
                /*******************************/
                /* COPY INPUT DATA MEMBERS ... */
                /*******************************/
                this.type = type;
                this.head = head;
                this.tail = tail;
        }
    
        public void PrintMe() 
        {
                System.out.print("AST_TYPEID_LIST\n");

            	/**********************************/
		/* RECURSIVELY PRINT AST_TYPEID_LIST */
		/**********************************/
                if(type != null) { type.PrintMe(); }
                if (tail != null) { tail.PrintMe(); }
        
                /**********************************/
                /* PRINT to AST GRAPHVIZ DOT file */
                /**********************************/
                AST_GRAPHVIZ.getInstance().logNode(SerialNumber, "TYPEID_LIST");
        
                /****************************************/
                /* PRINT Edges to AST GRAPHVIZ DOT file */
                /****************************************/
                AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("TYPE ID(%s)", head));
                if (type != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, type.SerialNumber);
                if (tail != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, tail.SerialNumber);
        }
}
