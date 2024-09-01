package IR;

import MIPS.*;
import TEMP.*;

public class IRcommand_Const_String extends IRcommand_Assign {
    String label;
    String val;

    public IRcommand_Const_String(TEMP dst, String label, String value) {
        this.dst = dst;
        this.label = label;
        this.val = value;
        changeName("IRcommand_Assign");
    }

    public void MIPSme() {
        System.out.println("IRcommand_Const_String" + "- MIPSme");
        MIPSGenerator.getInstance().stringAllocate(dst, label, val);
    }
}
