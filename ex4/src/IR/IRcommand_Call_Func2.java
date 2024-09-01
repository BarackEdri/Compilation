package IR;

import AST.*;

public class IRcommand_Call_Func2 extends IRcommand {
    public String name;
    public int localVars;
    public String className;
    public String labelEnd;

    public IRcommand_Call_Func2(String name) {this.name = name;}

    public void MIPSme() {
        System.out.println("IRcommand_Call_Func2" + "- MIPSme");
        String labelStart;
        if (className != null){labelStart = className + "_" + name;}
        else {
            labelStart = IRcommand.getFreshLabel("start_" + name);
            AST_Node.offsets.put(name, labelStart);
        }
        (new IRcommand_Label(labelStart)).MIPSme();
        (new IRcommand_Prologue(localVars)).MIPSme();
    }
}
