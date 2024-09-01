package IR;

import MIPS.*;
import TEMP.*;

public class IRcommand_Binop_GT_Integers extends IRcommand_Binop {

    public IRcommand_Binop_GT_Integers(TEMP dst, TEMP t1, TEMP t2) {
        this.dst = dst;
        this.t1 = t1;
        this.t2 = t2;
        changeName("IRcommand_Binop");
    }

    public void MIPSme() {
        System.out.println("IRcommand_Binop_GT_Integers" + "- MIPSme");
        String label_end = getFreshLabel("end");
        String label_AssignOne = getFreshLabel("AssignOne");
        String label_AssignZero = getFreshLabel("AssignZero");
        MIPSGenerator.getInstance().bgt(t1, t2, label_AssignOne);
        MIPSGenerator.getInstance().ble(t1, t2, label_AssignZero);
        MIPSGenerator.getInstance().label(label_AssignOne);
        MIPSGenerator.getInstance().li(dst, 1);
        MIPSGenerator.getInstance().jump(label_end);
        MIPSGenerator.getInstance().label(label_AssignZero);
        MIPSGenerator.getInstance().li(dst, 0);
        MIPSGenerator.getInstance().jump(label_end);
        MIPSGenerator.getInstance().label(label_end);
    }
}
