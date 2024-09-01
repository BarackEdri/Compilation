package AST;

public class AST_TYPE extends AST_Node 
{
        public String type;
        public Boolean id;

        public AST_TYPE(String type, Boolean id) 
        {
                /******************************/
            		/* SET A UNIQUE SERIAL NUMBER */
            		/******************************/
                SerialNumber = AST_Node_Serial_Number.getFresh();

                /***************************************/
            		/* PRINT CORRESPONDING DERIVATION RULE */
            		/***************************************/
                if (id) { System.out.format("type -> ID(%s)\n", type); }
                else    { System.out.format("type -> %s\n", type);     }

                /*******************************/
            		/* COPY INPUT DATA MEMBERS ... */
            		/*******************************/
                this.type = type;
                this.id = id;
        }

        public void PrintMe() 
        {
		            System.out.print("AST_TYPE\n");

            		/**********************************/
  		          /* PRINT to AST GRAPHVIZ DOT file */
  		          /**********************************/
                if (id) { AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("ID(%s)", type)); }
                else    { AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("%s", type));     }
        }
}
