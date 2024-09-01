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
import TEMP.*;

public class IRcommandConstInt extends IRcommand
{
	TEMP t;
	int value;
	
	public IRcommandConstInt(TEMP t,int value)
	{
		this.t = t;
		this.value = value;
	}

	public void transform() {
		this.outTransform = new HashSet<>(this.inTransform);
		this.outTransform.add(String.valueOf(this.t.getSerialNumber()));
	}

	public String toString() {
		return "@" + t.getSerialNumber() + " = " + value;
	}
}
