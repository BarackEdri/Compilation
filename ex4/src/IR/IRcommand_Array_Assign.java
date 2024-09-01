package IR;

import MIPS.*;
import TEMP.*;

public class IRcommand_Array_Assign extends IRcommand {
    public TEMP array, index, val;

    public IRcommand_Array_Assign(TEMP array, TEMP index, TEMP val) {
        this.array = array;
        this.index = index;
        this.val = val;
    }

    public void MIPSme() {
        System.out.println("IRcommand_Array_Assign" + "- MIPSme");
        MIPSGenerator.getInstance().arraySet(array, index, val);
    }
}