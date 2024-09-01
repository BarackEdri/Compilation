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

public class IRcommand_Binop_Divide_Integers extends IRcommand
{
	public TEMP t1;
	public TEMP t2;
	public TEMP dst;

	public IRcommand_Binop_Divide_Integers(TEMP dst,TEMP t1,TEMP t2)
	{
		this.dst = dst;
		this.t1 = t1;
		this.t2 = t2;
	}

	public void transform() {
		this.outTransform = new HashSet<>(this.inTransform);
		String t1Serial = String.valueOf(this.t1.getSerialNumber());
		String t2Serial = String.valueOf(this.t2.getSerialNumber());

		if (this.inTransform.contains(t1Serial) && this.inTransform.contains(t2Serial)) {
			this.outTransform.add(String.valueOf(this.dst.getSerialNumber()));
		}
	}

	public String toString() {
		return "@" + dst.getSerialNumber() + " = @" + t1.getSerialNumber() + " / @" + t2.getSerialNumber();
	}
}
