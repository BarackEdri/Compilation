package IR;

public class IRVariableID {
    static int counter = 0;

    public String declared_name;
    public int serial_number;

    public IRVariableID(String declared_name) {
        this.declared_name = declared_name;
        this.serial_number = counter++;
    }

    public String toString() {
        return declared_name + "@" + serial_number;
    }
}
