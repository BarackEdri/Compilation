package MIPS;

import java.io.*;
import java.util.*;

import IR.*;
import TEMP.*;

public class MIPSGenerator {
    public static String mipsOutputFilename;
    private static MIPSGenerator instance = null;
    private final int WORD_SIZE = 4;
    private PrintWriter fileWriter;

    protected MIPSGenerator() {}

	/***********/
	/* declare */
	/***********/
    public void declareClass(String class_name, ArrayList<ArrayList<String>> funcs) {
        fileWriter.format(".data\n");
        fileWriter.format("vt_%s:\n", class_name);
        if (funcs.size() == 0){fileWriter.format("\t.word 0\n");}
        else
            for (int i = 0; i < funcs.size(); i++) {
                String n = (funcs.get(i)).get(1) + "_";
                n += (funcs.get(i)).get(0);
                fileWriter.format("\t.word %s\n", n);
            }
        fileWriter.format(".text\n");
    }
    public void declareString(String label, String id, String value) {
        fileWriter.format(".data\n");
        fileWriter.format("%s: .asciiz \"%s\"\n", label, value);
        fileWriter.format("%s: .word %s\n", id, label);
        fileWriter.format("\n.text\n");
    }
    public void declareInt(String id, int value) {
        fileWriter.format(".data\n");
        fileWriter.format("%s: .word %d\n", id, value);
        fileWriter.format("\n.text\n");
    }
    public void declareObject(String id) {
        fileWriter.format(".data\n");
        fileWriter.format("%s: .word 0\n", id);
        fileWriter.format("\n.text\n");
    }

	/**********/
	/* access */
	/**********/
    public void fieldAccess(TEMP t1, int offset, TEMP t2) {
        int t1_id = t1.getSerialNumber();
        int t2_id = t2.getSerialNumber();
        fileWriter.format("\tbeq $t%d, 0, invalid_pointer_dereference\n", t2_id);
        fileWriter.format("\tlw $t%d, %d($t%d)\n", t1_id, offset, t2_id);
    }
    public void arrayAccess(TEMP t0, TEMP t1, TEMP t2) {
        int t0_id = t0.getSerialNumber();
        int t1_id = t1.getSerialNumber();
        int t2_id = t2.getSerialNumber();
        fileWriter.format("\tbeq $t%d, 0, invalid_pointer_dereference\n", t1_id);
        fileWriter.format("\tbltz $t%d, out_of_bound\n", t2_id);
        fileWriter.format("\tlw $s0, 0($t%d)\n", t1_id);
        fileWriter.format("\tbge $t%d, $s0, out_of_bound\n", t2_id);
        fileWriter.format("\tmove $s0, $t%d\n", t2_id);
        fileWriter.format("\tadd $s0, $s0, 1\n");
        fileWriter.format("\tmul $s0, $s0, 4\n");
        fileWriter.format("\taddu $s0, $t%d, $s0\n", t1_id);
        fileWriter.format("\tlw $t%d, 0($s0)\n", t0_id);
    }

	/*******/
	/* set */
	/*******/
    public void fieldSet(TEMP t1, int off, TEMP val) {
        int t1_id = t1.getSerialNumber();
        int valid = val.getSerialNumber();
        fileWriter.format("\tbeq $t%d, 0, invalid_pointer_dereference\n", t1_id);
        fileWriter.format("\tla $s0, %d($t%d)\n", off, t1_id);
        fileWriter.format("\tsw $t%d, 0($s0)\n", valid);
    }
    public void setField(TEMP t0, String label, int offset) {
        int t0_id = t0.getSerialNumber();
        fileWriter.format("\tlw $s0, %s\n", label);
        fileWriter.format("\tsw $s0, %d($t%d)\n", offset, t0_id);
    }
    public void arraySet(TEMP array, TEMP index, TEMP val) {
        int array_id = array.getSerialNumber();
        int index_id = index.getSerialNumber();
        int val_id = val.getSerialNumber();
        fileWriter.format("\tbeq $t%d, 0, invalid_pointer_dereference\n", array_id);
        fileWriter.format("\tbltz $t%d, out_of_bound\n", index_id);
        fileWriter.format("\tlw $s0, 0($t%d)\n", array_id);
        fileWriter.format("\tbge $t%d, $s0, out_of_bound\n", index_id);
        fileWriter.format("\tmove $s0, $t%d\n", index_id);
        fileWriter.format("\tadd $s0, $s0, 1\n");
        fileWriter.format("\tmul $s0, $s0, 4\n");
        fileWriter.format("\taddu $s0, $t%d, $s0\n", array_id);
        fileWriter.format("\tsw $t%d, 0($s0)\n", val_id);
    }

