package application.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;

public final class WeatherModel
{
	private final ObjectProperty<Weather> selected = new SimpleObjectProperty<Weather>();

	public enum Weather
	{
		Sun(1.0), Clouds(0.93), Rain(0.87);

		public final double windFactor;


		Weather(double factor)
		{
			windFactor = factor;
		}
	}


	public WeatherModel()
	{
		setSelected(Weather.Sun);
	}


	public void setSelected(Weather value)
	{
		selected.set(value);
	}


	public Weather getSelected()
	{
		return selected.get();
	}


	public void addListener(ChangeListener<Weather> toAdd)
	{
		selected.addListener(toAdd);
	}

}
