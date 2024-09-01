package TYPES;

public class TYPE_STRING extends TYPE {
    private static TYPE_STRING instance = null;

    protected TYPE_STRING() {}

    public static TYPE_STRING getInstance() {
        if (instance == null) {
            instance = new TYPE_STRING();
            instance.name = "string";
        }
        return instance;
    }
}
