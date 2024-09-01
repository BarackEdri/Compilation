package IR;

import MIPS.*;
import TEMP.*;

public class IRcommand_Const_Int extends IRcommand_Assign {
    int val;

    public IRcommand_Const_Int(TEMP t, int value) {
        this.dst = t;
        this.val = value;
        changeName("IRcommand_Assign");
    }

    public void MIPSme() {
        System.out.println("IRcommand_Const_Int" + "- MIPSme");
        MIPSGenerator.getInstance().li(dst, val);
    }
}
