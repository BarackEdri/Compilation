package IR;

public class IRcommand_Array {
    public IRcommand head;
    public IRcommand_Array tail;

    IRcommand_Array(IRcommand head, IRcommand_Array tail) {
        this.head = head;
        this.tail = tail;
    }

    public void MIPSme() {
        if (head != null){head.MIPSme();}
        if (tail != null){tail.MIPSme();}
    }
}
