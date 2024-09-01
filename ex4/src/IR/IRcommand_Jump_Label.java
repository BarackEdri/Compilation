package IR;

import MIPS.*;

public class IRcommand_Jump_Label extends IRcommand_Jump_Conditional {
    
    public IRcommand_Jump_Label(String label) {
        this.label = label;
        changeName("IRcommand_Jump_Conditional");
    }

    public void MIPSme() {
        System.out.println("IRcommand_Jump_Label" + "- MIPSme");
        MIPSGenerator.getInstance().jump(label);
    }
}
