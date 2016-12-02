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
			super.setText(String.format(formatString, model.getYDriftAbs(), model.getYDriftRel()));
		}
	}


	@Override
	protected String getFormat()
	{
		return "%2.1f y | %.1f %%";
	}

}
