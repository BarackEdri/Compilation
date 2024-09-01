package IR;

import java.util.*;

import IR.GraphColor.node;
import IR.*;
import TEMP.*;

public class CFG {
    BasicBlocks head;
    BasicBlocks tail;
    HashMap<String, String> mips_hash = new HashMap<>();

    public CFG() {
        IRcommand h = IR.getInstance().head;
        IRcommand_Array t = IR.getInstance().tail;
        int lineCounter = 0;
        this.head = new BasicBlocks(lineCounter, h);
        lineCounter++;
        BasicBlocks curr = this.head;
        BasicBlocks next;
        while (t != null) {
            h = t.head;t = t.tail;
            next = new BasicBlocks(lineCounter, h);
            lineCounter++;
            curr.direct = next;next.father = curr;curr = next;
        }
        this.tail = curr;
        curr = this.head;
        while (curr != null) {
            boolean isBranch = (curr.line.IRname.equals("IRcommand_Jump_Conditional"));
            if (isBranch) {
                next = this.head;
                String l = ((IRcommand_Jump_Conditional) curr.line).label;
                while (next != null) {
                    boolean isLabel = next.line.getClass().toString().equals("class IR.IRcommand_Label");
                    if (isLabel) {if (Objects.equals(((IRcommand_Label) next.line).label_name, l)) {curr.work = next;}break;}
                    next = next.direct;
                }
            }
            curr = curr.direct;
        }
    }

