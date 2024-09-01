package IR;

import MIPS.*;

public class IRcommand_Prologue extends IRcommand {
    int localVars;

    public IRcommand_Prologue(int localVars) {this.localVars = localVars;}

    public void MIPSme() {
        System.out.println("IRcommand_Prologue" + "- MIPSme");
        MIPSGenerator.getInstance().prologue(localVars);
    }
}
