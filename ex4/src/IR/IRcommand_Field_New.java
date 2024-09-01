package IR;

import MIPS.*;
import TEMP.*;

public class IRcommand_Field_New extends IRcommand {
    public TEMP dst;
    public TEMP val;
    String var;

    public IRcommand_Field_New(TEMP t1, String varName, TEMP val) {
        this.dst = t1;
        this.val = val;
        this.var = varName;
    }

    public void MIPSme() {
        System.out.println("IRcommand_Field_New" + "- MIPSme");
        MIPSGenerator.getInstance().fieldSet(dst, offset, val);
    }
}