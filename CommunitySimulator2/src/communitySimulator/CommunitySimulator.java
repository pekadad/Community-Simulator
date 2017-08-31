package communitySimulator;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Iterator;

public class CommunitySimulator
{
	public int MAX_NUMBER_OF_DAYS = 30;
	public int NUMBER_OF_TOPICS = 4;
	static public PrintStream	output_stream = System.out;
	static public PrintStream	member_output_stream = null;

	public CommunitySimulator()
	{
	}
	
	public SummaryResult runSimulation( int number_of_runs, int number_of_days, CommunityConfiguration config, int simulation_number )
	{
		int total_threads = 0;
		int total_messages = 0;
		int total_members_who_posted = 0;
		
		LocalDateTime simulation_starting_time = LocalDateTime.now();
		String experiment_label = config.getExperimentID() + "-" + simulation_number + "-" + simulation_starting_time.getLong( ChronoField.MILLI_OF_DAY );
		
		for (int i = 0; i < number_of_runs; i++)
		{
			LocalDateTime starting_time = LocalDateTime.now();
			System.out.print( "\nRunning experiment " + experiment_label + ", simulation\t" + (i+1) + "\twith\t" + config.getNumberOfMembers() + "\tmembers" );
			output_stream.println( "\nRunning simulation " + (i+1) );
			
			Community community = new Community( config );
			
			//output_stream.println( "Community members before run: " );
			//community.outputMembers( output_stream );
			
			// Run simulation
			community.simulate( number_of_days );
			
			total_threads += community.totalThreads();
			total_messages += community.totalMessages();
			total_members_who_posted += community.getNumberOfMembersWhoPosted();
			
			// community.outputDiscussionBoard( MAX_NUMBER_OF_DAYS, output_stream );
			//output_stream.println( "Community members after run: " );
			//community.outputMembers( output_stream );
			community.outputMembersAsTable( member_output_stream, i, experiment_label + "-" + i );

			CommunityMember first_member = community.memberIterator().next();
			double total_probability = first_member.getProbabilityToStartConversation() + first_member.getProbabilityToReplyToConversation() + first_member.getCommunityVisibility();
			
			LocalDateTime ending_time = LocalDateTime.now();
			Duration time_passed = Duration.between( starting_time, ending_time );
			long milliseconds = time_passed.toMillis();
			outputRunSummary( output_stream, community.totalThreads(), community.totalMessages(), total_probability, milliseconds );
			outputRunSummary( System.out, community.totalThreads(), community.totalMessages(), total_probability, milliseconds );
		}
		
		LocalDateTime simulation_ending_time = LocalDateTime.now();
		Duration simulation_time_passed = Duration.between( simulation_starting_time, simulation_ending_time );
		long total_milliseconds = simulation_time_passed.toMillis();
		outputSummary( output_stream, number_of_runs, config.getNumberOfMembers(), number_of_days, total_threads, total_messages, total_milliseconds );
		outputSummary( System.out, number_of_runs, config.getNumberOfMembers(), number_of_days, total_threads, total_messages, total_milliseconds );
		return new SummaryResult( config, experiment_label, total_threads, total_messages, total_members_who_posted, total_milliseconds );
	}
	
	private void outputRunSummary( PrintStream output_stream, int totalThreads, int totalMessages, double total_probability, long milliseconds )
	{
		output_stream.print( "\tTotal threads this run: " + totalThreads + "\t" );
		output_stream.print( "\tTotal messages this run: " + totalMessages + "\t" );
		output_stream.print( "\tTotal of three factors for first member: " + total_probability );
		output_stream.print( "\tMillisecond: " + milliseconds );
		output_stream.println("");
	}

