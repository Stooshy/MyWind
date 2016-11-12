package application.gui;

import application.model.RangeModel;
import application.model.WeatherModel;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.scene.control.Label;

public class Result extends Label implements InvalidationListener
{
	private String name;
	private double range;


	public Result()
	{
		setId("textnormal");
	}


	@Override
	public void invalidated(Observable observable)
	{
		if (observable instanceof RangeModel)
		{
			RangeModel rangeModel = (RangeModel) observable;
			range = rangeModel.getTrueRange();

		}
		else if (observable instanceof WeatherModel)
		{
			WeatherModel weatherModel = (WeatherModel) observable;
			name = weatherModel.getName();
		}
		super.setText(String.format("%s%6.1f", name, range));
	}

}
