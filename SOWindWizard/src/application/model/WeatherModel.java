package application.model;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.input.KeyCode;

public final class WeatherModel implements Observable
{
	private final ObjectProperty<Weather> selected = new SimpleObjectProperty<Weather>();
	private final List<InvalidationListener> listeners;
	
	public enum Weather
	{
		Sun(1.0, KeyCode.F2), Clouds(1.07, KeyCode.F3), Rain(1.13, KeyCode.F4);

		public final double windFactor;
		public final KeyCode id;


		Weather(double factor, KeyCode code)
		{
			id = code;
			windFactor = factor;
		}


		public static Weather getWeather(KeyCode code)
		{
			for (Weather weather : Weather.values())
			{
				if (weather.id == code)
					return weather;
			}
			throw new IllegalArgumentException("Not found: " + code.getName());
		}

	}


	public WeatherModel()
	{
		listeners = new ArrayList<>();
		setSelected(Weather.Sun);
	}


	public void setSelected(Weather value)
	{
		selected.set(value);
		notifyListeners();
	}


	public Weather getSelected()
	{
		return selected.get();
	}
	
	public String getName()
	{
		return getSelected().name();
	}


	public void addListener(ChangeListener<Weather> toAdd)
	{
		selected.addListener(toAdd);
	}


	private void notifyListeners()
	{
		listeners.forEach(listener -> listener.invalidated(this));
	}


	@Override
	public void addListener(InvalidationListener listener)
	{
		listeners.add(listener);
	}


	@Override
	public void removeListener(InvalidationListener listener)
	{
		listeners.remove(listener);

	}

}
