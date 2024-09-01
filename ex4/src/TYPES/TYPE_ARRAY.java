package TYPES;

public class TYPE_ARRAY extends TYPE {
    public TYPE entryType;

    public TYPE_ARRAY(TYPE entryType,String name) {
        this.entryType = entryType;
        this.name = name;
    }

    public boolean isArray() {return true;}
}
