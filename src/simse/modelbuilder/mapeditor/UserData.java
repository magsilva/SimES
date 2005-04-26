package simse.modelbuilder.mapeditor;

// this inner class contains all pertinent information about the imported SOP objects
// such as the image associated with it, where it is located on the map, whether it is displayed

//import simse.modelbuilder.graphicsbuilder.matchmaker.*;

import simse.modelbuilder.startstatebuilder.*;
import simse.modelbuilder.objectbuilder.*;
import java.awt.event.*;
import java.awt.Image;
import javax.swing.JMenuItem;

public class UserData
{
	private SimSEObject userObject;
	private String userIconLocation;
	private Image userIcon;

	private boolean userDisplayed;		// is this employee displayed on screen
	private boolean activated;			// is this employee created by a rule, if it's a StartStateObject this is automatically true

	// grid locations of the SimSEObject
	// <xLocation,yLocation> where <0,0> is the top left tile
	private int xLocation;
	private int yLocation;

	private JMenuItem userMenu;

	public UserData(SimSEObject obj, String il, ActionListener a, boolean d, boolean ra, int x, int y)
	{
		userObject = obj;
		userIconLocation = il;
		userIcon = MapData.getImage(il);

		userMenu = new JMenuItem(getName());
		userMenu.addActionListener(a);

		activated = ra;
		setDisplayed(d);
		setXYLocations(x,y);
	}

	
	public Image getUserIcon()
	{
		return userIcon;
	}

	
	public int getXLocation()
	{
		return xLocation;
	}

	
	public int getYLocation()
	{
		return yLocation;
	}

	
	public JMenuItem getUserMenu()
	{
		return userMenu;
	}


	// is user activated by a rule
	public boolean isActivated()
	{
		return activated;
	}

	
	public boolean isDisplayed()
	{
		return userDisplayed;
	}

	
	public void setActivated(boolean a)
	{
		activated = a;
	}

	
	// either sets this user to be displayed and disables JMenu, or viseversa
	public void setDisplayed(boolean b)
	{
		userDisplayed = b;
		userMenu.setEnabled(!b);
	}

	
	public void setXYLocations(int x, int y)
	{
		xLocation = x;
		yLocation = y;
	}

	
	public boolean checkXYLocations(int x, int y)
	{
		return xLocation == x && yLocation == y;
	}

	
	public String getName()
	{
		int type = userObject.getSimSEObjectType().getType();
		String key = new String();
		if(userObject.getKey().isInstantiated())
		{
			key = userObject.getKey().getValue().toString();
		}

		return "- " + SimSEObjectTypeTypes.getText(type) + " - " + userObject.getName() + " - " + key;
	}
	
	
	public SimSEObject getSimSEObject()
	{
		return userObject;
	}
}