    public void liveness() {
        BasicBlocks curr = this.tail;
        boolean notTail = false;
        while (curr != null) {
            if (notTail) {
                curr.outSet.addAll(curr.direct.inSet);
                if (curr.work != null) {curr.outSet.addAll(curr.work.inSet);}
            }
            notTail = true;
            boolean isBinop = curr.line.IRname.equals("IRcommand_Binop");
            boolean isAssign = curr.line.IRname.equals("IRcommand_Assign");
            boolean isTwo = curr.line.IRname.equals("IRcommand_Assign_Ttemp");
            curr.inSet.addAll(curr.outSet);
            if (isBinop) {
                curr.inSet.remove(("Temp_" + (((IRcommand_Binop) curr.line).dst.getSerialNumber())));
                curr.inSet.add(("Temp_" + (((IRcommand_Binop) curr.line).t1.getSerialNumber())));
                curr.inSet.add(("Temp_" + (((IRcommand_Binop) curr.line).t2.getSerialNumber())));
                if (curr.inFunc) {
                    curr.FuncScope.remove(("Temp_" + (((IRcommand_Binop) curr.line).dst.getSerialNumber())));
                    curr.FuncScope.add(("Temp_" + (((IRcommand_Binop) curr.line).t1.getSerialNumber())));
                    curr.FuncScope.add(("Temp_" + (((IRcommand_Binop) curr.line).t2.getSerialNumber())));
                }
            }
            if (isAssign) {
                curr.inSet.remove(("Temp_" + (((IRcommand_Assign) curr.line).dst.getSerialNumber())));
                if (curr.inFunc) {curr.FuncScope.remove(("Temp_" + (((IRcommand_Assign) curr.line).dst.getSerialNumber())));}
            }
            if (isTwo) {
                curr.inSet.remove(("Temp_" + (((IRcommand_Assign_Ttemp) curr.line).dst.getSerialNumber())));
                curr.inSet.add(("Temp_" + (((IRcommand_Assign_Ttemp) curr.line).val.getSerialNumber())));
                if (curr.inFunc) {
                    curr.FuncScope.remove(("Temp_" + (((IRcommand_Assign_Ttemp) curr.line).dst.getSerialNumber())));
                    curr.FuncScope.add(("Temp_" + (((IRcommand_Assign_Ttemp) curr.line).val.getSerialNumber())));
                }
            }
            if (curr.line.getClass().toString().equals("class IR.IRcommand_Store_Local")) {
                curr.inSet.add(("Temp_" + (((IRcommand_Store_Local) curr.line).src.getSerialNumber())));
                if (curr.inFunc) {curr.FuncScope.add(("Temp_" + (((IRcommand_Store_Local) curr.line).src.getSerialNumber())));}
            }
            if (curr.line.getClass().toString().equals("class IR.IRcommand_Store_Global")) {
                curr.inSet.add(("Temp_" + (((IRcommand_Store_Global) curr.line).dst.getSerialNumber())));
                if (curr.inFunc) {curr.FuncScope.add(("Temp_" + (((IRcommand_Store_Global) curr.line).dst.getSerialNumber())));}
            }
            if (curr.line.getClass().toString().equals("class IR.IRcommand_Set_Dot")) {
                if (!((IRcommand_Set_Dot) curr.line).cfg) {
                    curr.inSet.add(("Temp_" + (((IRcommand_Set_Dot) curr.line).dst.getSerialNumber())));
                    if (curr.inFunc) {curr.FuncScope.add(("Temp_" + (((IRcommand_Set_Dot) curr.line).dst.getSerialNumber())));}
                } else {
                    curr.inSet.remove(("Temp_" + (((IRcommand_Set_Dot) curr.line).dst.getSerialNumber())));
                    if (curr.inFunc) {curr.FuncScope.remove(("Temp_" + (((IRcommand_Set_Dot) curr.line).dst.getSerialNumber())));}
                }
            }
            if (curr.line.getClass().toString().equals("class IR.IRcommand_Call_Func1")) {
                curr.inSet.remove(("Temp_" + (((IRcommand_Call_Func1) curr.line).t.getSerialNumber())));
                TEMP h;
                TEMP_LIST t = ((IRcommand_Call_Func1) curr.line).tempList;
                while (t != null) {
                    h = t.head;
                    curr.inSet.add(("Temp_" + (h.getSerialNumber())));
                    t = t.tail;
                }
                if (curr.inFunc) {
                    curr.FuncScope.remove(("Temp_" + (((IRcommand_Call_Func1) curr.line).t.getSerialNumber())));
                    t = ((IRcommand_Call_Func1) curr.line).tempList;
                    while (t != null) {
                        h = t.head;
                        curr.FuncScope.add(("Temp_" + (h.getSerialNumber())));
                        t = t.tail;
                    }
                }
            }
            if (curr.line.getClass().toString().equals("class IR.IRcommand_Call_Virtual")) {
                curr.inSet.remove(("Temp_" + (((IRcommand_Call_Virtual) curr.line).dst.getSerialNumber())));
                curr.inSet.add(("Temp_" + (((IRcommand_Call_Virtual) curr.line).classTemp.getSerialNumber())));
                TEMP h;
                TEMP_LIST t = ((IRcommand_Call_Virtual) curr.line).args;
                while (t != null) {
                    h = t.head;
                    curr.inSet.add(("Temp_" + (h.getSerialNumber())));
                    t = t.tail;
                }
                if (curr.inFunc) {
                    curr.FuncScope.remove(("Temp_" + (((IRcommand_Call_Virtual) curr.line).dst.getSerialNumber())));
                    curr.FuncScope.add(("Temp_" + (((IRcommand_Call_Virtual) curr.line).classTemp.getSerialNumber())));
                    t = ((IRcommand_Call_Virtual) curr.line).args;
                    while (t != null) {
                        h = t.head;
                        curr.FuncScope.add(("Temp_" + (h.getSerialNumber())));
                        t = t.tail;
                    }
                }
            }
            if (curr.line.IRname.equals("IRcommand_Jump_Conditional")) {
                if ((curr.line.getClass().toString().equals("class IR.IRcommand_Jump_bnez")) || (curr.line.getClass().toString().equals("class IR.IRcommand_Jump_beqz"))) {
                    curr.inSet.add(("Temp_" + (((IRcommand_Jump_Conditional) curr.line).oprnd1.getSerialNumber())));
                    if (curr.inFunc) {curr.FuncScope.add(("Temp_" + (((IRcommand_Jump_Conditional) curr.line).oprnd1.getSerialNumber())));}
                } else if ((curr.line.getClass().toString().equals("class IR.IRcommand_Jump_Label"))) {} 
                else {
                    curr.inSet.add(("Temp_" + (((IRcommand_Jump_Conditional) curr.line).oprnd1.getSerialNumber())));
                    curr.inSet.add(("Temp_" + (((IRcommand_Jump_Conditional) curr.line).oprnd2.getSerialNumber())));
                    if (curr.inFunc) {
                        curr.FuncScope.add(("Temp_" + (((IRcommand_Jump_Conditional) curr.line).oprnd1.getSerialNumber())));
                        curr.FuncScope.add(("Temp_" + (((IRcommand_Jump_Conditional) curr.line).oprnd2.getSerialNumber())));
                    }
                }
            }
            if (curr.line.getClass().toString().equals("class IR.IRcommand_Array_Assign")) {
                curr.inSet.add(("Temp_" + (((IRcommand_Array_Assign) curr.line).array.getSerialNumber())));
                curr.inSet.add(("Temp_" + (((IRcommand_Array_Assign) curr.line).index.getSerialNumber())));
                curr.inSet.add(("Temp_" + (((IRcommand_Array_Assign) curr.line).val.getSerialNumber())));
                if (curr.inFunc) {
                    curr.FuncScope.add(("Temp_" + (((IRcommand_Array_Assign) curr.line).array.getSerialNumber())));
                    curr.FuncScope.add(("Temp_" + (((IRcommand_Array_Assign) curr.line).index.getSerialNumber())));
                    curr.FuncScope.add(("Temp_" + (((IRcommand_Array_Assign) curr.line).val.getSerialNumber())));
                }
            }
            if (curr.line.getClass().toString().equals("class IR.IRcommand_Prologue")) {
                curr.inFunc = false;
                if (curr.FuncScope != null) {
                    for (String temp : curr.FuncScope) {curr.inSet.remove(temp);}
                    curr.FuncScope = new HashSet<String>();
                }
            }
            if (curr.line.getClass().toString().equals("class IR.IRcommand_Epilogue")) {curr.inFunc = true;}
            if (curr.line.getClass().toString().equals("class IR.IRcommand_Array_Access")) {
                curr.inSet.remove(("Temp_" + (((IRcommand_Array_Access) curr.line).dst.getSerialNumber())));
                curr.inSet.add(("Temp_" + (((IRcommand_Array_Access) curr.line).t1.getSerialNumber())));
                curr.inSet.add(("Temp_" + (((IRcommand_Array_Access) curr.line).t2.getSerialNumber())));
                if (curr.inFunc) {
                    curr.FuncScope.remove(("Temp_" + (((IRcommand_Array_Access) curr.line).dst.getSerialNumber())));
                    curr.FuncScope.add(("Temp_" + (((IRcommand_Array_Access) curr.line).t1.getSerialNumber())));
                    curr.FuncScope.add(("Temp_" + (((IRcommand_Array_Access) curr.line).t2.getSerialNumber())));
                }
            }
            if (curr.line.getClass().toString().equals("class IR.IRcommand_Load_Local")) {
                curr.inSet.remove(("Temp_" + (((IRcommand_Load_Local) curr.line).dst.getSerialNumber())));
                if (curr.inFunc) {curr.FuncScope.remove(("Temp_" + (((IRcommand_Load_Local) curr.line).dst.getSerialNumber())));}
            }
            if (curr.line.getClass().toString().equals("class IR.IRcommand_Field_Store")) {
                curr.inSet.remove(("Temp_" + (((IRcommand_Field_Store) curr.line).val.getSerialNumber())));
                if (curr.inFunc) {curr.FuncScope.remove(("Temp_" + (((IRcommand_Field_Store) curr.line).val.getSerialNumber())));}
            }
            if (curr.line.getClass().toString().equals("class IR.IRcommand_Field_Access")) {
                curr.inSet.remove(("Temp_" + (((IRcommand_Field_Access) curr.line).dst.getSerialNumber())));
                curr.inSet.add(("Temp_" + (((IRcommand_Field_Access) curr.line).src.getSerialNumber())));
                if (curr.inFunc) {
                    curr.FuncScope.remove(("Temp_" + (((IRcommand_Field_Access) curr.line).dst.getSerialNumber())));
                    curr.FuncScope.add(("Temp_" + (((IRcommand_Field_Access) curr.line).src.getSerialNumber())));
                }
            }
            if (curr.line.getClass().toString().equals("class IR.IRcommand_Field_New")) {
                curr.inSet.add(("Temp_" + (((IRcommand_Field_New) curr.line).dst.getSerialNumber())));
                curr.inSet.add(("Temp_" + (((IRcommand_Field_New) curr.line).val.getSerialNumber())));
                if (curr.inFunc) {
                    curr.FuncScope.add(("Temp_" + (((IRcommand_Field_New) curr.line).dst.getSerialNumber())));
                    curr.FuncScope.add(("Temp_" + (((IRcommand_Field_New) curr.line).val.getSerialNumber())));
                }
            }
            if (curr.line.getClass().toString().equals("class IR.IRcommand_Set_Return")) {
                curr.inFunc = true;
                if (((IRcommand_Set_Return) curr.line).RetVal != null) {curr.inSet.add(("Temp_" + (((IRcommand_Set_Return) curr.line).RetVal.getSerialNumber())));}
            }
            if (curr.line.getClass().toString().equals("class IR.IRcommand_Array_New")) {
                curr.inSet.add(("Temp_" + (((IRcommand_Array_New) curr.line).src.getSerialNumber())));
                curr.inSet.add(("Temp_" + (((IRcommand_Array_New) curr.line).dst.getSerialNumber())));
                if (curr.inFunc) {
                    curr.FuncScope.add(("Temp_" + (((IRcommand_Array_New) curr.line).src.getSerialNumber())));
                    curr.FuncScope.add(("Temp_" + (((IRcommand_Array_New) curr.line).dst.getSerialNumber())));
                }
            }
            if (curr.father != null) {
                curr.father.inFunc = curr.inFunc;
                curr.father.FuncScope = curr.FuncScope;
            }
            System.out.println(curr.line.getClass());
            System.out.println(curr.inSet);
            curr = curr.father;
        }
    }

