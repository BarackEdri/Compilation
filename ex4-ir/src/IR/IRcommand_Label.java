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

public class IRcommand_Label extends IRcommand
{
	String label_name;
	
	public IRcommand_Label(String label_name)
	{
		this.isFirst = true;
		this.label_name = label_name;
	}

	public String toString()
	{
		return label_name + ":";
	}
}
