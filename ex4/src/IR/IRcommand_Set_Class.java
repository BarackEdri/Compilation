package IR;

import java.util.*;

import AST.*;
import MIPS.*;
import TEMP.*;

public class IRcommand_Set_Class extends IRcommand_Assign {
    public String className;

    public IRcommand_Set_Class(TEMP t1, String className) {
        this.dst = t1;
        this.className = className;
    }

    public void MIPSme() {
        System.out.println("IRcommand_Set_Class" + "- MIPSme");
        int size = AST_Node.getClassSize(className);
        MIPSGenerator.getInstance().classAllocate(dst, className, size);
        ArrayList<String> fields = AST_Node.classfields.get(className);
        for (String field : fields) {
            String currName = className + "_" + field;
            int curr_offset = AST_Node.GetOffset(currName);
            MIPSGenerator.getInstance().setField(dst, currName, curr_offset);
        }
    }
}