package communitySimulator;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

public class SearchLogSimulator
{
	int number_of_terms = 0;
	ProbabilityGenerator term_probability_generator = null;
	ArrayList<String> terms = null;
	double probabilities[] = null;
	long   uses[] = null;
	
	public SearchLogSimulator( int number_of_terms, ProbabilityGenerator generator )
	{
		this.number_of_terms = number_of_terms;
		this.term_probability_generator = generator;
	}
	
	private PrintStream getOutputStream( String path )
	{
		PrintStream result = System.out;
		if (path != null)
		{
			try
			{
				result = new PrintStream( new FileOutputStream( path ) );
			} 
			catch (FileNotFoundException e)
			{
				System.err.println( "Error opening file '" + path + "' for writing.  Defaulting to standard out." );
				e.printStackTrace();
			}
		}
		return result;
	}
	
	private void outputResults( PrintStream output, int number_of_increments )
	{
		output.println( "Term\tProbability\tHits" );
		for (int term_index = 0; term_index < number_of_terms; term_index++)
		{
			output.println( terms.get( term_index ) + "\t" + probabilities[term_index] + "\t" + (uses[term_index] + 1) );
		}
	}
	
	public void run( int number_of_increments, ProbabilityGenerator hit_generator )
	{
		terms = new ArrayList<String>( number_of_terms );
		probabilities = new double[ number_of_terms ];
		uses = new long[ number_of_terms ];
		
		// Initialize the terms and their probabilities
		for (int i = 0; i < number_of_terms; i++)
		{
			terms.add( new String( "Term" + i ) );
			probabilities[i] = term_probability_generator.getValue( i );
			uses[i] = 0;
		}
		
		// Now run through the number of increments
		int total_hit_number = 0;
		for (int increment = 0; increment < number_of_increments; increment++)
		{
			if ((increment % 100) == 0)
				System.out.println( "At increment " + increment + " of " + number_of_increments);
			// For each term, check if it "fires"
			for (int term_index = 0; term_index < number_of_terms; term_index++)
			{
				if (hit_generator.getValue( total_hit_number ) <= probabilities[term_index])
					uses[term_index]++;
			}
		}
		
		outputResults( getOutputStream( "E:\\Workspace\\CommunitySimulator\\search_log_output.txt" ), number_of_increments );
	}

	public static void main(String[] args)
	{
		(new SearchLogSimulator( 10000, new GaussianRandomGenerator( "Term Generator", 0.0000, 0.0, 0.07 ) )).run( 200000, new SimpleGenerator( "Hit Generator", 0.0, 1.0));
	}
}
