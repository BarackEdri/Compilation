/***********/
/* PACKAGE */
/***********/
package IR;

/*******************/
/* GENERAL IMPORTS */
/*******************/

/*******************/
/* PROJECT IMPORTS */
/*******************/
import TEMP.*;

public class IRcommand_Jump_start extends IRcommand
{
	TEMP t;
	String label_name;
	
	public IRcommand_Jump_start(TEMP t, String label_name)
	{
		this.t          = t;
		this.label_name = label_name;
	}

	public String toString()
	{
		return String.format("IRcommand_Jump_start(@%s,%s)",t.getSerialNumber(),label_name);
	}
}
