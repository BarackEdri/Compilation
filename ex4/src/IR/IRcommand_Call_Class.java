package IR;

import java.util.*;

import MIPS.*;

public class IRcommand_Call_Class extends IRcommand {
    String class_name;
    ArrayList<ArrayList<String>> funcs;

    public IRcommand_Call_Class(String name, ArrayList<ArrayList<String>> funcs) {
        this.class_name = name;
        this.funcs = funcs;
    }

    public void MIPSme() {
        System.out.println("IRcommand_Call_Class" + "- MIPSme");
        MIPSGenerator.getInstance().declareClass(class_name, funcs);
    }
}