package communitySimulator;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;

public class CommunityConfiguration
{
	String experiment_id;
	int		number_of_runs;
	int 	number_of_days;

	int 	number_of_members;
	MemberBehaviorConfiguration member_behavior_config;
	ProbabilityGenerator posting_generator;
	ProbabilityGenerator reply_generator;
	ProbabilityGenerator visibility_generator;
	ProbabilityGenerator post_interest_generator;

	int		number_of_topics;
	ArrayList<Topic> topics = null;
	
	public CommunityConfiguration( String experiment_id, int number_of_runs, int number_of_days, 
								   int number_of_members, MemberBehaviorConfiguration member_behavior_config, 
								   ProbabilityGenerator posting_generator, 
								   ProbabilityGenerator reply_generator,
								   ProbabilityGenerator visibility_generator,
								   ProbabilityGenerator post_interest_generator,
								   int number_of_topics )
	{
		super();
		this.experiment_id = experiment_id;
		this.number_of_runs = number_of_runs;
		this.number_of_days = number_of_days;
		this.number_of_members = number_of_members;
		this.member_behavior_config = member_behavior_config;
		this.posting_generator = posting_generator;
		this.reply_generator = reply_generator;
		this.visibility_generator = visibility_generator;
		this.post_interest_generator = post_interest_generator;
		this.number_of_topics = number_of_topics;
		this.topics = null;
	}

	public CommunityMemberFactory getCommunityMemberFactory()
	{
		return new CommunityMemberFactory( number_of_members, member_behavior_config, posting_generator, reply_generator, visibility_generator, topics );
	}
	
	public String getExperimentID()
	{
		return experiment_id;
	}
	
	public int getNumberOfRuns()
	{
		return number_of_runs;
	}

	public int getNumberOfDays()
	{
		return number_of_days;
	}

	public int getNumberOfMembers()
	{
		return number_of_members;
	}

	public double getExponentForPosting()
	{
		return posting_generator.getExponent();
	}

	public double getBaseProbabilityOfPosting()
	{
		return posting_generator.getBaseProbability();
	}

	public double getVarianceForPosting()
	{
		return posting_generator.getVariance();
	}

	public double getExponentForReplying()
	{
		return reply_generator.getExponent();
	}

	public double getBaseProbabilityOfReplying()
	{
		return reply_generator.getBaseProbability();
	}

	public double getVarianceForReplying()
	{
		return reply_generator.getVariance();
	}

	public double getExponentForVisibility()
	{
		return visibility_generator.getExponent();
	}

	public double getBaseVisibility()
	{
		return visibility_generator.getBaseProbability();
	}

	public double getVarianceForVisibility()
	{
		return visibility_generator.getVariance();
	}

	public int getNumberOfTopics()
	{
		return number_of_topics;
	}
	
	public ProbabilityGenerator getPostInterestGenerator()
	{
		return post_interest_generator;
	}
	public ArrayList<Topic> getTopics()
	{
		return topics;
	}

	public void setTopics(ArrayList<Topic> topics)
	{
		this.topics = topics;
	}

	public void outputHeaders( PrintStream stream )
	{
		stream.print( "Experiment\t" +
					  "Runs\t" + 
	   			   	  "Members\t" +
	   			   	  "Number of days\t" );
		posting_generator.outputHeaders( stream );
		stream.print( "\t" );
		reply_generator.outputHeaders( stream );
		stream.print( "\t" );
		visibility_generator.outputHeaders( stream );
		stream.print( "\tNumber of topics" );
		stream.print( "\t" );
		post_interest_generator.outputHeaders( stream );
		stream.print( "\t" );
		member_behavior_config.outputHeaders( stream );
	}
	
	public void output( PrintStream stream )
	{
		stream.print(  getExperimentID() + "\t" +
					   getNumberOfRuns() + "\t" + 
				   	   getNumberOfMembers() + "\t" +
				   	   getNumberOfDays() + "\t" );
		posting_generator.output( stream );
		stream.print( "\t" );
		reply_generator.output( stream );
		stream.print( "\t" );
		visibility_generator.output( stream );
		stream.print( "\t" + getNumberOfTopics() );
		stream.print( "\t" );
		post_interest_generator.output( stream );
		stream.print( "\t" );
		member_behavior_config.output( stream );
	}
	
	static public ArrayList<CommunityConfiguration> readConfigurations( String source_file ) throws IOException
	{
		File file = new File( source_file );
		InputStream inputStream = new FileInputStream(file);

		return readConfigurations( inputStream);
	}
	
