package IR;

import MIPS.*;
import TEMP.*;

public class IRcommand_Field_Access extends IRcommand {
    public TEMP src;
    public TEMP dst;
    String field;

    public IRcommand_Field_Access(TEMP dst, TEMP src, String fieldName) {
        this.dst = dst;
        this.src = src;
        this.field = fieldName;
    }

    public void MIPSme() {
        System.out.println("IRcommand_Field_Access" + "- MIPSme");
        MIPSGenerator.getInstance().fieldAccess(dst, offset, src);
    }
}
