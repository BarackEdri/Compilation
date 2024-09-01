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

public class IRcommand_Load extends IRcommand
{
	TEMP dst;
	String var_name;
	
	public IRcommand_Load(TEMP dst,String var_name)
	{
		this.dst      = dst;
		this.var_name = var_name;
	}

	public void transform() {
		this.outTransform = new HashSet<>(this.inTransform);
		if (this.inTransform.contains(this.var_name)) {
			this.outTransform.add(String.valueOf(this.dst.getSerialNumber()));
		}
	}

	public String toString() {
		return "@" + dst.getSerialNumber() + " = " + var_name + " [0]";
	}
}