	static public ArrayList<CommunityConfiguration> readConfigurations( InputStream stream ) throws IOException
	{
		if (stream == null)
			return null;
		
		ArrayList<CommunityConfiguration> configs = new ArrayList<CommunityConfiguration>();
		
		DataInputStream in = new DataInputStream( stream );
		BufferedReader reader = new BufferedReader( new InputStreamReader( in ) );
		
		String[] next_line = null;
		while ((next_line = DelimitedFileReader.readNextLine( reader )) != null)
		{
			// Each line should have 57 values in it 
			if (next_line.length == 57)
			{
				// identify of configuration, Number of runs, Number of members, Number of days
				// then groups of 8 values - one each describing probability model for Starting a thread, replying to a thread, visibility, 
				// Number of topics
				// Another group of 8 values defining a probability model for interestingness of posts
				// Base Interest in Topics, Age Attenuation Factor, Energy Drop Per Post, Days to gain all energy back, Probability of self-reply
				// Another group of 8 values defining a probability model for interestingness of posts
				
				int number_of_elements_for_probability_model = 8;
				int index = 0;
				String				 config_name = next_line[index];
				index++;
				int 				 number_of_runs = Integer.parseInt( next_line[index] );
				index++;
				int 				 number_of_members = Integer.parseInt( next_line[index] );
				index++;
				int 				 number_of_days = Integer.parseInt( next_line[index] );
				index++;
				ProbabilityGenerator thread_starting_model = parseProbabilityGenerator( "Posting", number_of_members, next_line, index );
				index += number_of_elements_for_probability_model;
				ProbabilityGenerator thread_reply_model = parseProbabilityGenerator( "Replying", number_of_members, next_line, index );
				index += number_of_elements_for_probability_model;
				ProbabilityGenerator visibility_model = parseProbabilityGenerator( "Visibility", number_of_members, next_line, index );
				index += number_of_elements_for_probability_model;
				int 				 number_of_topics = Integer.parseInt( next_line[index] );
				index++;
				ProbabilityGenerator posting_interestingness_model = parseProbabilityGenerator( "Post Interestingness", number_of_members, next_line, index );
				index += number_of_elements_for_probability_model;
				double 				 base_topic_interest = Double.parseDouble( next_line[index] );
				index++;
				double 				 age_attenuation = Double.parseDouble( next_line[index] );
				index++;
				double 				 energy_drop_per_post = Double.parseDouble( next_line[index] );
				index++;
				int    				 days_to_gain_back_energy = Integer.parseInt( next_line[index] );
				index++;
				ProbabilityGenerator topic_interest_level_generator = parseProbabilityGenerator( "Topic Interest Level", number_of_members, next_line, index );
				index += number_of_elements_for_probability_model;
				ProbabilityGenerator self_reply_generator = parseProbabilityGenerator( "Self Reply", number_of_members, next_line, index );
				

				configs.add( new CommunityConfiguration( config_name, number_of_runs, number_of_days, number_of_members, 
														 new MemberBehaviorConfiguration( base_topic_interest, 
																 						  topic_interest_level_generator, 
																 						  age_attenuation, 
																 						  energy_drop_per_post, 
																 						  days_to_gain_back_energy, 
																 						  self_reply_generator), 
														 thread_starting_model, thread_reply_model, visibility_model, posting_interestingness_model, 
														 number_of_topics ) );
			}
			else
				System.err.println( "Invalid line in configuration file - incorrect number of columns: " + next_line.length );
		}
		
		return configs;
	}

	private static ProbabilityGenerator parseProbabilityGenerator(String generator_label, int number_of_increments, String[] next_line, int i)
	{
		String generator_type = next_line[i];
		double	exponent = Double.parseDouble( next_line[i+1] );
		double	base_probability = Double.parseDouble( next_line[i+2] );
		double	mean = Double.parseDouble( next_line[i+3] );
		double	variance = Double.parseDouble( next_line[i+4] );
		double	lower_bound = Double.parseDouble( next_line[i+5] );
		double	upper_bound = Double.parseDouble( next_line[i+6] );
		double	curve_coefficient = Double.parseDouble( next_line[i+7] );

		if (generator_type.equalsIgnoreCase( "Simple" ))
		{
			return new SimpleGenerator(generator_label, lower_bound, upper_bound);
		}
		else
			if (generator_type.equalsIgnoreCase( "PowerCurveGaussian" ))
			{
				PowerCurveCalculator pcc = new PowerCurveCalculator( number_of_increments, lower_bound, upper_bound, curve_coefficient, exponent );
				GaussianRandomGenerator generator = new GaussianRandomGenerator( generator_label, base_probability, mean, variance );
				return new PowerCurveGaussianProbabilityGenerator( generator_label, pcc, generator );
			}
			else
				if (generator_type.equalsIgnoreCase( "Gaussian" ))
				{
					return new GaussianRandomGenerator( generator_label, base_probability, mean, variance );
				}
				else
					if (generator_type.equalsIgnoreCase( "Constant" ))
					{
						return new ConstantProbabilityGenerator( generator_label, base_probability );
					}
		
		return null;
	}
}
