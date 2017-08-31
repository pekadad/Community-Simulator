package communitySimulator;

public class PowerCurveCalculator 
{
	double lower_bound;
	double upper_bound;
	double coefficient;
	double power;
	
	double increment_size;
	double lower_value;
	double upper_value;
	double value_range;
	
	static double lower_x = 0.01;
	static double upper_x = 1.01;
	
	public PowerCurveCalculator( int number_of_increments, double lower_bound, double upper_bound, double coefficient, double power )
	{
		this.lower_bound = lower_bound;
		this.upper_bound = upper_bound;
		this.coefficient = coefficient;
		this.power = power;
		
		increment_size = (upper_x - lower_x) / (double) number_of_increments;
		
		lower_value = getCurveValue( 0 );
		upper_value = getCurveValue( number_of_increments - 1);
		//lower_value = getCurveValue( .01 );
		//upper_value = getCurveValue( 1.01 );
		value_range = Math.abs( lower_value - upper_value );
	}
	
	// index should be a value between 0 and number_of_increments - 1
	public double getValue( int index )
	{
		double y = getCurveValue( index  );
		
		// now scale to between lower_bound and upper_bound
		return lower_bound + (upper_bound - lower_bound) * y / value_range;
	}

	public double getExponent()
	{
		return power;
	}
	
	public double getLowerBound()
	{
		return lower_bound;
	}
	public double getUpperBound()
	{
		return upper_bound;
	}
	public double getCoefficient()
	{
		return coefficient;
	}

	private double getCurveValue( int index )
	{
		double x = Math.pow( lower_x + (double)index * increment_size, -power );
		double y = coefficient * x;
		return y;
	}
	/*public static void main(String[] args)
	{
		int number_increments = 10000;
		PowerCurveCalculator calculator = new PowerCurveCalculator( (number_increments+1), 0.0, 1, 1.0, 4.0 );
		
		for (int i = 0; i < number_increments; i++ )
		{
			double value = calculator.getValue( ((double)i+1) / (number_increments + 1) );
			if (i < 20) System.out.println( "Value for\t" + i + " is:\t" + value );
		}
	}
*/
	private static double getNthValueForCurve( int n, int number_of_increments, double lower_bound, double upper_bound, double coefficient, double power )
	{
		PowerCurveCalculator calculator = new PowerCurveCalculator( number_of_increments, lower_bound, upper_bound, coefficient, power );
		
		return calculator.getValue( n );
	}
	
	private static void printCurveValue( int size )
	{
		System.out.println( "For size " + size + " the first value is " + getNthValueForCurve( 0, size, 0.0, 1, 1.0, 4.0 ) + " and the " + size + " value is " + getNthValueForCurve( (size-1), size, 0.0, 1, 1.0, 4.0 ) );
		System.out.println( "For size " + size + " the second value is " + getNthValueForCurve( 1, size, 0.0, 1, 1.0, 4.0 ) + " and the " + size + " value is " + getNthValueForCurve( (size-1), size, 0.0, 1, 1.0, 4.0 ) );
	}
	
	public static void main( String[] args )
	{
		printCurveValue( 10 );
		printCurveValue( 50 );
		printCurveValue( 100 );
		printCurveValue( 500 );
		printCurveValue( 1000 );
		printCurveValue( 10000 );
		printCurveValue( 100000 );
		printCurveValue( 100000 );
	}
}
