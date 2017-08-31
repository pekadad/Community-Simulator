package communitySimulator;

import java.io.PrintStream;

public class SummaryResult
{
	CommunityConfiguration run_configuration;
	String run_identifier;
	int total_threads = 0;
	int total_messages = 0;
	int number_of_members_who_posted = 0;
	long milliseconds_for_runs = 0;
	
	public SummaryResult( CommunityConfiguration run_configuration, String run_identifier, int total_threads, int total_messages, int number_of_members_who_posted, long milliseconds_for_runs )
	{
		super();
		this.run_configuration = run_configuration;
		this.run_identifier = run_identifier;
		this.total_threads = total_threads;
		this.total_messages = total_messages;
		this.number_of_members_who_posted = number_of_members_who_posted;
		this.milliseconds_for_runs = milliseconds_for_runs;
	}
	
	public String getRunIdentifier()
	{
		return run_identifier;
	}

	public int getTotalThreads()
	{
		return total_threads;
	}

	public int getTotalMessages()
	{
		return total_messages;
	}
	
	public int getNumberOfMembersWhoPosted()
	{
		return number_of_members_who_posted;
	}

	public CommunityConfiguration getRunConfiguration()
	{
		return run_configuration;
	}
	
	public long getMillisecondsForRuns()
	{
		return milliseconds_for_runs;
	}
	
	public void outputHeaders( PrintStream output_stream )
	{
		run_configuration.outputHeaders( output_stream );
		output_stream.println( "\t" +
				   			   "Run ID\t" +
				   			   "Total threads\t" +
				   			   "Total messages\t" +
				   			   "Total members who posted\t" +
				   			   "Total milliseconds for all runs" );
	}
	
	public void output( PrintStream output_stream )
	{
		run_configuration.output( output_stream );
		output_stream.println( "\t" +
							   getRunIdentifier() + "\t" +
							   getTotalThreads() + "\t" +
							   getTotalMessages() + "\t" +
							   getNumberOfMembersWhoPosted() + "\t" +
							   getMillisecondsForRuns() );
	}
}
