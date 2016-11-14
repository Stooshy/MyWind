package application.model;

import java.util.ArrayList;
import java.util.List;

import application.model.WeatherModel.Weather;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;

public final class RangeModel implements Observable, InvalidationListener
{

	private final DoubleProperty trueRange;
	private final DoubleProperty windValue;
	private int direction;
	private double[] windDrifts;
	private double range;
	private Weather weather;
	private final List<InvalidationListener> listeners;


	public RangeModel()
	{
		listeners = new ArrayList<>();
		windValue = new SimpleDoubleProperty(1.0);
		trueRange = new SimpleDoubleProperty(100);
		weather = Weather.Sun;
		setWind("1.0");
	}


	public double getTrueRange()
	{
		return trueRange.get();
	}


	private void setWeather(Weather value)
	{
		weather = value;
		calcTrueRange();
	}


	private void setTrueRange(double value)
	{
		trueRange.set(value);
		notifyListeners();
	}


	public void setWind(String value)
	{
		try
		{
			double _value = Double.parseDouble(value);
			this.windValue.set(_value);
			windDrifts = WindVectorCalculator.calcWindVectors(direction, _value);
			calcTrueRange();
		}
		catch (NumberFormatException ex)
		{
			// Nothing to do
		}
	}


	private void calcTrueRange()
	{
		setTrueRange(range * getYDriftRel());
	}


	public double getYDriftRel()
	{
		return weather.windFactor - windDrifts[1] / 100;
	}


	public double getXDriftAbs()
	{
		return (getXDriftRel() / 100) * range;
	}


	public double getYDriftAbs()
	{
		return  (1 - getYDriftRel()) * range;
	}


	public double getXDriftRel()
	{
		return windDrifts[0];
	}


	public int getDirection()
	{
		return direction;
	}


	private void setDirection(int value)
	{
		direction = value;
		windDrifts = WindVectorCalculator.calcWindVectors(value, windValue.get());
		calcTrueRange();
	}


	public void setRange(String value)
	{
		try
		{
			range = Double.parseDouble(value);
			calcTrueRange();
		}
		catch (NumberFormatException ex)
		{
			// Nothing to do
		}
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


	@Override
	public void invalidated(Observable observable)
	{
		if (observable instanceof IntegerProperty)
		{
			IntegerProperty value = (IntegerProperty) observable;
			setDirection(value.get());

		}
		else if (observable instanceof WeatherModel)
		{
			WeatherModel weatherModel = (WeatherModel) observable;
			setWeather(weatherModel.getSelected());
		}
	}
}
