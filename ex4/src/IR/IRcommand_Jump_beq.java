package IR;

import MIPS.*;
import TEMP.*;

public class IRcommand_Jump_beq extends IRcommand_Jump_Conditional {

    public IRcommand_Jump_beq(String label, TEMP op1, TEMP op2) {
        this.label = label;
        this.oprnd1 = op1;
        this.oprnd2 = op2;
        changeName("IRcommand_Jump_Conditional");
    }

    public void MIPSme() {
        System.out.println("IRcommand_Jump_beq" + "- MIPSme");
        MIPSGenerator.getInstance().beq(oprnd1, oprnd2, label);
    }
}
