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
			String directionSign = model.getDirection() > 0 && model.getDirection() < 9 ? "> " : "< ";
			super.setText(String.format(formatString, model.getXDriftRel(), directionSign));
		}
	}


	@Override
	protected String getFormat()
	{
		return "%.1f m/s %s";
	}

}
