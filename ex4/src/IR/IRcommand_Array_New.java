package IR;

import MIPS.*;
import TEMP.*;

public class IRcommand_Array_New extends IRcommand {
    public TEMP src;
    public TEMP dst;

    public IRcommand_Array_New(TEMP dst, TEMP src) {
        this.src = src;
        this.dst = dst;
    }

    public void MIPSme() {
        System.out.println("IRcommand_Array_New" + "- MIPSme");
        MIPSGenerator.getInstance().arrayAllocate(dst, src);
    }
}