/* This class defines a user action type trigger */

package simse.modelbuilder.actionbuilder;

public class UserActionTypeTrigger extends ActionTypeTrigger implements Cloneable
{
	private String menuText; // what should appear on a user's menu for performing this action

	public UserActionTypeTrigger(String name, ActionType action, String text)
	{
		super(name, action);
		menuText = text;
	}


	public UserActionTypeTrigger(String name, ActionType action)
	{
		super(name, action);
		menuText = new String();
	}
	
	
	public Object clone()
	{
		UserActionTypeTrigger cl = (UserActionTypeTrigger)(super.clone());
		cl.menuText = menuText;
		return cl;
	}	


	public String getMenuText()
	{
		return menuText;
	}


	public void setMenuText(String newText)
	{
		menuText = newText;
	}
}
