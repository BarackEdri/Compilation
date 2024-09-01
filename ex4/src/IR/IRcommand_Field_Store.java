package IR;

import MIPS.*;
import TEMP.*;

public class IRcommand_Field_Store extends IRcommand {
    public TEMP val;
    String classs;
    String var;

    public IRcommand_Field_Store(String classs, String varName, TEMP value) {
        this.classs = classs;
        this.var = varName;
        this.val = value;
    }
    public void MIPSme() {
        System.out.println("IRcommand_Field_Store" + "- MIPSme");
        MIPSGenerator.getInstance().fieldStore(offset, val);
    }
}