    public void K_color() {
        Stack<node> stack = new Stack<node>();
        GraphColor g = new GraphColor(this.head);
        for (node curr : g.overall) {
            if (curr.active_node < 10) {
                g.valid.remove(curr);
                stack.push(curr);
                for (node removing : g.overall) {if (removing.node_hash.contains(curr.name)) {removing.active_node--;}}
            }
        }
        if (!g.isEmpty()) {System.out.println("failed empty graph");}
        while (!stack.isEmpty()) {
            node toAdd = stack.pop();
            g.valid.add(toAdd);
            HashSet<String> col = new HashSet<String>();
            col.add("0");col.add("1");col.add("2");
            col.add("3");col.add("4");col.add("5");
            col.add("6");col.add("7");col.add("8");
            for (String neigName : toAdd.node_hash) {
                node actualNeig = g.find(neigName);
                if (actualNeig != null) {col.remove(actualNeig.paint);}
            }
            toAdd.paint = col.iterator().next();
        }
        for (node curr : g.valid) {this.mips_hash.put(curr.name, curr.paint);}
        this.mips_hash.put("dead", "9");
        changeIR();
    }

    public void changeIR() {
        IRcommand h = IR.getInstance().head;
        IRcommand_Array t = IR.getInstance().tail;
        while (t != null) {
            if (h.IRname.equals("IRcommand_Binop")) {
                String theNum;
                if (!((IRcommand_Binop) h).dst.changed) {
                    theNum = mips_hash.get(("Temp_" + (((IRcommand_Binop) h).dst.getSerialNumber())));
                    if (theNum == null) {theNum = mips_hash.get("dead");}
                    ((IRcommand_Binop) h).dst.serial = Integer.parseInt(theNum);
                    ((IRcommand_Binop) h).dst.changed = true;
                }
                if (!((IRcommand_Binop) h).t1.changed) {
                    theNum = mips_hash.get(("Temp_" + (((IRcommand_Binop) h).t1.getSerialNumber())));
                    if (theNum == null) {theNum = mips_hash.get("dead");}
                    ((IRcommand_Binop) h).t1.serial = Integer.parseInt(theNum);
                    ((IRcommand_Binop) h).t1.changed = true;
                }
                if (!((IRcommand_Binop) h).t2.changed) {
                    theNum = mips_hash.get(("Temp_" + (((IRcommand_Binop) h).t2.getSerialNumber())));
                    if (theNum == null) {theNum = mips_hash.get("dead");}
                    ((IRcommand_Binop) h).t2.serial = Integer.parseInt(theNum);
                    ((IRcommand_Binop) h).t2.changed = true;
                }
            }
            if (h.IRname.equals("IRcommand_Assign")) {
                if (!((IRcommand_Assign) h).dst.changed) {
                    String theNum = mips_hash.get(("Temp_" + (((IRcommand_Assign) h).dst.getSerialNumber())));
                    if (theNum == null) {theNum = mips_hash.get("dead");}
                    ((IRcommand_Assign) h).dst.serial = Integer.parseInt(theNum);
                    ((IRcommand_Assign) h).dst.changed = true;
                }
            }
            if (h.IRname.equals("IRcommand_Assign_Ttemp")) {
                String theNum;
                if (!((IRcommand_Assign_Ttemp) h).dst.changed) {
                    theNum = mips_hash.get(("Temp_" + (((IRcommand_Assign_Ttemp) h).dst.getSerialNumber())));
                    if (theNum == null) {theNum = mips_hash.get("dead");}
                    ((IRcommand_Assign_Ttemp) h).dst.serial = Integer.parseInt(theNum);
                    ((IRcommand_Assign_Ttemp) h).dst.changed = true;
                }
                if (!((IRcommand_Assign_Ttemp) h).val.changed) {
                    theNum = mips_hash.get(("Temp_" + (((IRcommand_Assign_Ttemp) h).val.getSerialNumber())));
                    if (theNum == null) {theNum = mips_hash.get("dead");}
                    ((IRcommand_Assign_Ttemp) h).val.serial = Integer.parseInt(theNum);
                    ((IRcommand_Assign_Ttemp) h).val.changed = true;
                }
            }
            if (h.getClass().toString().equals("class IR.IRcommand_Store_Local")) {
                if (!((IRcommand_Store_Local) h).src.changed) {
                    String theNum = mips_hash.get(("Temp_" + (((IRcommand_Store_Local) h).src.getSerialNumber())));
                    if (theNum == null) {theNum = mips_hash.get("dead");}
                    ((IRcommand_Store_Local) h).src.serial = Integer.parseInt(theNum);
                    ((IRcommand_Store_Local) h).src.changed = true;
                }
            }
            if (h.getClass().toString().equals("class IR.IRcommand_Call_Func1")) {
                String theNum;
                if (!((IRcommand_Call_Func1) h).t.changed) {
                    theNum = this.mips_hash.get(("Temp_" + (((IRcommand_Call_Func1) h).t.getSerialNumber())));
                    if (theNum == null) {theNum = this.mips_hash.get("dead");}
                    ((IRcommand_Call_Func1) h).t.serial = Integer.parseInt(theNum);
                    ((IRcommand_Call_Func1) h).t.changed = true;
                }
                TEMP x;
                TEMP_LIST y = ((IRcommand_Call_Func1) h).tempList;
                while (y != null) {
                    x = y.head;
                    if (!x.changed) {
                        theNum = mips_hash.get(("Temp_" + (x.getSerialNumber())));
                        if (theNum == null) {theNum = mips_hash.get("dead");}
                        x.serial = Integer.parseInt(theNum);
                        x.changed = true;
                    }
                    y = y.tail;
                }
            }
            if (h.IRname.equals("class IR.IRcommand_Jump_Conditional")) {
                if ((h.getClass().toString().equals("class IR.IRcommand_Jump_bnez")) || (h.getClass().toString().equals("class IR.IRcommand_Jump_beqz"))) {
                    if (!((IRcommand_Jump_Conditional) h).oprnd1.changed) {
                        String theNum = mips_hash.get(("Temp_" + (((IRcommand_Jump_Conditional) h).oprnd1.getSerialNumber())));
                        if (theNum == null) {theNum = mips_hash.get("dead");}
                        ((IRcommand_Jump_Conditional) h).oprnd1.serial = Integer.parseInt(theNum);
                        ((IRcommand_Jump_Conditional) h).oprnd1.changed = true;
                    }
                } else if ((h.getClass().toString().equals("class IR.IRcommand_Jump_Label"))) {} 
                else {
                    String theNum;
                    if (!((IRcommand_Jump_Conditional) h).oprnd1.changed) {
                        theNum = mips_hash.get(("Temp_" + (((IRcommand_Jump_Conditional) h).oprnd1.getSerialNumber())));
                        if (theNum == null) {theNum = mips_hash.get("dead");}
                        ((IRcommand_Jump_Conditional) h).oprnd1.serial = Integer.parseInt(theNum);
                        ((IRcommand_Jump_Conditional) h).oprnd1.changed = true;
                    }
                    if (!((IRcommand_Jump_Conditional) h).oprnd2.changed) {
                        theNum = mips_hash.get(("Temp_" + (((IRcommand_Jump_Conditional) h).oprnd2.getSerialNumber())));
                        if (theNum == null) {theNum = mips_hash.get("dead");}
                        ((IRcommand_Jump_Conditional) h).oprnd2.serial = Integer.parseInt(theNum);
                        ((IRcommand_Jump_Conditional) h).oprnd2.changed = true;
                    }
                }
            }
            if (h.getClass().toString().equals("class IR.IRcommand_Array_Assign")) {
                String theNum;
                if (!((IRcommand_Array_Assign) h).array.changed) {
                    theNum = mips_hash.get(("Temp_" + (((IRcommand_Array_Assign) h).array.getSerialNumber())));
                    if (theNum == null) {theNum = mips_hash.get("dead");}
                    ((IRcommand_Array_Assign) h).array.serial = Integer.parseInt(theNum);
                    ((IRcommand_Array_Assign) h).array.changed = true;
                }
                if (!((IRcommand_Array_Assign) h).index.changed) {
                    theNum = mips_hash.get(("Temp_" + (((IRcommand_Array_Assign) h).index.getSerialNumber())));
                    if (theNum == null) {theNum = mips_hash.get("dead");}
                    ((IRcommand_Array_Assign) h).index.serial = Integer.parseInt(theNum);
                    ((IRcommand_Array_Assign) h).index.changed = true;
                }
                if (!((IRcommand_Array_Assign) h).val.changed) {
                    theNum = mips_hash.get(("Temp_" + (((IRcommand_Array_Assign) h).val.getSerialNumber())));
                    if (theNum == null) {theNum = mips_hash.get("dead");}
                    ((IRcommand_Array_Assign) h).val.serial = Integer.parseInt(theNum);
                    ((IRcommand_Array_Assign) h).val.changed = true;
                }
            }
            if (h.getClass().toString().equals("class IR.IRcommand_Array_Access")) {
                String theNum;
                if (!((IRcommand_Array_Access) h).dst.changed) {
                    theNum = mips_hash.get(("Temp_" + (((IRcommand_Array_Access) h).dst.getSerialNumber())));
                    if (theNum == null) {theNum = mips_hash.get("dead");}
                    ((IRcommand_Array_Access) h).dst.serial = Integer.parseInt(theNum);
                    ((IRcommand_Array_Access) h).dst.changed = true;
                }
                if (!((IRcommand_Array_Access) h).t1.changed) {
                    theNum = mips_hash.get(("Temp_" + (((IRcommand_Array_Access) h).t1.getSerialNumber())));
                    if (theNum == null) {theNum = mips_hash.get("dead");}
                    ((IRcommand_Array_Access) h).t1.serial = Integer.parseInt(theNum);
                    ((IRcommand_Array_Access) h).t1.changed = true;
                }
                if (!((IRcommand_Array_Access) h).t2.changed) {
                    theNum = mips_hash.get(("Temp_" + (((IRcommand_Array_Access) h).t2.getSerialNumber())));
                    if (theNum == null) {theNum = mips_hash.get("dead");}
                    ((IRcommand_Array_Access) h).t2.serial = Integer.parseInt(theNum);
                    ((IRcommand_Array_Access) h).t2.changed = true;
                }
            }
            if (h.getClass().toString().equals("class IR.IRcommand_Load_Local")) {
                if (!((IRcommand_Load_Local) h).dst.changed) {
                    String theNum = mips_hash.get(("Temp_" + (((IRcommand_Load_Local) h).dst.getSerialNumber())));
                    if (theNum == null) {theNum = mips_hash.get("dead");}
                    ((IRcommand_Load_Local) h).dst.serial = Integer.parseInt(theNum);
                    ((IRcommand_Load_Local) h).dst.changed = true;
                }
            }
            if (h.getClass().toString().equals("class IR.IRcommand_Field_Access")) {
                String theNum;
                if (!((IRcommand_Field_Access) h).dst.changed) {
                    theNum = mips_hash.get(("Temp_" + (((IRcommand_Field_Access) h).dst.getSerialNumber())));
                    if (theNum == null) {theNum = mips_hash.get("dead");}
                    ((IRcommand_Field_Access) h).dst.serial = Integer.parseInt(theNum);
                    ((IRcommand_Field_Access) h).dst.changed = true;
                }
                if (!((IRcommand_Field_Access) h).src.changed) {
                    theNum = mips_hash.get(("Temp_" + (((IRcommand_Field_Access) h).src.getSerialNumber())));
                    if (theNum == null) {theNum = mips_hash.get("dead");}
                    ((IRcommand_Field_Access) h).src.serial = Integer.parseInt(theNum);
                    ((IRcommand_Field_Access) h).src.changed = true;
                }
            }
            if (h.getClass().toString().equals("class IR.IRcommand_Field_New")) {
                String theNum;
                if (!((IRcommand_Field_New) h).dst.changed) {
                    theNum = mips_hash.get(("Temp_" + (((IRcommand_Field_New) h).dst.getSerialNumber())));
                    if (theNum == null) {theNum = mips_hash.get("dead");}
                    ((IRcommand_Field_New) h).dst.serial = Integer.parseInt(theNum);
                    ((IRcommand_Field_New) h).dst.changed = true;
                }
                if (!((IRcommand_Field_New) h).val.changed) {
                    theNum = mips_hash.get(("Temp_" + (((IRcommand_Field_New) h).val.getSerialNumber())));
                    if (theNum == null) {theNum = mips_hash.get("dead");}
                    ((IRcommand_Field_New) h).val.serial = Integer.parseInt(theNum);
                    ((IRcommand_Field_New) h).val.changed = true;
                }
            }
            if (h.getClass().toString().equals("class IR.IRcommand_Field_Store")) {
                if (!((IRcommand_Field_Store) h).val.changed) {
                    String theNum = mips_hash.get(("Temp_" + (((IRcommand_Field_Store) h).val.getSerialNumber())));
                    if (theNum == null) {theNum = mips_hash.get("dead");}
                    ((IRcommand_Field_Store) h).val.serial = Integer.parseInt(theNum);
                    ((IRcommand_Field_Store) h).val.changed = true;
                }
            }
            if (h.getClass().toString().equals("class IR.IRcommand_Set_Return")) {
                if (((IRcommand_Set_Return) h).RetVal != null) {
                    if (!((IRcommand_Set_Return) h).RetVal.changed) {
                        String theNum = mips_hash.get(("Temp_" + (((IRcommand_Set_Return) h).RetVal.getSerialNumber())));
                        if (theNum == null) {theNum = mips_hash.get("dead");}
                        ((IRcommand_Set_Return) h).RetVal.serial = Integer.parseInt(theNum);
                        ((IRcommand_Set_Return) h).RetVal.changed = true;
                    }
                }
            }
            if (h.getClass().toString().equals("class IR.IRcommand_Array_New")) {
                if (!((IRcommand_Array_New) h).dst.changed) {
                    String theNum = mips_hash.get(("Temp_" + (((IRcommand_Array_New) h).dst.getSerialNumber())));
                    if (theNum == null) {theNum = mips_hash.get("dead");}
                    ((IRcommand_Array_New) h).dst.serial = Integer.parseInt(theNum);
                    ((IRcommand_Array_New) h).dst.changed = true;
                }
                if (!((IRcommand_Array_New) h).src.changed) {
                    String theNum = mips_hash.get(("Temp_" + (((IRcommand_Array_New) h).src.getSerialNumber())));
                    if (theNum == null) {theNum = mips_hash.get("dead");}
                    ((IRcommand_Array_New) h).src.serial = Integer.parseInt(theNum);
                    ((IRcommand_Array_New) h).src.changed = true;
                }
            }
            if (h.getClass().toString().equals("class IR.IRcommand_Store_Global")) {
                if (!((IRcommand_Store_Global) h).dst.changed) {
                    String theNum = mips_hash.get(("Temp_" + (((IRcommand_Store_Global) h).dst.getSerialNumber())));
                    if (theNum == null) {theNum = mips_hash.get("dead");}
                    ((IRcommand_Store_Global) h).dst.serial = Integer.parseInt(theNum);
                    ((IRcommand_Store_Global) h).dst.changed = true;
                }
            }
            if (h.getClass().toString().equals("class IR.IRcommand_Set_Dot")) {
                if (!((IRcommand_Set_Dot) h).dst.changed) {
                    String theNum = mips_hash.get(("Temp_" + (((IRcommand_Set_Dot) h).dst.getSerialNumber())));
                    if (theNum == null) {theNum = mips_hash.get("dead");}
                    ((IRcommand_Set_Dot) h).dst.serial = Integer.parseInt(theNum);
                    ((IRcommand_Set_Dot) h).dst.changed = true;
                }
            }
            if (h.getClass().toString().equals("class IR.IRcommand_Call_Virtual")) {
                String theNum;
                if (!((IRcommand_Call_Virtual) h).dst.changed) {
                    theNum = this.mips_hash.get(("Temp_" + (((IRcommand_Call_Virtual) h).dst.getSerialNumber())));
                    if (theNum == null) {theNum = this.mips_hash.get("dead");}
                    ((IRcommand_Call_Virtual) h).dst.serial = Integer.parseInt(theNum);
                    ((IRcommand_Call_Virtual) h).dst.changed = true;
                }
                if (!((IRcommand_Call_Virtual) h).classTemp.changed) {
                    theNum = this.mips_hash.get(("Temp_" + (((IRcommand_Call_Virtual) h).classTemp.getSerialNumber())));
                    if (theNum == null) {theNum = this.mips_hash.get("dead");}
                    ((IRcommand_Call_Virtual) h).classTemp.serial = Integer.parseInt(theNum);
                    ((IRcommand_Call_Virtual) h).classTemp.changed = true;
                }
                TEMP x;
                TEMP_LIST y = ((IRcommand_Call_Virtual) h).args;
                while (y != null) {
                    x = y.head;
                    if (!x.changed) {
                        theNum = mips_hash.get(("Temp_" + (x.getSerialNumber())));
                        if (theNum == null) {theNum = mips_hash.get("dead");}
                        x.serial = Integer.parseInt(theNum);
                        x.changed = true;
                    }
                    y = y.tail;
                }
            }
            h = t.head;
            t = t.tail;
        }
    }
}