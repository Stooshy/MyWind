package application.gui;

import javafx.scene.control.Label;

public abstract class ResultDisplay extends Label
{
	protected String formatString;


	public ResultDisplay()
	{
		setId("textnormal");
	}


	public abstract void setText(double preFix, Object... args);

}