	/********/
	/* call */
	/********/
    public void virtualCall(TEMP dst, TEMP classTemp, int offset, TEMP_LIST args) {
        int cnt = 1;
        fileWriter.format("\tsubu $sp, $sp, 4\n");
        while (args != null) {
            cnt++;
            int num = args.head.getSerialNumber();
            fileWriter.format("\tsw $t%d, 0($sp)\n", num);
            fileWriter.format("\tsubu $sp, $sp, 4\n");
            args = args.tail;
        }
        int classix = classTemp.getSerialNumber();
        fileWriter.format("\tsw $t%d, 0($sp) \n", classix);
        fileWriter.format("\tlw $s0, 0($t%d)\n", classix);
        fileWriter.format("\tlw $s1, %d($s0)\n", offset);
        fileWriter.format("\tjalr $s1\n");
        fileWriter.format("\taddu $sp, $sp,%d\n", 4 * cnt);
        int dstix = dst.getSerialNumber();
        fileWriter.format("\tmove $t%d, $v0\n", dstix);
    }
    public void funcCall(TEMP t, String startLabel, TEMP_LIST reversedTempList) {
        int cnt = 0;
        int t_id = t.getSerialNumber();
        for (TEMP_LIST it = reversedTempList; it != null; it = it.tail) {
            cnt++;
            TEMP curr = it.head;
            int currInd = curr.getSerialNumber();
            fileWriter.format("\tsubu $sp, $sp, 4\n");
            fileWriter.format("\tsw $t%d, 0($sp)\n", currInd);
        }
        fileWriter.format("\tjal %s\n", startLabel);
        fileWriter.format("\taddu $sp, $sp, %d\n", 4 * cnt);
        fileWriter.format("\tmove $t%d, $v0\n", t_id);
    }

	/********/
	/* load */
	/********/
    public void localLoad(TEMP dst, int offset) {
        int dest_id = dst.getSerialNumber();
        fileWriter.format("\tlw $t%d,%d($fp)\n", dest_id, offset);
    }
    public void labelLoad(TEMP dst, String label) {
        int dest_id = dst.getSerialNumber();
        fileWriter.format("\tlw $t%d, %s\n", dest_id, label);
    }
    public void fieldLoad(TEMP dst, int offset) {
        int id = dst.getSerialNumber();
        fileWriter.format("\t lw $s0, 8($fp)\n");
        fileWriter.format("\t lw $t%d, %d($s0)\n", id, offset);
    }

	/*********/
	/* store */
	/*********/
    public void localStore(TEMP dst, int offset) {
        int dest_id = dst.getSerialNumber();
        fileWriter.format("\tsw $t%d, %d($fp)\n", dest_id, offset);
    }
    public void labelStore(TEMP dst, String label) {
        int dest_id = dst.getSerialNumber();
        fileWriter.format("\tsw $t%d, %s\n", dest_id, label);
    }
    public void fieldStore(int offset, TEMP val) {
        int id = val.getSerialNumber();
        fileWriter.format("\tlw $s0, 8($fp)\n");
        fileWriter.format("\tsw $t%d, %d($s0)\n", id, offset);
    }

