package IR;

public abstract class IRcommand {
    protected static int label_counter = 0;
    public String IRname = "";
    public int offset;

    public void MIPSme() {System.out.println("DEFAULT MIPSme");}

    public static String getFreshLabel(String msg) { return String.format("Label_%d_%s", label_counter++, msg);}

    public void changeName(String name) {this.IRname = name;}
}
