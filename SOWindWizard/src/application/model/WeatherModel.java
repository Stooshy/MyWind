package application.model;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.scene.input.KeyCode;

public final class WeatherModel implements Observable
{
	private Weather selected = Weather.Sun;
	private final List<InvalidationListener> listeners;

	public enum Weather
	{
		Sun(0, KeyCode.F2), Clouds(-6d, KeyCode.F3), Rain(-11d, KeyCode.F4);

		public final double weatherFactor;
		public final KeyCode id;


		Weather(double factor, KeyCode code)
		{
			id = code;
			weatherFactor = factor;
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
	}


	public void setSelected(Weather value)
	{
		selected = value;
		notifyListeners();
	}


	public Weather getSelected()
	{
		return selected;
	}


	public double getFactor()
	{
		return getSelected().weatherFactor;
	}


	public String getName()
	{
		return getSelected().name();
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
