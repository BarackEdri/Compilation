package TYPES;

public class TYPE_INT extends TYPE {
    private static TYPE_INT instance = null;

    protected TYPE_INT() {}

    public static TYPE_INT getInstance() {
        if (instance == null) {
            instance = new TYPE_INT();
            instance.name = "int";
        }
        return instance;
    }
}
