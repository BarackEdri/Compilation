package IR;

import java.util.*;

public class BasicBlocks {
    BasicBlocks father, direct, work;
    int time;
    boolean inFunc;
    IRcommand line;
    HashSet<String> inSet, outSet, FuncScope;

    public BasicBlocks(int time, IRcommand line) {
        this.time = time;
        this.line = line;
        inSet = new HashSet<>();
        outSet = new HashSet<>();
        FuncScope = new HashSet<>();
        inFunc = false;
    }
}