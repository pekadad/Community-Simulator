package communitySimulator;

import java.util.Random;

public class Randomizer
{
	static private Random randomizer = null;
	
	public Randomizer()
	{
	}
	
	Random getRandomizer()
	{
		if (randomizer == null)
		{
			randomizer = new Random();
		}
		return randomizer;
	}
	
	public double nextGaussian(double aMean, double aVariance)
	{
		return aMean + getRandomizer().nextGaussian() * aVariance;
	}
	
	public int nextInt()
	{
		return getRandomizer().nextInt();
	}
	
	public int nextInt( int upper_bound )
	{
		return getRandomizer().nextInt( upper_bound );
	}
	
	public int nextInt( int lower_bound, int upper_bound )
	{
		return lower_bound + nextInt( upper_bound - lower_bound );
	}
	
	public double nextDouble()
	{
		return getRandomizer().nextDouble();
	}
	
	public double nextDouble( double upper_bound )
	{
		return nextDouble() * upper_bound;
	}
	
	public double nextDouble( double lower_bound, double upper_bound )
	{
		return lower_bound + nextDouble( upper_bound );
	}
	
	public static void main(String[] args)
	{
		Randomizer randomizer = new Randomizer();
		for (int i = 0; i < 40; i++)
			//System.out.println( "" + getRandomizer().nextGaussian(.1, .03) );
			System.out.println( "" + randomizer.nextInt( 0, 3) );
	}

}
