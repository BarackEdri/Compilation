package IR;
import TEMP.*;
import java.util.HashSet;

public class IRcommand_Return extends IRcommand {
    public TEMP returnValue;

    public IRcommand_Return(TEMP returnValue) {
        this.returnValue = returnValue;
    }
    public void transform() {
        // טיפול בטרנספורמציה של inTransform ל-outTransform
        this.outTransform = new HashSet<>(this.inTransform);
        if (returnValue != null) {
            this.outTransform.add(returnValue.toString());
        }
    }

    public HashSet<String> outTransform() {
        return new HashSet<>(this.inTransform);
    }

    public boolean isReturn() {
        return true;
    }

    public String toString() {
        if (returnValue!=null)
        return "Return@"+returnValue.toString();
        return "Return@null";
    }
}
