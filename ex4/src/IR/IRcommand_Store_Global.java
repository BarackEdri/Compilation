package IR;

import MIPS.*;
import TEMP.*;

public class IRcommand_Store_Global extends IRcommand {
    public TEMP dst;
    String label;

    public IRcommand_Store_Global(TEMP dst, String label) {
        this.dst = dst;
        this.label = label;
    }

    public void MIPSme() {
        System.out.println("IRcommand_Store_Global" + "- MIPSme");
        MIPSGenerator.getInstance().labelStore(dst, label);
    }
}
