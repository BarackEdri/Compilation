package IR;

import MIPS.*;
import TEMP.*;

public class IRcommand_Call_Virtual extends IRcommand {
    public TEMP dst;
    public TEMP classTemp;
    public TEMP_LIST args;
    String funcName;

    public IRcommand_Call_Virtual(TEMP dst, TEMP classTemp, String funcName, TEMP_LIST tempList) {
        this.dst = dst;
        this.classTemp = classTemp;
        this.funcName = funcName;
        this.args = tempList;

    }

    public void MIPSme() {
        System.out.println("IRcommand_Call_Virtual" + " - MIPSme");
        MIPSGenerator.getInstance().virtualCall(dst, classTemp, offset, args);
    }
}