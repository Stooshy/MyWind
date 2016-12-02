package application.gui;

import javafx.beans.value.ChangeListener;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

public class DoubleInputControl extends TextField
{

	public DoubleInputControl()
	{
		super();
		setId("textnormal");
		setMaxWidth(50.0);
		setOnKeyReleased(event -> {
			if (event.getCode().equals(KeyCode.ENTER))
			{
				setText("" + getValue());
			}
		});

		focusedProperty().addListener(listener -> {
			setText("" + getValue());
		});

	}


	/**
	 * @return 0.0 in case the value couldn´t be parsed.
	 */
	public double getValue()
	{
		try
		{
			return Double.parseDouble(getText());
		}
		catch (NumberFormatException ex)
		{
			return 0.0;
		}
	}


	public void setValue(double value)
	{
		setText("" + value);
	}


	public void addListener(ChangeListener<String> toAdd)
	{
		textProperty().addListener(toAdd);
	}
}
