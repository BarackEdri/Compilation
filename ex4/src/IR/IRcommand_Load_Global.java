package IR;

import MIPS.*;
import TEMP.*;

public class IRcommand_Load_Global extends IRcommand_Assign {
    String label;

    public IRcommand_Load_Global(TEMP dst, String name) {
        this.dst = dst;
        this.label = name;
        changeName("IRcommand_Assign");
    }

    public void MIPSme() {
        System.out.println("IRcommand_Load_Global" + "- MIPSme");
        MIPSGenerator.getInstance().labelLoad(dst, label);
    }
}
