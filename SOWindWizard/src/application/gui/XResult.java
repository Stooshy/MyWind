package application.gui;

public final class XResult extends ResultDisplay
{

	public XResult()
	{
		formatString = "%10.1f y | %.1f %%";
	}


	@Override
	public void setText(double value, Object... args)
	{
		String preFix = value > 1 && value < 10 ? "> " : "< ";

		Object[] formatArgs = args;
		int n = formatArgs.length;
		if (n < 2)
			throw new IllegalArgumentException("Not enough arguments: 2");

		super.setText(String.format(preFix + formatString, (double) formatArgs[0] / 100.0 * (double) formatArgs[1],
				formatArgs[0]));

	}

}
