package TEMP;

public class TEMP_FACTORY {
    private static TEMP_FACTORY instance = null;
    private int counter = 0;

    protected TEMP_FACTORY() {}

    public static TEMP_FACTORY getInstance() {
        if (instance == null) {
            instance = new TEMP_FACTORY();
        }
        return instance;
    }

    public TEMP getFreshTEMP() {return new TEMP(counter++);}
}
