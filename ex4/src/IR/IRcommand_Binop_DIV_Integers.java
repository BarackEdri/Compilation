package IR;

import MIPS.*;
import TEMP.*;

public class IRcommand_Binop_DIV_Integers extends IRcommand_Binop {

    public IRcommand_Binop_DIV_Integers(TEMP dst, TEMP t1, TEMP t2) {
        this.dst = dst;
        this.t1 = t1;
        this.t2 = t2;
        changeName("IRcommand_Binop");
    }

    public void MIPSme() {
        System.out.println("IRcommand_Binop_DIV_Integers" + "- MIPSme");
        MIPSGenerator.getInstance().div(dst, t1, t2);
    }
}
