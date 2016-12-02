package application.model;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectInputValidation;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class UserData implements Observable, Serializable, ObjectInputValidation
{

	private static final long serialVersionUID = 1L;
	private DoubleProperty userFactor = new SimpleDoubleProperty(2d);
	private transient List<InvalidationListener> listeners;
	private transient boolean isValid = false;


	public UserData()
	{
		listeners = new ArrayList<>();
	}


	public void setUserFactor(double value)
	{
		userFactor.set(value);
		notifyListeners();
	}


	public double getUserFactor()
	{
		return userFactor.get();
	}


	private void writeObject(ObjectOutputStream out) throws IOException
	{
		Object obj = new Object[]
		{
				getUserFactor()
		};
		out.writeObject(obj);
	}


	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		in.registerValidation((ObjectInputValidation) this, 0);

		Object[] obj = (Object[]) in.readObject();
		userFactor = new SimpleDoubleProperty((Double) obj[0]);
		isValid = true;
		listeners = new ArrayList<>();
	}


	public void validateObject() throws InvalidObjectException
	{
		if (this.userFactor == null)
			throw new InvalidObjectException("Fields may not be null");

		if (!(this.userFactor instanceof SimpleDoubleProperty))
			throw new InvalidObjectException("Data error. Corrupted cache file.");
	}


	public boolean getIsValid()
	{
		return isValid;
	}


	private void notifyListeners()
	{
		listeners.forEach(listener -> listener.invalidated(this));
	}


	public void setIsValid(boolean isValid)
	{
		this.isValid = isValid;
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
