package IR;

import MIPS.*;
import TEMP.*;

public class IRcommand_Call_Func1 extends IRcommand {
    public TEMP t;
    public TEMP_LIST tempList;
    String startLabel;

    public IRcommand_Call_Func1(TEMP t, String startLabel, TEMP_LIST tempList) {
        this.t = t;
        this.startLabel = startLabel;
        this.tempList = tempList;
    }

    public void MIPSme() {
        System.out.println("IRcommand_Call_Func1" + " - MIPSme");
        TEMP_LIST tmp = null;
        if (tempList != null) {tmp = tempList.reverseList();}
        MIPSGenerator.getInstance().funcCall(t, startLabel, tmp);
    }
}
