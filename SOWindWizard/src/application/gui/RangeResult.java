package application.gui;

import application.model.RangeModel;
import application.model.WeatherModel;
import javafx.beans.Observable;

public class RangeResult extends ResultLabel
{
	private String name;
	private double range;


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
		super.setText(String.format(formatString, name, range));
	}


	@Override
	String getFormat()
	{
		return "%s%6.1f";
	}

}
