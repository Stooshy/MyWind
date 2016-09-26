package application.gui;

import javafx.beans.value.ChangeListener;
import javafx.scene.control.TextField;

public class InputControl extends TextField
{

	public InputControl(String text)
	{
		super(text);
		setId("textnormal");
		setMaxWidth(50.0);
		focusedProperty().addListener(listener -> {
			try
			{
				setId("textnormal");
				setText("" + getValue());
				selectPositionCaret(getText().length());
			}
			catch (Exception ex)
			{
				setId("textred");
			}
		});
	}


	public double getValue() throws NumberFormatException
	{
		String text = getText();
		return Double.parseDouble(text.isEmpty() ? "0.0" : text);
	}


	public void addListener(ChangeListener<String> toAdd)
	{
		textProperty().addListener(toAdd);
	}
}
