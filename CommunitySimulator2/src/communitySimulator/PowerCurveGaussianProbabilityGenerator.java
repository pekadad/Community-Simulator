package communitySimulator;

public class PowerCurveGaussianProbabilityGenerator extends ProbabilityGenerator
{
	PowerCurveCalculator pcc;
	GaussianRandomGenerator generator;

	public PowerCurveGaussianProbabilityGenerator( String label, PowerCurveCalculator pcc, GaussianRandomGenerator generator )
	{
		super( label );
		this.pcc = pcc;
		this.generator = generator;
	}

	@Override
	public double getValue(int index)
	{
		return pcc.getValue( index ) * generator.next();
	}
	
	@Override
	public double getExponent()
	{
		return pcc.getExponent();
	}
	@Override
	public double getBaseProbability()
	{
		return generator.getBaseProbability();
	}
	@Override
	public double getMean()
	{
		return generator.getMean();
	}
	@Override
	public double getVariance()
	{
		return generator.getVariance();
	}
	@Override
	public double getLowerBound()
	{
		return pcc.getLowerBound();
	}
	@Override
	public double getUpperBound()
	{
		return pcc.getUpperBound();
	}
	
	@Override
	String getType()
	{
		return "PowerCurveGaussian";
	}

}
