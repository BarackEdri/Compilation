package IR;

import MIPS.*;

public class IRcommand_Label extends IRcommand {
    public String label_name;

    public IRcommand_Label(String label_name) {this.label_name = label_name;}

    public void MIPSme() {
        System.out.println("IRcommand_Label" + "- MIPSme");
        MIPSGenerator.getInstance().label(label_name);
    }
}