	/************/
	/* prologue */
	/************/
    public void prologue(int vars) {
        fileWriter.format("\tsubu $sp, $sp, 4\n");
        fileWriter.format("\tsw $ra, 0($sp)\n");
        fileWriter.format("\tsubu $sp, $sp, 4\n");
        fileWriter.format("\tsw $fp, 0($sp)\n");
        fileWriter.format("\tmove $fp, $sp\n");
        fileWriter.format("\tsubu $sp, $sp, 4\n");
        fileWriter.format("\tsw $t0, 0($sp)\n");
        fileWriter.format("\tsubu $sp, $sp, 4\n");
        fileWriter.format("\tsw $t1, 0($sp)\n");
        fileWriter.format("\tsubu $sp, $sp, 4\n");
        fileWriter.format("\tsw $t2, 0($sp)\n");
        fileWriter.format("\tsubu $sp, $sp, 4\n");
        fileWriter.format("\tsw $t3, 0($sp)\n");
        fileWriter.format("\tsubu $sp, $sp, 4\n");
        fileWriter.format("\tsw $t4, 0($sp)\n");
        fileWriter.format("\tsubu $sp, $sp, 4\n");
        fileWriter.format("\tsw $t5, 0($sp)\n");
        fileWriter.format("\tsubu $sp, $sp, 4\n");
        fileWriter.format("\tsw $t6, 0($sp)\n");
        fileWriter.format("\tsubu $sp, $sp, 4\n");
        fileWriter.format("\tsw $t7, 0($sp)\n");
        fileWriter.format("\tsubu $sp, $sp, 4\n");
        fileWriter.format("\tsw $t8, 0($sp)\n");
        fileWriter.format("\tsubu $sp, $sp, 4\n");
        fileWriter.format("\tsw $t9, 0($sp)\n");
        fileWriter.format("\tsub $sp, $sp, %d\n", vars * 4);
    }

	/************/
	/* epilogue */
	/************/
    public void epilogue() {
        fileWriter.format("\tmove $sp, $fp\n");
        fileWriter.format("\tlw $t0, -4($sp)\n");
        fileWriter.format("\tlw $t1, -8($sp)\n");
        fileWriter.format("\tlw $t2, -12($sp)\n");
        fileWriter.format("\tlw $t3, -16($sp)\n");
        fileWriter.format("\tlw $t4, -20($sp)\n");
        fileWriter.format("\tlw $t5, -24($sp)\n");
        fileWriter.format("\tlw $t6, -28($sp)\n");
        fileWriter.format("\tlw $t7, -32($sp)\n");
        fileWriter.format("\tlw $t8, -36($sp)\n");
        fileWriter.format("\tlw $t9, -40($sp)\n");
        fileWriter.format("\tlw $fp, 0($sp)\n");
        fileWriter.format("\tlw $ra, 4($sp)\n");
        fileWriter.format("\taddu $sp, $sp, 8\n");
        fileWriter.format("\tjr $ra\n");
        fileWriter.format("\n");
    }

