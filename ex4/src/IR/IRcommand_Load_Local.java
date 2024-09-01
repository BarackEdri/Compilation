package IR;

import MIPS.*;
import TEMP.*;

public class IRcommand_Load_Local extends IRcommand {
    public TEMP dst;
    String var;

    public IRcommand_Load_Local(String var, TEMP dst) {
        this.dst = dst;
        this.var = var;
    }

    public void MIPSme() {
        System.out.println("IRcommand_Load_Local" + "- MIPSme");
        MIPSGenerator.getInstance().localLoad(dst, offset);
    }
}