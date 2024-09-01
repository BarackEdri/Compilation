package IR;

import MIPS.*;

public class IRcommand_Call_Int extends IRcommand_Assign_Ntemp {
    int value;

    public IRcommand_Call_Int(String id, int value) {
        this.id = id;
        this.value = value;
        changeName("IRcommand_Assign_Ntemp");
    }

    public void MIPSme() {
        System.out.println("IRcommand_Call_Int" + " - MIPSme");
        MIPSGenerator.getInstance().declareInt(id, value);
    }
}
