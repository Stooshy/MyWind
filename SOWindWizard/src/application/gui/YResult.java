package application.gui;

import application.model.RangeModel;
import javafx.beans.Observable;

public final class YResult extends ResultDisplay
{
	public YResult()
	{
		formatString = "%2.1f y | %.1f%%";
	}


	@Override
	public void invalidated(Observable observable)
	{
		if (observable instanceof RangeModel)
		{
			RangeModel model = (RangeModel) observable;
			super.setText(
					String.format(formatString, model.getYDriftRel() * model.getTrueRange(), model.getYDriftAbs()));
		}
	}

}
