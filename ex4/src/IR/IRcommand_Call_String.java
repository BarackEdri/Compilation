package IR;

import MIPS.*;

public class IRcommand_Call_String extends IRcommand_Assign_Ntemp {

    public IRcommand_Call_String(String id, String value) {
        this.id = id;
        this.var = value;
        changeName("IRcommand_Assign_Ntemp");
    }

    public void MIPSme() {
        System.out.println("IRcommand_Call_String" + " - MIPSme");
        String label = IRcommand.getFreshLabel("const_string");
        MIPSGenerator.getInstance().declareString(label, id, var);
    }
}
