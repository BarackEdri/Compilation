package IR;

import java.util.*;
import TEMP.*;

public class UninitializedVariableAnalysis {

    public static void analyze(IRcommandList list, Block first) {
        ArrayList<Block> workList = new ArrayList<>();
        if (first.isAllocationBlock()) {
            System.out.println("Skip");
            first = first.next;
        }
        workList.add(first);
        while (!workList.isEmpty()) {
            boolean changed = false;
            Block block = workList.remove(0);
            if (block != null) {
                for (int i = 0; i < block.body.size(); i++) {
                    HashSet<String> newinTransform;
                    IRcommand command = block.body.get(i);
                    
                    if (i == 0) {
                        if (block.inputs.size() == 0) {
                            newinTransform = new HashSet<>(block.getinTransform());
                        } else if (block.inputs.size() == 1) {
                            newinTransform = new HashSet<>(block.inputs.get(0).getoutTransform());
                        } else {
                            newinTransform = new HashSet<>(block.inputs.get(0).getoutTransform());
                            for (int j = 1; j < block.inputs.size(); j++) {
                                HashSet<String> tmp = block.inputs.get(j).getoutTransform();
                                newinTransform.addAll(tmp);
                            }
                        }
                    } else {
                        newinTransform = new HashSet<>(block.body.get(i - 1).outTransform);
                    }
                    
                    command.inTransform = newinTransform;
                    command.transform();
                    
                    if (!newinTransform.equals(command.outTransform)) {
                        changed = true;
                    }

                    if (changed && i == block.body.size() - 1) {
                        for (Block successor : block.outputs) {
                            if (!workList.contains(successor)) {
                                workList.add(successor);
                            }
                        }
                    }
                }
            }
        }
    }
// בעיה בניהול רשימות ה transform.
    public static HashSet<String> findUninitializedAccesses(IRcommandList list) {
        HashMap<String, String> map = new HashMap<>();
        Stack<HashSet<String>> uninitializedVars = new Stack<>();
        uninitializedVars.push(new HashSet<>());
        HashSet<String> theList = new HashSet<>();
        HashSet<String> retList = new HashSet<>();
        IRcommandList it = list;
        HashMap<TEMP, Boolean> tempmap = new HashMap<>();
        int count=0;
        int labelcount=0;
        Boolean wasReturn=true;

        while (it != null) {
            if (it.head instanceof IRcommand_Return) {
                IRcommand_Return returnCmd = (IRcommand_Return) it.head;
                if (returnCmd.returnValue != null) {
                    String returnVar = returnCmd.returnValue.toString();
                    if (!returnCmd.inTransform.contains(returnVar)) {
                         
                        theList.add(returnVar);
                    }
                }
                uninitializedVars.pop();
                labelcount=0;
                while(!(labelcount==-1 )){
                    if(it.head instanceof IRcommand_Label){
                    IRcommand_Label label = (IRcommand_Label) it.head;
                    if (label.label_name.contains("start")) {
                        labelcount+=1;
                    } else if (label.label_name.contains("end")) {
                        labelcount-=1;
                    }
                }
                if(it.head instanceof IRcommand_Jump_If_Eq_To_Zero){
                    labelcount+=1;
                }
                    it = it.tail;
                }
            } else
             if (it.head instanceof IRcommand_Load) {
                IRcommand_Load load = (IRcommand_Load) it.head;
                    String name_id = load.var_name;
                    String programmer_name = name_id;
                    if (uninitializedVars.peek().contains(programmer_name)) {
                    theList.add(programmer_name);
                    tempmap.put(load.dst,false);
                }
                else{
                    tempmap.put(load.dst,true);
                }
            } else if (it.head instanceof IRcommand_Label) {
                
                IRcommand_Label label = (IRcommand_Label) it.head;
                if (label.label_name.contains("start")) {
                    HashSet<String> tempHash = new HashSet<>(uninitializedVars.peek());
                    uninitializedVars.push(tempHash);
                } else if (label.label_name.contains("end")) {
                    uninitializedVars.pop();
                }
            } 
            else if (it.head instanceof IRcommand_Store) {
                IRcommand_Store storeCmd = (IRcommand_Store) it.head;
                String destVarName = storeCmd.var_name;
                if(!(tempmap.containsKey(storeCmd.src)&&tempmap.get(storeCmd.src))){
                    if(!tempmap.containsKey(storeCmd.src)){
                        theList.add(destVarName);
                    }
                    uninitializedVars.peek().add(destVarName);
                    tempmap.put(storeCmd.src,false);
                }
                else{
                    uninitializedVars.peek().remove(destVarName);
                    tempmap.put(storeCmd.src,true);
                }
                  
            } else
            if (it.head instanceof IRcommand_Allocate) {
                IRcommand_Allocate allocate = (IRcommand_Allocate) it.head;
                String name_id = allocate.var_name;
                String programmer_name = name_id;
                map.put(String.valueOf(count), name_id);
                count++;
                uninitializedVars.peek().add(programmer_name);
            }else
            
            
            if (it.head instanceof IRcommand_Jump_If_Eq_To_Zero) {
                IRcommand_Jump_If_Eq_To_Zero jumpCmd = (IRcommand_Jump_If_Eq_To_Zero) it.head;
                String programmer_name = jumpCmd.t.toString();
               HashSet<String> tempHash = new HashSet<>(uninitializedVars.peek());
                uninitializedVars.push(tempHash);
                }
               else if (it.head instanceof IRcommand_Jump_start) {
                    IRcommand_Jump_start jumpCmd = (IRcommand_Jump_start) it.head;
                    String programmer_name = jumpCmd.t.toString();
                    if(uninitializedVars.peek().contains(programmer_name)){
                        theList.add(programmer_name);
                    }
                    }
                
                else
                if (it.head instanceof IRcommandConstInt) {
                    IRcommandConstInt alnum = (IRcommandConstInt) it.head;
                    map.put(String.valueOf(count),String.valueOf(alnum.value));
                    count++;
                    tempmap.put(alnum.t,true);

                }else
                if (it.head instanceof IRcommand_Binop_Mul_Integers) {
                    IRcommand_Binop_Mul_Integers bin = (IRcommand_Binop_Mul_Integers) it.head;
                    if(tempmap.containsKey(bin.t1) && tempmap.containsKey(bin.t2)&& tempmap.get(bin.t1) && tempmap.get(bin.t2)) {
                        tempmap.put(bin.dst, true);
                    } else {
                        tempmap.put(bin.dst, false);
                    }
                } else if (it.head instanceof IRcommand_Binop_Add_Integers) {
                    IRcommand_Binop_Add_Integers bin = (IRcommand_Binop_Add_Integers) it.head;
                     if(tempmap.containsKey(bin.t1) && tempmap.containsKey(bin.t2)&& tempmap.get(bin.t1) && tempmap.get(bin.t2)) {
                        tempmap.put(bin.dst, true);
                    } else {
                        tempmap.put(bin.dst, false);
                    }
                } else if (it.head instanceof IRcommand_Binop_Substract_Integers) {
                    IRcommand_Binop_Substract_Integers bin = (IRcommand_Binop_Substract_Integers) it.head;
                     if(tempmap.containsKey(bin.t1) && tempmap.containsKey(bin.t2)&& tempmap.get(bin.t1) && tempmap.get(bin.t2)) {
                        tempmap.put(bin.dst, true);
                    } else {
                        tempmap.put(bin.dst, false);
                    }
                } else if (it.head instanceof IRcommand_Binop_Divide_Integers) {
                    IRcommand_Binop_Divide_Integers bin = (IRcommand_Binop_Divide_Integers) it.head;
                     if(tempmap.containsKey(bin.t1) && tempmap.containsKey(bin.t2)&& tempmap.get(bin.t1) && tempmap.get(bin.t2)) {
                        tempmap.put(bin.dst, true);
                    } else {
                        tempmap.put(bin.dst, false);
                    }
                } else if (it.head instanceof IRcommand_Binop_EQ_Integers) {
                    IRcommand_Binop_EQ_Integers bin = (IRcommand_Binop_EQ_Integers) it.head;
                    if(tempmap.containsKey(bin.t1) && tempmap.containsKey(bin.t2)&& tempmap.get(bin.t1) && tempmap.get(bin.t2)) {
                        tempmap.put(bin.dst, true);
                    } else {
                        tempmap.put(bin.dst, false);
                    }
                } else if (it.head instanceof IRcommand_Binop_GT_Integers) {
                    IRcommand_Binop_GT_Integers bin = (IRcommand_Binop_GT_Integers) it.head;
                    if(tempmap.containsKey(bin.t1) && tempmap.containsKey(bin.t2)&&tempmap.get(bin.t1) && tempmap.get(bin.t2)) {
                        tempmap.put(bin.dst, true);
                    } else {
                        tempmap.put(bin.dst, false);
                    }
                } else if (it.head instanceof IRcommand_Binop_LT_Integers) {
                    IRcommand_Binop_LT_Integers bin = (IRcommand_Binop_LT_Integers) it.head;
                    if(tempmap.containsKey(bin.t1) && tempmap.containsKey(bin.t2)&& tempmap.get(bin.t1) && tempmap.get(bin.t2)) {
                        tempmap.put(bin.dst, true);
                    } else {
                        tempmap.put(bin.dst, false);
                    }

                } 
            if(it!=null){
            it = it.tail;
        }}

        for(String s: theList){
            s=s.split("@")[0];
            retList.add(s);
        }


        return retList;
    }
}
