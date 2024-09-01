package IR;

import MIPS.*;

public class IRcommand_Call_Object extends IRcommand_Assign_Ntemp {
    
    public IRcommand_Call_Object(String id) {
        this.id = id;
        changeName("IRcommand_Assign_Ntemp");
    }

    public void MIPSme() {
        System.out.println("IRcommand_Call_Object" + " - MIPSme");
        MIPSGenerator.getInstance().declareObject(id);
    }
}
