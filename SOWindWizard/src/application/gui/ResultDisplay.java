package application.gui;

import javafx.beans.InvalidationListener;
import javafx.scene.control.Label;

public abstract class ResultDisplay extends Label implements InvalidationListener
{
	protected String formatString;


	public ResultDisplay()
	{
		setId("textnormal");
	}

}
