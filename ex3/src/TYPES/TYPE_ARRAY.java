package TYPES;

public class TYPE_ARRAY extends TYPE
{
	public TYPE type;

	public TYPE_ARRAY(TYPE type, String name) 
	{
		this.type = type;
		this.name = name;
	}
  	
	/*************/
	/* getType() */
	/*************/
	public String getType() { return "TYPE_ARRAY"; }
	
	/*************/
	/* isArray() */
	/*************/
	public boolean isArray(){ return true;}
}