	/***********************/
	/* register operations */
	/***********************/
    public void add(TEMP dst, TEMP op1, TEMP op2) {
        int i1 = op1.getSerialNumber();
        int i2 = op2.getSerialNumber();
        int dst_id = dst.getSerialNumber();
        fileWriter.format("\tadd $t%d,$t%d,$t%d\n", dst_id, i1, i2);
        overflowChecking(dst);
        underflowChecking(dst);
    }
    public void sub(TEMP dst, TEMP op1, TEMP op2) {
        int i1 = op1.getSerialNumber();
        int i2 = op2.getSerialNumber();
        int dst_id = dst.getSerialNumber();
        fileWriter.format("\tsub $t%d,$t%d,$t%d\n", dst_id, i1, i2);
        underflowChecking(dst);
        overflowChecking(dst);
    }
    public void mul(TEMP dst, TEMP op1, TEMP op2) {
        int i1 = op1.getSerialNumber();
        int i2 = op2.getSerialNumber();
        int dst_id = dst.getSerialNumber();
        fileWriter.format("\tmul $t%d,$t%d,$t%d\n", dst_id, i1, i2);
        overflowChecking(dst);
        underflowChecking(dst);
    }
    public void div(TEMP dst, TEMP op1, TEMP op2) {
        int i1 = op1.getSerialNumber();
        int i2 = op2.getSerialNumber();
        int dst_id = dst.getSerialNumber();
        beqz(op2, "division_by_zero");
        fileWriter.format("\tdiv $t%d,$t%d,$t%d\n", dst_id, i1, i2);
        underflowChecking(dst);
        overflowChecking(dst);
    }
    public void li(TEMP dst, int val) {
        int dst_id = dst.getSerialNumber();
        fileWriter.format("\tli $t%d, %d\n", dst_id, val);
    }
    public void move(TEMP dst, TEMP value) {
        int val = value.getSerialNumber();
        int dst_id = dst.getSerialNumber();
        fileWriter.format("\tmove $t%d,$t%d\n", dst_id, val);
    }
    public void label(String inlabel) {
        fileWriter.format("\n");
        fileWriter.format("%s:\n", inlabel);
    }
    public void jump(String inlabel) {
        fileWriter.format("\tj %s\n", inlabel);
    }
    public void blt(TEMP op1, TEMP op2, String label) {
        int i1 = op1.getSerialNumber();
        int i2 = op2.getSerialNumber();
        fileWriter.format("\tblt $t%d,$t%d,%s\n", i1, i2, label);
    }
    public void bgt(TEMP op1, TEMP op2, String label) {
        int i1 = op1.getSerialNumber();
        int i2 = op2.getSerialNumber();
        fileWriter.format("\tbgt $t%d,$t%d,%s\n", i1, i2, label);
    }
    public void bge(TEMP op1, TEMP op2, String label) {
        int i1 = op1.getSerialNumber();
        int i2 = op2.getSerialNumber();
        fileWriter.format("\tbge $t%d,$t%d,%s\n", i1, i2, label);
    }
    public void ble(TEMP op1, TEMP op2, String label) {
        int i1 = op1.getSerialNumber();
        int i2 = op2.getSerialNumber();
        fileWriter.format("\tble $t%d,$t%d,%s\n", i1, i2, label);
    }
    public void bne(TEMP op1, TEMP op2, String label) {
        int i1 = op1.getSerialNumber();
        int i2 = op2.getSerialNumber();
        fileWriter.format("\tbne $t%d,$t%d,%s\n", i1, i2, label);
    }
    public void beq(TEMP op1, TEMP op2, String label) {
        int i1 = op1.getSerialNumber();
        int i2 = op2.getSerialNumber();
        fileWriter.format("\tbeq $t%d,$t%d,%s\n", i1, i2, label);
    }
    public void beqz(TEMP op1, String label) {
        int i1 = op1.getSerialNumber();
        fileWriter.format("\tbeq $t%d,$zero,%s\n", i1, label);
    }
    public void bnez(TEMP op1, String label) {
        int i1 = op1.getSerialNumber();
        fileWriter.format("\tbne $t%d,$zero,%s\n", i1, label);
    }
    public void ret(TEMP val) {
        if (val != null) {
            int idx = val.getSerialNumber();
            fileWriter.format("\tmove $v0, $t%d\n", idx);
        }
        epilogue();
    }

	/************/
	/* allocate */
	/************/
    public void stringAllocate(TEMP dst, String label, String val) {
        int dst_id = dst.getSerialNumber();
        fileWriter.format(".data\n");
        fileWriter.format("%s: .asciiz \"%s\"\n", label, val);
        fileWriter.format(".text\n");
        fileWriter.format("\tla $t%d, %s\n", dst_id, label);
    }
    public void arrayAllocate(TEMP t0, TEMP t1) {
        int t0_id = t0.getSerialNumber();
        int t1_id = t1.getSerialNumber();
        fileWriter.format("\tli $v0, 9\n");
        fileWriter.format("\tmove $a0, $t%d\n", t1_id);
        fileWriter.format("\tadd $a0, $a0, 1\n");
        fileWriter.format("\tmul $a0, $a0, 4\n");
        fileWriter.print("\tsyscall\n");
        fileWriter.format("\tmove $t%d, $v0\n", t0_id);
        fileWriter.format("\tsw $t%d, 0($t%d)\n", t1_id, t0_id);
    }
    public void classAllocate(TEMP t0, String className, int size) {
        int t0_id = t0.getSerialNumber();
        fileWriter.format("\tli $v0, 9\n");
        fileWriter.format("\tli $a0, %d\n", size);
        fileWriter.print("\tsyscall\n");
        fileWriter.format("\tmove $t%d, $v0\n", t0_id);
        fileWriter.format("\tla $s0, vt_%s\n", className);
        fileWriter.format("\tsw $s0, 0($t%d)\n", t0_id);
    }

