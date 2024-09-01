package IR;
import java.io.PrintWriter;
import java.util.*;

public class Block {

	/******************/
	/* [1] Initialize */
	/******************/
    public static ArrayList<Block> allBlocks = new ArrayList<Block>(); 
    public static HashMap<String, Block> labels = new HashMap<String, Block>();   

    public ArrayList<Block> inputs;
    public ArrayList<Block> outputs;
    public ArrayList<IRcommand> body;

    public static Block current = null; 
    public Block next;

    /*******************/
	/* [2] Constractor */
	/*******************/
    protected Block() {
        this.inputs = new ArrayList<Block>();
        this.outputs = new ArrayList<Block>();
        this.body = new ArrayList<IRcommand>();
        this.next = null;
    }

    /****************************/
	/* [2] Additional Functions */
    /****************************/
    public static void GenerateNewCurrentBlock() {
        allBlocks.add(new Block());
        if (current != null) {
            current.next = allBlocks.get(allBlocks.size() - 1);
        }
        current = allBlocks.get(allBlocks.size() - 1);
    }

    public static void Add_IRcommand(IRcommand cmd) {
        current.body.add(cmd);
    }

    public HashSet<String> getoutTransform() {
        return this.body.get(this.body.size() - 1).outTransform;
    }

    public HashSet<String> getinTransform() {
        return this.body.get(0).inTransform;
    }

    public boolean isAllocationBlock() {
        return !this.body.stream().anyMatch(command -> (command instanceof IRcommand_Store || command instanceof IRcommand_Load));
    }
}