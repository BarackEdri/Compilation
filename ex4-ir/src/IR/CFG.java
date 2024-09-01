package IR;

public class CFG {

    public static void firstCheck(IRcommandList list) {
        IRcommandList it = list;
        boolean first_check = false;
        while (it != null) {
            if (first_check) {
                it.head.isFirst = true;
                first_check = false;
            }
            if (it.head instanceof IRcommand_Jump_If_Eq_To_Zero || it.head instanceof IRcommand_Jump_Label|| it.head instanceof IRcommand_Label) {
                first_check = true;
            }
            it = it.tail;
        }
    }

    public static Block connectBlocks(Block first) {
        Block it = first;
        while (it != null) {
            if (it.body.size() > 0) {
                IRcommand last = it.body.get(it.body.size() - 1);
                if (last instanceof IRcommand_Jump_If_Eq_To_Zero) {
                    // קבלת הבלוק המייצג את גוף הלולאה
                    Block loopBody = Block.labels.get(((IRcommand_Jump_If_Eq_To_Zero) last).label_name);
                    if (loopBody != null) {
                        // חיבור בלוק הבדיקה לתחילת גוף הלולאה
                        connectBlockPair(it, loopBody);
    
                        // יצירת בלוק חדש עבור האיטרציה החדשה
                        Block newIterationBlock = new Block();
                        connectBlockPair(loopBody, newIterationBlock);
    
                        // חיבור לבלוק הבא מחוץ ללולאה במקרה שהקפיצה לא מתבצעת
                        if (it.next != null) {
                            connectBlockPair(it, it.next);
                        }
                    }
                } else {
                    if (it.next != null) {
                        connectBlockPair(it, it.next);
                    }
                }
            }
            it = it.next;
        }
        return first;
    }
    
    
    public static Block generateBlockCFG(IRcommandList list) {
        IRcommandList it = list;
        Block.GenerateNewCurrentBlock(); // יצירת בלוק ראשוני
        Block first = Block.current;
        while (it != null) {
            // בדיקה אם הפקודה הנוכחית היא התחלה של בלוק חדש
            if (it.head.isFirst && Block.current.body.size() > 0) {
                Block.GenerateNewCurrentBlock(); // יצירת בלוק חדש
            }
            // בדיקת תווית
            if (it.head instanceof IRcommand_Label) {
                IRcommand_Label labelCommand = (IRcommand_Label) it.head;
                Block.labels.put(labelCommand.label_name, Block.current);
                
                // אם התווית היא "end", סיום בלוק נוכחי והתחלת בלוק חדש
                if ("end".equals(labelCommand.label_name)) {
                    Block.GenerateNewCurrentBlock(); // סיום בלוק נוכחי והתחלת בלוק חדש
                }
            }
            Block.Add_IRcommand(it.head); // הוספת הפקודה הנוכחית לבלוק
            it = it.tail; // מעבר לפקודה הבאה
        }
        return first;
    }
    

    public static void connectBlockPair(Block from, Block to) {
        from.outputs.add(to);
        to.inputs.add(from);
        System.out.println("Connected block from " + from + " to " + to);
    }

    public static Block generateCFG(IRcommandList list) {
        firstCheck(list);
        Block first = generateBlockCFG(list);
        connectBlocks(first);

        return first;
    }
}