	/**************/
	/* more funcs */
	/**************/
    public void overflowChecking(TEMP dst) {
        String label = IRcommand.getFreshLabel("not_over");
        int ind = dst.getSerialNumber();
        int max = 32767;
        fileWriter.format("\tli $s0, %d\n", max);
        fileWriter.format("\tbge $s0, $t%d, %s\n", ind, label);
        fileWriter.format("\tli $t%d, %d\n", ind, max);
        fileWriter.format("%s:\n", label);
    }
    public void underflowChecking(TEMP dst) {
        String label = IRcommand.getFreshLabel("not_under");
        int ind = dst.getSerialNumber();
        int min = -32768;
        fileWriter.format("\tli $s0, %d\n", min);
        fileWriter.format("\tble $s0, $t%d, %s\n", ind, label);
        fileWriter.format("\tli $t%d, %d\n", ind, min);
        fileWriter.format("%s:\n", label);
    }
    public void concat_strings(TEMP dst, TEMP str1, TEMP str2, String[] labels) {
        int st1_id = str1.getSerialNumber();
        int st2_id = str2.getSerialNumber();
        int dst_id = dst.getSerialNumber();
        MIPSGenerator.getInstance().label(labels[0]);
        fileWriter.format("\tmove $s0, $t%d\n", st1_id);
        fileWriter.format("\tmove $s1, $t%d\n", st2_id);
        fileWriter.format("\tli $s2, 0\n");
        MIPSGenerator.getInstance().label(labels[1]);
        fileWriter.format("\tlb $s3, 0($s0)\n");
        fileWriter.format("\tbeq $s3, 0, %s\n", labels[2]);
        fileWriter.format("\taddu $s0, $s0, 1\n");
        fileWriter.format("\taddu $s2, $s2, 1\n");
        MIPSGenerator.getInstance().jump(labels[1]);
        MIPSGenerator.getInstance().label(labels[2]);
        fileWriter.format("\tlb $s3, 0($s1)\n");
        fileWriter.format("\tbeq $s3, 0, %s\n", labels[3]);
        fileWriter.format("\taddu $s1, $s1, 1\n");
        fileWriter.format("\taddu $s2, $s2, 1\n");
        MIPSGenerator.getInstance().jump(labels[2]);
        MIPSGenerator.getInstance().label(labels[3]);
        fileWriter.format("\taddu $s2, $s2, 1\n");
        fileWriter.format("\tmove $a0, $s2\n", st1_id);
        fileWriter.format("\tli $v0, 9\n");
        instance.fileWriter.format("\tsyscall\n");
        fileWriter.format("\tmove $s0, $t%d\n", st1_id);
        fileWriter.format("\tmove $s1, $t%d\n", st2_id);
        fileWriter.format("\tmove $s2, $v0\n");
        MIPSGenerator.getInstance().label(labels[4]);
        fileWriter.format("\tlb $s3, 0($s0)\n");
        fileWriter.format("\tbeq $s3, 0, %s\n", labels[5]);
        fileWriter.format("\tsb $s3, 0($s2)\n");
        fileWriter.format("\taddu $s0, $s0, 1\n");
        fileWriter.format("\taddu $s2, $s2, 1\n");
        MIPSGenerator.getInstance().jump(labels[4]);
        MIPSGenerator.getInstance().label(labels[5]);
        fileWriter.format("\tlb $s3, 0($s1)\n");
        fileWriter.format("\tbeq $s3, 0, %s\n", labels[6]);
        fileWriter.format("\tsb $s3, 0($s2)\n");
        fileWriter.format("\taddu $s1, $s1, 1\n");
        fileWriter.format("\taddu $s2, $s2, 1\n");
        MIPSGenerator.getInstance().jump(labels[5]);
        MIPSGenerator.getInstance().label(labels[6]);
        fileWriter.format("\taddu $s2, $s2, 1\n");
        fileWriter.format("\tli $s3, 0\n");
        fileWriter.format("\tsb $s3, 0($s2)\n");
        fileWriter.format("\tmove $t%d, $v0\n", dst_id);
    }
    public void eq_strings(TEMP dst, TEMP t1, TEMP t2, String[] labels) {
        int dst_id = dst.getSerialNumber();
        int t1_id = t1.getSerialNumber();
        int t2_id = t2.getSerialNumber();
        fileWriter.format("\tli $t%d, 1\n", dst_id);
        fileWriter.format("\tmove $s0, $t%d\n", t1_id);
        fileWriter.format("\tmove $s1, $t%d\n", t2_id);
        label(labels[0]);
        fileWriter.format("\tlb $s2, 0($s0)\n");
        fileWriter.format("\tlb $s3, 0($s1)\n");
        fileWriter.format("\tbne $s2, $s3, %s\n", labels[1]);
        fileWriter.format("\tbeq $s2, 0, %s\n", labels[2]);
        fileWriter.format("\taddu $s0, $s0, 1\n");
        fileWriter.format("\taddu $s1, $s1, 1\n");
        jump(labels[0]);
        label(labels[1]);
        fileWriter.format("\tli $t%d, 0\n", dst_id);
        label(labels[2]);
    }
    public void finalizeFile() {
        fileWriter.print("\t\n");
        fileWriter.print("main:\n");
        fileWriter.print("\tjal user_main\n");
        fileWriter.print("\tli $v0, 10\n");
        fileWriter.print("\tsyscall\n");
        fileWriter.close();
    }
    public static MIPSGenerator getInstance() {
        if (instance == null) {
            instance = new MIPSGenerator();
            try {instance.fileWriter = new PrintWriter(mipsOutputFilename);}
            catch (Exception e) {e.printStackTrace();}
            instance.fileWriter.print(".data\n");
            instance.fileWriter.print("string_access_violation: .asciiz \"Access Violation\"\n");
            instance.fileWriter.print("string_invalid_ptr_dref: .asciiz \"Invalid Pointer Dereference\"\n");
            instance.fileWriter.print("string_illegal_div_by_0: .asciiz \"Illegal Division By Zero\"\n");
            instance.fileWriter.print("string_space: .asciiz \" \"\n");
            instance.fileWriter.print("string_tab: .asciiz \"\t\"\n");
            instance.fileWriter.format("\n.text\n");
            instance.label("division_by_zero");
            instance.fileWriter.format("\tla $a0, string_illegal_div_by_0\n");
            instance.fileWriter.format("\tli $v0, 4\n");
            instance.fileWriter.format("\tsyscall\n");
            instance.fileWriter.format("\tli $v0, 10\n");
            instance.fileWriter.format("\tsyscall\n");
            instance.label("invalid_pointer_dereference");
            instance.fileWriter.format("\tla $a0, string_invalid_ptr_dref\n");
            instance.fileWriter.format("\tli $v0,4\n");
            instance.fileWriter.format("\tsyscall\n");
            instance.fileWriter.format("\tli $v0,10\n");
            instance.fileWriter.format("\tsyscall\n");
            instance.label("out_of_bound");
            instance.fileWriter.format("\tla $a0, string_access_violation\n");
            instance.fileWriter.format("\tli $v0, 4\n");
            instance.fileWriter.format("\tsyscall\n");
            instance.fileWriter.print("\tli $v0, 10\n");
            instance.fileWriter.print("\tsyscall\n");
            instance.fileWriter.format(".text\n");
            MIPSGenerator.getInstance().label("PrintInt");
            MIPSGenerator.getInstance().prologue(0);
            instance.fileWriter.format("\tlw $a0, 8($fp)\n");
            instance.fileWriter.format("\tli $v0, 1\n");
            instance.fileWriter.format("\tsyscall\n");
            instance.fileWriter.format("\tla $a0, string_space\n");
            instance.fileWriter.format("\tli $v0, 4\n");
            instance.fileWriter.format("\tsyscall\n");
            MIPSGenerator.getInstance().epilogue();
            MIPSGenerator.getInstance().label("PrintString");
            MIPSGenerator.getInstance().prologue(0);
            instance.fileWriter.format("\tlw $a0, 8($fp)\n");
            instance.fileWriter.format("\tli $v0, 4\n");
            instance.fileWriter.format("\tsyscall\n");
            MIPSGenerator.getInstance().epilogue();
        }
        return instance;
    }
}
