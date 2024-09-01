package TYPES;

public abstract class TYPE
{
	/******************************/
	/*  Every type has a name ... */
	/******************************/
	public String name;

	/*************/
	/* getType() */
	/*************/
	public String getType(){ return "TYPE"; };

	/*************/
	/* isClass() */
	/*************/
	public boolean isClass(){ return false;}

	/*************/
	/* isArray() */
	/*************/
	public boolean isArray(){ return false;}

	/****************/
	/* isFunction() */
	/****************/
	public boolean isFunction(){ return false;}


	/************/
	/* equals() */
	/************/
	public static boolean equals(TYPE t, String s) 
	{
        if (t.getType().equals(s)){ return true; }
		return false;
    }

}
