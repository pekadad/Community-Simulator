package communitySimulator;

import java.io.PrintStream;

public abstract class ProbabilityGenerator
{
	Randomizer randomizer;
	String header_label;

	public ProbabilityGenerator( String header_label )
	{
		randomizer = new Randomizer();
		this.header_label = header_label;
	}
	
	public void outputHeaders( PrintStream stream )
	{
		stream.print( header_label + " Type\t" + 
					  header_label + " Exponent\t" + 
					  header_label + " Base Probability\t" + 
					  header_label + " Gaussian Mean\t" + 
					  header_label + " Gaussian Variance\t" + 
					  header_label + " Lower Bound\t" + 
					  header_label + " Upper Bound" );
	}
	public void output( PrintStream stream )
	{
		stream.print( getType() + "\t" + getExponent() + "\t" + getBaseProbability() + "\t" + getMean() + "\t" + getVariance() + "\t" + getLowerBound() + "\t" + getUpperBound() );
	}
	
	abstract String getType();
	
	public double getExponent()
	{
		return 0.0;
	}
	public double getBaseProbability()
	{
		return 0.0;
	}
	public double getMean()
	{
		return 0.0;
	}
	public double getVariance()
	{
		return 0.0;
	}
	public double getLowerBound()
	{
		return 0.0;
	}
	public double getUpperBound()
	{
		return 0.0;
	}
	
	abstract public double getValue( int index );

	public Randomizer getRandomizer()
	{
		return randomizer;
	}

}
