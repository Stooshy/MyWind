package application.gui;

import application.model.RangeModel;
import javafx.beans.Observable;

public final class YResult extends ResultLabel
{
	@Override
	public void invalidated(Observable observable)
	{
		if (observable instanceof RangeModel)
		{
			RangeModel model = (RangeModel) observable;
			super.setText(String.format(formatString, model.getYDriftAbs(), (1 - model.getYDriftRel()) * 100 ));
		}
	}


	@Override
	String getFormat()
	{
		return "%2.1f y | %.1f %%";
	}

}
