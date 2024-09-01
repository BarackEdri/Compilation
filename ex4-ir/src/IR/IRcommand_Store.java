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

public class IRcommand_Store extends IRcommand
{
	String var_name;
	TEMP src;
	
	public IRcommand_Store(String var_name,TEMP src)
	{
		this.src      = src;
		this.var_name = var_name;
		System.out.println("Storing " + src + " in " + var_name);
	}
	
	public void transform() {
		this.outTransform = new HashSet<>(this.inTransform);
		if (this.inTransform.contains(String.valueOf(this.src.getSerialNumber()))) {
			this.outTransform.add(this.var_name);
			System.out.println("Adding " + this.var_name + " to outTransform");
		}
	}

	public String toString() {
		return var_name + " [0] = @" + src.getSerialNumber();
	}
}
