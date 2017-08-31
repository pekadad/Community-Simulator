package communitySimulator;

public class SimpleGenerator extends ProbabilityGenerator
{
	double min;
	double max;

	public SimpleGenerator( String label, double min, double max )
	{
		super( label );
		this.min = min;
		this.max = max;
	}

	@Override
	public double getValue(int index)
	{
		return getRandomizer().nextDouble( min, max );
	}
	@Override
	public double getLowerBound()
	{
		return min;
	}
	@Override
	public double getUpperBound()
	{
		return max;
	}

	@Override
	String getType()
	{
		return "Simple";
	}
	
}
