package IR;

import MIPS.*;
import TEMP.*;

public class IRcommand_Set_Dot extends IRcommand {
    public TEMP dst;
    public boolean cfg = false;
    String name;

    public IRcommand_Set_Dot(String Fname, TEMP dst) {
        this.dst = dst;
        this.name = Fname;
    }

    public void MIPSme() {
        System.out.println("IRcommand_Set_Dot" + "- MIPSme");
        MIPSGenerator.getInstance().fieldLoad(dst, offset);
    }
}