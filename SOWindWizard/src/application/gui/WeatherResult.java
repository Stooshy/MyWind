package application.gui;

import application.model.WeatherModel;
import javafx.scene.control.Label;

public class WeatherResult extends Label
{

	private final WeatherModel selectionModel;
	private final String formatString;


	public WeatherResult(WeatherModel model)
	{
		selectionModel = model;
		formatString = "%s%6.1f";
		setId("textnormal");
	}


	public void setText(Object... args)
	{
		Object[] formatArgs = args;
		int n = formatArgs.length;
		if (n < 1)
			throw new IllegalArgumentException("Not enough arguments: 1");

		super.setText(String.format(formatString, selectionModel.getSelected().name(),  formatArgs[0]));
	}

}
