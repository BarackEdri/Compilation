package TYPES;

public class TYPE_CLASS_VAR_DEC extends TYPE
{
	public TYPE type;
	public String name;
	
	public TYPE_CLASS_VAR_DEC(TYPE type, String name)
	{
		this.type = type;
		this.name = name;
	}

	public String getType() { return "TYPE_CLASS_VAR_DEC"; }
	public boolean isFunction(){ return type.isFunction(); }
}
