package communitySimulator;

import java.text.DecimalFormat;

public class GaussianRandomGenerator extends ProbabilityGenerator
{
	double base_probability;
	double mean;
	double variance;
	double multiplier;
	Randomizer randomizer = new Randomizer();

	public GaussianRandomGenerator( String label, double base_probability,  double mean, double variance )
	{
		super( label );
		this.base_probability = base_probability;
		this.mean = mean;
		this.variance = variance;
		this.multiplier = 1.0;
	}
	
	public GaussianRandomGenerator( String label, double base_probability,  double mean, double variance, double multiplier )
	{
		super( label );
		this.base_probability = base_probability;
		this.mean = mean;
		this.variance = variance;
		this.multiplier = multiplier;
	}
	
	public double getValue( int index )
	{
		return next();
	}
	
	public double next()
	{
		return base_probability + randomizer.nextGaussian( mean, variance ) * multiplier;
	}
	
	@Override
	public double getBaseProbability()
	{
		return base_probability;
	}
	@Override
	public double getMean()
	{
		return mean;
	}
	@Override
	public double getVariance()
	{
		return variance;
	}
	
	@Override
	String getType()
	{
		return "Gaussian";
	}

	public static void main(String[] args)
	{
		double					value = 0.05;
		double					variance = 0.015;
		int						number_of_buckets = 10;
		GaussianRandomGenerator	generator = new GaussianRandomGenerator( "Test", value, 0.0, variance );
		DecimalFormat			formatter = new DecimalFormat( "#.00000" );
		int						numbers_to_generate = 5000;
		//PowerCurveCalculator	pcc = new PowerCurveCalculator( 1000, 0.0, 100, 1.0, 1.1 );
		int[]					counts = new int[number_of_buckets];
		
		for (int i = 0; i < numbers_to_generate; i++)
		{
			//double index = ((double)(i+1))/((double)(numbers_to_generate+1));
			//double curve_value = pcc.getValue( index );
			double random_value = generator.next();
			int bucket = (int)((random_value - value) / variance) + (number_of_buckets / 2);
			if (bucket >= number_of_buckets)
				bucket = (number_of_buckets - 1);
			if (bucket < 0)
				bucket = 0;
			counts[bucket]++;
			if (bucket == 2)
				System.out.println( "Bucket 2 value: " + random_value);
			if (bucket == 8)
				System.out.println( "Bucket 8 value: " + random_value);
			
			if (i < 10)
				System.out.println( "For " + i + "\tRandom Value: " + formatter.format( random_value ) );
		}
		
		for (int i = 0; i < number_of_buckets; i++)
		{
			System.out.println( "Bucket[" + i + "]: " + counts[i]);
		}
	}

}
