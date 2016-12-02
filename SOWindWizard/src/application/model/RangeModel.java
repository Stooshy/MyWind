package application.model;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.IntegerProperty;

public final class RangeModel implements Observable, InvalidationListener
{
	private double trueRange;
	private double windValue;
	private int direction;
	private double[] windVector;
	private double range;
	private double weatherFactor;
	private double userFactor = 2d;
	private final List<InvalidationListener> listeners;


	public RangeModel()
	{
		listeners = new ArrayList<>();
		windValue = 1.0;
		trueRange = 100.0;
		setWind("1.0");
	}


	public void setUserFactor(double value)
	{
		userFactor = value;
		calcTrueRange();
	}


	public double getUserData()
	{
		return userFactor;
	}


	public double getTrueRange()
	{
		return trueRange;
	}


	private void setWeatherFactor(double value)
	{
		weatherFactor = value;
		calcTrueRange();
	}


	private void setTrueRange(double value)
	{
		trueRange = value;
		notifyListeners();
	}


	public void setWind(String value)
	{
		try
		{
			double _value = Double.parseDouble(value);
			windValue = _value;
			windVector = WindVectorCalculator.calcWindVectors(direction, _value);
			calcTrueRange();
		}
		catch (NumberFormatException ex)
		{
			// Nothing to do
		}
	}


	private void calcTrueRange()
	{
		setTrueRange(range - getYDriftAbs());
	}


	/**
	 * 
	 * @return <b>e. g. rain & 1.0 headwind:</b> 1.13 + 2 * -0.98 = 1.13 - 3.1%
	 *         further</b>
	 */
	public double getYDriftRel()
	{
		return weatherFactor + userFactor * windVector[1];
	}


	public double getYDriftAbs()
	{
		return getYDriftRel() / 100d * range;
	}


	public double getXDriftRel()
	{
		return windVector[0];
	}


	public int getDirection()
	{
		return direction;
	}


	private void setDirection(int value)
	{
		direction = value;
		windVector = WindVectorCalculator.calcWindVectors(value, windValue);
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
			setDirection(((IntegerProperty) observable).get());
		}
		else if (observable instanceof WeatherModel)
		{
			setWeatherFactor(((WeatherModel) observable).getFactor());
		}
		else if (observable instanceof UserData)
		{
			setUserFactor(((UserData) observable).getUserFactor());
		}
	}

}
