package IR;

public class IR {
    private static IR instance = null;
    public IRcommand head = null;
    public IRcommand_Array tail = null;
    private IRcommand firstHead = null;
    private IRcommand_Array firstTail = null;

    protected IR() {}

    public static IR getInstance() {
        if (instance == null) {instance = new IR();}
        return instance;
    }

    public void Add_IRcommand(IRcommand cmd) {
        if ((head == null) && (tail == null)) {this.head = cmd;} 
        else if ((head != null) && (tail == null)) {this.tail = new IRcommand_Array(cmd, null);} 
        else {
            IRcommand_Array it = tail;
            while ((it != null) && (it.tail != null)) {it = it.tail;}
            it.tail = new IRcommand_Array(cmd, null);
        }
    }

    public void MIPSme() {
        if (head != null){head.MIPSme();}
        if (tail != null){tail.MIPSme();}
    }

    public void MIPSinitialization() {
        if (firstHead != null){firstHead.MIPSme();}
        if (firstTail != null){firstTail.MIPSme();}
    }
}
