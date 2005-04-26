package simse.modelbuilder.mapeditor;

import java.awt.event.*;
import java.awt.*;
import javax.swing.*;

public class PopupListener extends MouseAdapter
{
	JPopupMenu popup;
	boolean enabled;

	
	public PopupListener(JPopupMenu popupMenu)
	{
		popup = popupMenu;
		enabled = true;
	}

	
	public boolean isEnabled()
	{
		return enabled;
	}

	
	public void setEnabled(boolean e)
	{
		enabled = e;
	}

	public void mousePressed(MouseEvent e){}

	
	public void mouseReleased(MouseEvent e)
	{
		maybeShowPopup(e);
	}

	
	private void maybeShowPopup(MouseEvent e)
	{
		if (e.isPopupTrigger() && isEnabled())
		{
			popup.show(e.getComponent(), e.getX(), e.getY());
		}
	}
}
