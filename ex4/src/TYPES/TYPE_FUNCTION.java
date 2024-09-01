package TYPES;

public class TYPE_FUNCTION extends TYPE {
    public TYPE returnType;
    public TYPE_LIST params;
    public String startLabel;

    public TYPE_FUNCTION(TYPE returnType,String name,TYPE_LIST params) {
        this.returnType = returnType;
        this.name = name;
        this.params = params;
    }

    public boolean isFunc() {return true;}
}
