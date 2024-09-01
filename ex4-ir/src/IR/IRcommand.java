/***********/
/* PACKAGE */
/***********/
package IR;

/*******************/
/* GENERAL IMPORTS */
/*******************/
import java.util.*;

/*******************/
/* PROJECT IMPORTS */
/*******************/

public abstract class IRcommand
{
	/*****************/
	/* Label Factory */
	/*****************/
	protected static int label_counter=0;
	public boolean isFirst = false;

	public HashSet<String> inTransform = new HashSet<>();
	public HashSet<String> outTransform = new HashSet<>();

	public    static String getFreshLabel(String msg)
	{
		return String.format("Label_%d_%s",label_counter++,msg);
	}

	public void transform() {
		this.outTransform = new HashSet<>(this.inTransform);
	}	
	
	public String toString() {
		return "IRCommand";
	}
}
