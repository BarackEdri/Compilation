package TYPES;

public class TYPE_CLASS extends TYPE
{
	/*********************************************************************/
	/* If this class does not extend a father class this should be null  */
	/*********************************************************************/
	public TYPE_CLASS father;

	/**************************************************/
	/* Gather up all data members in one place        */
	/* Note that data members coming from the AST are */
	/* packed together with the class methods         */
	/**************************************************/
	public TYPE_CLASS_VAR_DEC_LIST data_members ;

	public TYPE_CLASS(TYPE_CLASS father, String name, TYPE_CLASS_VAR_DEC_LIST data_members)
	{
		this.name = name;
		this.father = father;
		this.data_members = data_members;
	}

	public TYPE_CLASS(String name, TYPE_CLASS_VAR_DEC_LIST  dataMembers) {
        this.name = name;
        this.data_members = dataMembers;
		this.father = null;
    }

	/*************/
	/* getType() */
	/*************/
	public String getType() { return "TYPE_CLASS"; }
	
	/*************/
	/* isClass() */
	/*************/
	public boolean isClass(){ return true;}

	public boolean isSubClassOf(TYPE_CLASS clazz) {
		if (clazz == null) {
			return false;
		}
		TYPE_CLASS curr = this;
		while (curr != null && curr != clazz) {
			curr = curr.father;
		}
		return curr != null;
	}
}
