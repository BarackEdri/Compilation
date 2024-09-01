package AST;

import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_CFIELD_LIST extends AST_Node 
{
        public AST_CFIELD head;
        public AST_CFIELD_LIST tail;

        public AST_CFIELD_LIST(AST_CFIELD head, AST_CFIELD_LIST tail) 
        {
                /******************************/
                /* SET A UNIQUE SERIAL NUMBER */
                /******************************/
                SerialNumber = AST_Node_Serial_Number.getFresh();
        
                /***************************************/
                /* PRINT CORRESPONDING DERIVATION RULE */
                /***************************************/
                if (tail == null) { System.out.print("cFieldList -> cField\n"); }
                else      { System.out.print("cFieldList -> cField cFieldList\n"); }
            
                /*******************************/
                /* COPY INPUT DATA MEMBERS ... */
                /*******************************/
                this.head = head;
                this.tail = tail;
        }
    
        public void PrintMe() 
        {
                System.out.print("AST_CFIELD_LIST\n");

            	/*************************************/
          		/* RECURSIVELY PRINT AST_CFIELD_LIST */
          		/*************************************/
                if (head != null) { head.PrintMe(); }
                if (tail != null) { tail.PrintMe(); }
        
                /**********************************/
                /* PRINT to AST GRAPHVIZ DOT file */
                /**********************************/
                AST_GRAPHVIZ.getInstance().logNode(SerialNumber, "CFIELD_LIST");
        
                /****************************************/
                /* PRINT Edges to AST GRAPHVIZ DOT file */
                /****************************************/
                if (head != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, head.SerialNumber);
                if (tail != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, tail.SerialNumber);
        }

        public AST_CFIELD getHead()
        {
                return this.head;
        }
       
        public AST_CFIELD_LIST getTail()
        {
                return this.tail;
        }
        
        public TYPE_CLASS_VAR_DEC_LIST SemantMe(TYPE_CLASS type) 
        {
            if (tail == null)
            {
                type.data_members =  new TYPE_CLASS_VAR_DEC_LIST((TYPE_CLASS_VAR_DEC)head.SemantMe(),
                type.data_members);
            }
            else
            {
                type.data_members =  new TYPE_CLASS_VAR_DEC_LIST((TYPE_CLASS_VAR_DEC)head.SemantMe(),
                (TYPE_CLASS_VAR_DEC_LIST)type.data_members);
                tail.SemantMe(type);
            }
            return null;
        }
}
