package application.gui;

import application.model.RangeModel;
import javafx.beans.Observable;

public final class XResult extends ResultLabel
{
	@Override
	public void invalidated(Observable observable)
	{
		if (observable instanceof RangeModel)
		{
			RangeModel model = (RangeModel) observable;
			String preFix = model.getDirection() > 1 && model.getDirection() < 10 ? "> " : "< ";
			super.setText(String.format(formatString, preFix, model.getXDriftAbs(), model.getXDriftRel()));
		}
	}


	@Override
	String getFormat()
	{
		return "%s%2.1f y | %.1f %%";
	}

}
