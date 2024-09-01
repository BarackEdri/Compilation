package IR;

import MIPS.*;
import TEMP.*;

public class IRcommand_Jump_beqz extends IRcommand_Jump_Conditional {

    public IRcommand_Jump_beqz(TEMP t, String label) {
        this.oprnd1 = t;
        this.label = label;
        changeName("IRcommand_Jump_Conditional");
    }

    public void MIPSme() {
        System.out.println("IRcommand_Jump_beqz" + "- MIPSme");
        MIPSGenerator.getInstance().beqz(oprnd1, label);
    }
}