	private void outputSummary( PrintStream output_stream, int number_of_runs, int number_of_members,
								int number_of_days, int total_threads, int total_messages, long milliseconds )
	{
		output_stream.println( "After " + number_of_runs + " total simulations each for " + number_of_days + " days with " + number_of_members + " members:" );
		output_stream.println( "\tTotal threads: " + total_threads );
		output_stream.println( "\tAverage threads: " + ((double)total_threads / (double)number_of_runs) );
		output_stream.println( "\tTotal messages: " + total_messages );
		output_stream.println( "\tAverage messages: " + ((double)total_messages / (double)number_of_runs) );
		output_stream.println( "\tRatio of all messages to threads: " + ((double)total_messages / (double)total_threads) );
		output_stream.println( "\tTotal milliseconds for all runs: " + milliseconds );
	}

	public void simulateDiscussions()
	{
		ArrayList<SummaryResult> 	result_table = new ArrayList<SummaryResult>();

		int		population_sizes_to_test[]	= {10,50,100,250,500/*,1000*/};
		double	curve_powers_to_test[]		= {4.0}; //{2.0, 3.0, 4.0, 5.0};
		int 	number_of_runs 			= 100;
		int 	number_of_days			= 30;
		double	energy_drops_to_test[]	= {0.8}; // {0.5,0.8,0.95}; // {0.8};
		double	probability_of_posting[] 	= {0.005,0.010}, // {0.1},
				variance_for_posting 	= 0.02;
		double	probability_of_replying[] = {0.1, 0.15, 0.25},
				variance_for_replying 	= 0.05;
		double	visibility 				= 0.05,
				variance_for_visibility = 0.015;
		int		number_of_topics_to_test[] = {NUMBER_OF_TOPICS}; //{1,3,5,10,15};
		
		int		number_of_runs_of_runs	= 5;
		
		SimpleGenerator topic_interest_level_generator = new SimpleGenerator( "Topic Interest Level", 0.0, 1.0 );
		
		for (int energy_index = 0; energy_index < energy_drops_to_test.length; energy_index++)
		{
			ProbabilityGenerator post_interest_generator = new GaussianRandomGenerator( "Post Interestingness", 0.0, 0.2, 0.04 );
			ProbabilityGenerator self_reply_generator = new ConstantProbabilityGenerator( "Self Reply", 0.25 );
			
			MemberBehaviorConfiguration member_config = new MemberBehaviorConfiguration( 0.2, topic_interest_level_generator, 0.8, energy_drops_to_test[energy_index], 5, self_reply_generator );
			
			for (int i = 0; i < population_sizes_to_test.length; i++)
			{
				for (int k = 0; k < curve_powers_to_test.length; k++)
				{
					for (int j = 0; j < number_of_topics_to_test.length; j++)
					{
						PowerCurveCalculator pcc = new PowerCurveCalculator( population_sizes_to_test[i], 0.0, 1, 1.0, curve_powers_to_test[k] );
						for (int post_index = 0; post_index < probability_of_posting.length; post_index++)
						{
							//ProbabilityGenerator post_probability_generator = new PowerCurveGaussianProbabilityGenerator( "Posting", pcc, new GaussianRandomGenerator( "Posting", probability_of_posting[post_index], 0.0, variance_for_posting, 2 ) );
							ProbabilityGenerator post_probability_generator = new GaussianRandomGenerator( "Posting", probability_of_posting[post_index], 0.0, 0.001 );
							for (int reply_index = 0; reply_index < probability_of_replying.length; reply_index++)
							{
								ProbabilityGenerator reply_probability_generator = new PowerCurveGaussianProbabilityGenerator( "Replying", pcc, new GaussianRandomGenerator( "Replying", probability_of_replying[reply_index], 0.0, variance_for_replying, 2 ) );
								ProbabilityGenerator visibility_generator = new PowerCurveGaussianProbabilityGenerator( "Visibility", pcc, new GaussianRandomGenerator( "Visibility", visibility, 0.0, variance_for_visibility, 2 ) );
								
								CommunityConfiguration config = new CommunityConfiguration( "Basic", number_of_runs, number_of_days, 
																		  					population_sizes_to_test[i], member_config,
																		  					post_probability_generator,
																		  					reply_probability_generator,
																		  					visibility_generator,
																		  					post_interest_generator,
																		  					number_of_topics_to_test[j] );
								
								for (int l = 0; l < number_of_runs_of_runs; l++)
								{
									SummaryResult result = runSimulation( number_of_runs, number_of_days, config, l );
									result_table.add( result );
								}
							}
						}
					}
				}
			}
		}
		
		outputSummary( result_table );
	}
	
