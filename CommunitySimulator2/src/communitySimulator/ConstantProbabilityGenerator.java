/**
 * 
 */
package communitySimulator;

/**
 * @author Romero
 *
 */
public class ConstantProbabilityGenerator extends ProbabilityGenerator
{
	double base_probability;

	/**
	 * @param header_label
	 */
	public ConstantProbabilityGenerator( String header_label, double base_probability )
	{
		super(header_label);
		this.base_probability = base_probability;
	}

	/* (non-Javadoc)
	 * @see communitySimulator.ProbabilityGenerator#getType()
	 */
	@Override
	String getType()
	{
		return "Constant";
	}

	@Override
	public double getExponent()
	{
		return 0.0;
	}

	@Override
	public double getBaseProbability()
	{
		return base_probability;
	}

	@Override
	public double getVariance()
	{
		return 0.0;
	}

	@Override
	public double getLowerBound()
	{
		return base_probability;
	}

	@Override
	public double getUpperBound()
	{
		return base_probability;
	}

	/* (non-Javadoc)
	 * @see communitySimulator.ProbabilityGenerator#getValue(int)
	 */
	@Override
	public double getValue(int index)
	{
		return base_probability;
	}

}
