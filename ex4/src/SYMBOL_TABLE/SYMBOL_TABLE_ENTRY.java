package SYMBOL_TABLE;

import TYPES.*;

public class SYMBOL_TABLE_ENTRY {
    public String name;
    public boolean isInstance;
    public TYPE type;
    public SYMBOL_TABLE_ENTRY prevtop;
    public SYMBOL_TABLE_ENTRY next;
    public int prevtop_index;
    int index;

    public SYMBOL_TABLE_ENTRY(
        String name,
        TYPE type,
        int index,
        SYMBOL_TABLE_ENTRY next,
        SYMBOL_TABLE_ENTRY prevtop,
        int prevtop_index) {
        this.index = index;
        this.name = name;
        this.type = type;
        this.next = next;
        this.prevtop = prevtop;
        this.prevtop_index = prevtop_index;
        this.isInstance = true;
    }
}
