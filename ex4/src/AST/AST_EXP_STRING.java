package AST;

import IR.*;
import TEMP.*;
import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_EXP_STRING extends AST_EXP {
    public String s;
    public String scope;
    public String label;

    public AST_EXP_STRING(Object s) {
        SerialNumber = AST_Node_Serial_Number.getFresh();
        String inputString = (String) s;
        
        // Check for empty quoted string
        if ("\"\"".equals(inputString)) {
            this.s = "";
        }
        // Check for non-empty strings enclosed in quotes
        else if (inputString != null && inputString.contains("\"")) {
            String[] splitString = inputString.split("\"");
            if (splitString.length > 1) {
                this.s = splitString[1];
            } else {
                System.err.println("Invalid string format: " + inputString);
                throw new RuntimeException("String format is invalid: expected a quoted string.");
            }
        } 
        // Handle other invalid formats
        else {
            System.err.println("Invalid string format: " + inputString);
            throw new RuntimeException("String format is invalid: expected a quoted string.");
        }
        
        System.out.print("exp -> STRING\n");
    }


    /*public AST_EXP_STRING(Object s) {
        SerialNumber = AST_Node_Serial_Number.getFresh();
        this.s = ((String) s).split("\"")[1];
        System.out.print("exp -> STRING\n");
    }*/

    public void PrintMe() {
        System.out.format("AST EXP STRING NODE\n");
        AST_GRAPHVIZ.getInstance().logNode(SerialNumber, String.format("%s", s));
    }

    public TYPE SemantMe() {
        System.out.println("EXP STRING - SemantMe");
        scope = SYMBOL_TABLE.getInstance().getScope();
        return TYPE_STRING.getInstance();
    }

    public TEMP IRme() {
        System.out.println("EXP STRING - IRme");
        TEMP t = TEMP_FACTORY.getInstance().getFreshTEMP();
        this.label = IRcommand.getFreshLabel("const_string");
        IR.getInstance().Add_IRcommand(new IRcommand_Const_String(t, label, s));
        return t;
    }
}