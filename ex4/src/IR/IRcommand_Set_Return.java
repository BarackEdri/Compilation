package IR;

import MIPS.*;
import TEMP.*;

//not in liveness
public class IRcommand_Set_Return extends IRcommand {
    public TEMP RetVal;

    public IRcommand_Set_Return(TEMP retVal) {this.RetVal = retVal;}

    public void MIPSme() {
        System.out.println("IRcommand_Set_Return" + "- MIPSme");
        MIPSGenerator.getInstance().ret(RetVal);
    }
}
