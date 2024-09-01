package TEMP;

public class TEMP_LIST {
    public TEMP head;
    public TEMP_LIST tail;

    public TEMP_LIST(TEMP head,TEMP_LIST tail) {
        this.head = head;
        this.tail = tail;
    }

    public TEMP_LIST reverseList() {
        TEMP_LIST res = null;
        for (TEMP_LIST it = this; it != null; it = it.tail) {res = new TEMP_LIST(it.head, res);}
        return res;
    }
}
