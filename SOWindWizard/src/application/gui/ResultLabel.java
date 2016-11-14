package application.gui;

import javafx.beans.InvalidationListener;
import javafx.scene.control.Label;

public abstract class ResultLabel extends Label implements InvalidationListener
{
	protected final String formatString;


	public ResultLabel()
	{
		setId("textnormal");
		formatString = getFormat();
	}


	abstract String getFormat();

}
