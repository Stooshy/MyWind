package application.gui;

public final class YResult extends ResultDisplay
{
	private double drift;


	public YResult()
	{
		formatString = "%s%10.1f y | %.1f %%";
	}


	@Override
	public void setText(double value, Object... args)
	{
		String preFix = value > 0.0 ? "^" : "v";

		Object[] formatArgs = args;
		int n = formatArgs.length;
		if (n < 2)
			throw new IllegalArgumentException("Not enough arguments: 2");

		drift = (double) formatArgs[0] / 100.0 * (double) formatArgs[1];
		super.setText(String.format(formatString, preFix, drift, formatArgs[0]));
	}


	public double getDrift()
	{
		return drift;
	}
}