	private void simulateDiscussions( ArrayList<CommunityConfiguration> configs )
	{
		String file_prefix = getSummaryOutputFilePrefix( configs.get( 0 ) );
		PrintStream summary_output_stream = getOutputStream( "E:\\Workspace\\CommunitySimulatorData\\output\\" + file_prefix + ".output_summary.txt" );
		member_output_stream = getOutputStream( "E:\\Workspace\\CommunitySimulatorData\\output\\" + file_prefix + ".member_output_summary.txt" );
		Boolean have_output_headers = false;
		for (Iterator<CommunityConfiguration> i = configs.iterator(); i.hasNext(); )
		{
			CommunityConfiguration config = i.next();
			SummaryResult result = runSimulation( config.getNumberOfRuns(), config.getNumberOfDays(), config, 1 );
			if (!have_output_headers)
			{
				result.outputHeaders( summary_output_stream );
				have_output_headers = true;
			}
			result.output(summary_output_stream);
		}
	}

	private void outputSummary(ArrayList<SummaryResult> result_table)
	{
		PrintStream summary_output_stream = getOutputStream( "E:\\Workspace\\CommunitySimulatorData\\output\\" + getSummaryOutputFileSuffix( result_table ) + ".output_summary.txt" );
		Boolean have_output_headers = false;
		for (Iterator<SummaryResult> r = result_table.iterator(); r.hasNext(); )
		{
			SummaryResult sr = r.next();
			if (!have_output_headers)
			{
				sr.outputHeaders( summary_output_stream );
				have_output_headers = true;
			}
			sr.output(summary_output_stream);
		}
	}

	private String getFileNameSuffixDate()
	{
		LocalDateTime current_time = LocalDateTime.now();
		
		String suffix = "" + current_time.format( DateTimeFormatter.ofPattern("yyyyMMdd") ) + "." + current_time.getLong( ChronoField.MILLI_OF_DAY );
		return suffix;
	}
	private String getSummaryOutputFilePrefix( CommunityConfiguration config )
	{
		if (config != null)
			return config.getExperimentID() + "_" + getFileNameSuffixDate();
		else
			return getFileNameSuffixDate();
	}
	private String getSummaryOutputFileSuffix(ArrayList<SummaryResult> result_table)
	{
		if (result_table.size() > 0)
			return getSummaryOutputFilePrefix( result_table.get(0).getRunConfiguration() );
		else
			return getFileNameSuffixDate();
	}

	public void generateMembers( ArrayList<CommunityConfiguration> configs )
	{
		boolean output_headers = true;
		for (Iterator<CommunityConfiguration> j = configs.iterator(); j.hasNext(); )
		{
			CommunityConfiguration config = j.next();
			for (int i = 0; i < config.getNumberOfRuns(); i++)
			{
				// TODO Capture the totals / averages for members for all runs
				Community community = new Community( config );
				community.outputMemberTable( i, 0, output_stream, output_headers );
				output_headers = false;
			}
		}
		
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
	
	public void run( String[] args )
	{
		output_stream = getOutputStream( "E:\\Workspace\\CommunitySimulatorData\\output\\output.txt" );
		
		try
		{
			ArrayList<CommunityConfiguration> configs = CommunityConfiguration.readConfigurations( "E:\\Workspace\\CommunitySimulatorData\\input\\input.txt" );
			simulateDiscussions( configs );
			//generateMembers( configs );
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public static void main(String[] args)
	{
		(new CommunitySimulator()).run(args);
	}

}
