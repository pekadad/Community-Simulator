package communitySimulator;

import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;

public class CommunityMember
{
	static int					NEVER = -1;
	static Randomizer			randomizer = new Randomizer();
	static DecimalFormat		probability_formatter = new DecimalFormat( "#.00000000" );
	static double				FULL_ENERGY = 1.0;
	
	MemberBehaviorConfiguration	config = null;
	
	String						name = null;
	double						probability_to_start_conversation = 0.0;
	double						probability_to_reply_to_conversation = 0.0;
	double						community_visibility = 0.0;
	
	int							main_messages_posted = 0;
	int							replies_posted = 0;

	ArrayList<TopicInterest>	topics_of_interest = null;
	ArrayList<Double>			topic_probabilities = null;
	
	int 						day_of_last_post = NEVER;
	double						energy_to_post = FULL_ENERGY;
	
	
	public CommunityMember( String name, double probability_to_start_conversation, double probability_to_reply_to_conversation, double community_visibility,
							ArrayList<Topic> topics, MemberBehaviorConfiguration	config )
	{
		// TODO need to generalize visibility to reflect a more realistic network and not a simple single value for a member
		this.name = name;
		this.probability_to_start_conversation = probability_to_start_conversation;
		this.probability_to_reply_to_conversation = probability_to_reply_to_conversation;
		this.community_visibility = community_visibility;
		this.config = config;
		setTopicsOfInterest( topics );
	}
	
	private void setTopicsOfInterest(ArrayList<Topic> topics)
	{
		this.topics_of_interest = new ArrayList<TopicInterest>();
		this.topic_probabilities = new ArrayList<Double>();
		
		if (topics != null)
		{
			double	total_probabilities = 0.0;
			for (int i = 0; i < topics.size(); i++)
			{
				double interest_level = config.getInterestLevelForNextTopic( i );
				if (interest_level > 0.0)
					total_probabilities += interest_level;
				addTopicOfInterest( topics.get(i), interest_level );
			}
			
			// Now need to break up the range 0.0 ... 1.0 into sizes corresponding with the interest levels for the individual topics
			// Because some probability models might calculate to <=0.0 probabilities, it is possible that total probabilities is 0
			if (total_probabilities <= 0.0)
			{
				// Make all topics equally likely
				double probability = 1.0 / topics.size();
				double probability_sum = 0.0;
				for (int i = 0; i < topics.size(); i++)
				{
					this.topic_probabilities.add( new Double( probability_sum ) );
					probability_sum += probability; 
				}
			}
			else
			{
				double cumulative_probability = 0.0;
				// distribute probability of topics across those that are >= 0
				for (int i = 0; i < this.topics_of_interest.size(); i++)
				{
					double probability_of_this_topic = this.topics_of_interest.get( i ).getInterestLevel();
					double probability_max_for_next_topic = (probability_of_this_topic > 0.0) ? (probability_of_this_topic / total_probabilities) : 0;
					cumulative_probability += probability_max_for_next_topic; 
					this.topic_probabilities.add( new Double( cumulative_probability ) );
				}
			}
		}
	}

	public TopicInterest addTopicOfInterest( Topic topic, double interest_level )
	{
		TopicInterest new_topic_interest = new TopicInterest( topic, interest_level );
		topics_of_interest.add( new_topic_interest );
		return new_topic_interest;
	}
	public TopicInterest addTopicOfInterest( String topic, double interest_level )
	{
		TopicInterest new_topic_interest = new TopicInterest( topic, interest_level );
		topics_of_interest.add( new_topic_interest );
		return new_topic_interest;
	}

	public String getName()
	{
		return name;
	}

	public double getProbabilityToStartConversation()
	{
		return probability_to_start_conversation;
	}

	public double getProbabilityToReplyToConversation()
	{
		return probability_to_reply_to_conversation;
	}

	public double getCommunityVisibility()
	{
		return community_visibility;
	}
	
	public void simulateDay( CommunityDiscussionBoard db, int day )
	{
		int number_of_posts = postUntilDone( db, day );
		
		// Check if the member will reply to any messages
		number_of_posts += checkForReplies( db.getMainThreadIterator(), day );
		if (number_of_posts == 0)
			recoverEnergy(day);
	}
	
	private int postUntilDone( CommunityDiscussionBoard db, int day )
	{
		boolean done = false;
		int number_of_posts = 0;
		
		while (!done)
			// Check to see if this member would post today
			if (memberWouldPost( day ))
			{
				CommunityDiscussionBoardPost post = db.addPost( this, day, topicOfPost() );
				//CommunitySimulator.output_stream.println( "Member\t" + getName() + " posted a message about " + post.getTopic().getLabel() + " with interestingness of " + post.getInterestingness() );
				number_of_posts++;
				main_messages_posted++;
				spendEnergyPosting( day );
				CommunitySimulator.output_stream.println( "\tDay: " + day + 
														  "\tCommunity member " + getName() + 
														  " posted " + post.getSubject() + 
														  " with interestingness of " + post.getInterestingness() +
														  " about topic: " + post.getTopic().getLabel() );
				//CommunitySimulator.output_stream.println( "\tDay: " + day + "\tCommunity member " + getName() + " has energy level of " + energy_to_post );
			}
			else
				done = true;
		
		return number_of_posts;
	}
	
	private boolean memberWouldPost( int day )
	{
		double probability_to_post = getProbabilityToStartConversation();
		
		// TODO implement some means to factor in "fear of asking in a large group"

		probability_to_post *= energy_to_post;
		return randomizer.nextDouble() < probability_to_post;
	}
	
	private void recoverEnergy( int day )
	{
		if ((day - day_of_last_post) > config.getDaysToGainAllEnergy())
			energy_to_post = FULL_ENERGY;
		else
		{
			energy_to_post /= config.getEnergyDropPerPost();
			if (energy_to_post > FULL_ENERGY)
				energy_to_post = FULL_ENERGY;
		}
	}

	private void spendEnergyPosting(int day)
	{
		day_of_last_post = day;
		energy_to_post *= config.getEnergyDropPerPost(); 
	}

	private Topic topicOfPost()
	{
		if (topic_probabilities.size() > 0)
		{
			double random_value = randomizer.nextDouble( 0.0, 1.0 );
			
			for (int i = 0; i < topic_probabilities.size(); i++)
				if (random_value < topic_probabilities.get( i ).doubleValue())
					return topics_of_interest.get( i ).getTopic();
			// This code should not be hit because the last topic of interest should always have an interest level of 1
			return topics_of_interest.get( topics_of_interest.size() - 1 ).getTopic();
		}
		else
			return null;
	}

	private int checkForReplies( Iterator<CommunityDiscussionBoardPost> message_iterator, int day )
	{
		int number_of_replies = 0;
		if (message_iterator != null)
			for (Iterator<CommunityDiscussionBoardPost> i = message_iterator; i.hasNext(); )
			{
				CommunityDiscussionBoardPost post = i.next();
				number_of_replies += postRepliesUntilDone( post, day );
				number_of_replies += checkForReplies( post.getReplyIterator(), day );
			}
		return number_of_replies;
	}
	
	private int postRepliesUntilDone(CommunityDiscussionBoardPost post, int day)
	{
		int number_of_replies = 0;
		
		boolean done = false;
		while (!done)
			if (memberWouldReply( post, day ))
			{
				CommunityDiscussionBoardPost reply = post.postReply( this, day );
				spendEnergyPosting( day );
				number_of_replies++;
				replies_posted++;
				CommunitySimulator.output_stream.println( "****\tDay: " + day + "\tCommunity member " + getName() + " replied to " + post.getSubject() + " (from day " + post.getTimestamp() + " by member " + post.getAuthor().getName() + ") with " + reply.getSubject() + " with interestingness of " + reply.getInterestingness() + ".  Now has energy of " + energy_to_post );
				
				//CommunitySimulator.output_stream.println( "\tDay: " + day + "\tCommunity member " + getName() + " has energy level of " + energy_to_post );
			}
			else
				done = true;
		
		return number_of_replies;
	}

	private boolean memberWouldReply( CommunityDiscussionBoardPost post, int day )
	{
		// TODO - Determine how to show the effect of different types of members - e.g., "join only"
			// Above could be done by adding in an attribute that reflects how likely a member is to read any messages in any day 
		
		double probability_of_reply = calculateProbabilityOfReply( post, day );
		
/*		CommunitySimulator.output_stream.println( "Day: " + day + "\tCommunity member " + getName() + 
												  " is considering a replying to post \t" + post.getSubject() + 
												  "\twith a probability of \t" + probability_formatter.format( probability_of_reply * 100 ) + "%" +
												  "\tBase likelihood of reply is\t" + probability_formatter.format( getProbabilityToReplyToConversation() * 100 ) + "%" );
*/		
		double next_random = randomizer.nextDouble();
/*		if (next_random < probability_of_reply)
			CommunitySimulator.output_stream.println( "\tCommunity member " + getName() + 
													  " will reply - probability was " + probability_of_reply + 
													  " and random number is " + next_random );
*/		return next_random < probability_of_reply;
	}

	private double calculateProbabilityOfReply(CommunityDiscussionBoardPost post, int current_day)
	{
		double probability_of_reply = getProbabilityToReplyToConversation();
		//CommunitySimulator.output_stream.println( "\t****\tMember " + this.getName() + " considering replying to message '" + post.getSubject() +"' (authored by " + post.getAuthor().getName() + ") with a base probability of reply: " + probability_of_reply );
		// probability_of_reply += post.getInterestingness();
		probability_of_reply = probability_of_reply * (1 + post.getInterestingness());
		//CommunitySimulator.output_stream.println( "\t\t\tAfter considering post interestingness (" + post.getInterestingness() + "): " + probability_of_reply );
		
		
		// TODO - Likelihood of a member replying to the same message should go down
		// TODO - Likelihood of a member posting in the same thread should go down
		// TODO Reflect higher probability of replying to someone who is replying to you
		if (!(post.getAuthor() == this))
			probability_of_reply *= (1 + post.getAuthor().getCommunityVisibility());
		else
			probability_of_reply *= config.getProbabilityOfSelfReply();
		
		//CommunitySimulator.output_stream.println( "\t\t\tAdding author visibility:  (" + post.getAuthor().getCommunityVisibility() + ") " + probability_of_reply );
		
		probability_of_reply *= ageAttenuation( current_day, post );
		
		// TODO reflect Stan's suggestion of a member who is much more likely to reply to a message about a particular topic
		// TODO For smaller communities, is it more likely that they will have more interest in the core topics of a community?
		probability_of_reply *= (1 + topicInterest( post.getTopic() ));
		probability_of_reply *= energy_to_post;
		
		return probability_of_reply;
	}

	private double topicInterest(Topic topic)
	{
		double interest = config.getBaseInterestInTopic();
		for (int i = 0; i < topics_of_interest.size(); i++)
		{
			if (topics_of_interest.get( i ).getTopic().equals(topic) )
			{
				if (i > 0)
					return topics_of_interest.get( i ).getInterestLevel() - topics_of_interest.get( i - 1 ).getInterestLevel();
				else
					return topics_of_interest.get( i ).getInterestLevel();
			}
		}
		return interest;
	}

	private double ageAttenuation(int current_day, CommunityDiscussionBoardPost post)
	{
		int days_between = current_day - post.getTimestamp();
		return config.getAgeImpact( days_between );
	}

	public void output(PrintStream out)
	{
		String topic_list = getListOfTopics();
		
		out.println( "Name:\t" + getName() + 
					 "\tProbability to start a conversation: " + probability_formatter.format( getProbabilityToStartConversation() * 100 ) + 
					 "%\tProbability to reply to a conversation: " + probability_formatter.format( getProbabilityToReplyToConversation() * 100 ) +
					 "%\tCommunity visibility: " + probability_formatter.format( getCommunityVisibility() * 100 ) + "%" +
					 "\tMain messages posted: " + getMainMessagesPosted() +
					 "\tReplies posted: " + getRepliesPosted() +
					 "\t" + topic_list );
	}
	
	public void outputAsTableRow( PrintStream out, int run_number, String experiment, boolean include_headers, int number_of_members )
	{
		if (include_headers)
		{
			out.println( "Experiment\t" + 
						 "Run\t" +
						 "Number of members\t" +
						 "Name\t" +
						 "Probability to start a conversation\t" +
						 "Probability to reply to a conversation\t" +
						 "Community Visibility\t" +
						 "Threads started\t" +
						 "Replies posted"
					);
		}
		out.println( experiment + "\t" + 
					 run_number + "\t" +
					 number_of_members + "\t" +
					 getName() + "\t" +
					 probability_formatter.format( getProbabilityToStartConversation() * 100 ) +"%\t" +
					 probability_formatter.format( getProbabilityToReplyToConversation() * 100 ) + "%\t" + 
					 probability_formatter.format( getCommunityVisibility() * 100 ) + "%\t" + 
					 getMainMessagesPosted() + "\t" + getRepliesPosted() );
	}

	public void outputTableRow(int run, int day, PrintStream out)
	{
		out.println( run + "\t" +
					 day + "\t" +
					 getName() + "\t" +
				 	 probability_formatter.format( getProbabilityToStartConversation() * 100 ) +  "\t" +
				 	 probability_formatter.format( getProbabilityToReplyToConversation() * 100 ) + "\t" +
				 	 probability_formatter.format( getCommunityVisibility() * 100 ) + "\t" +
				 	 getMainMessagesPosted() + "\t" +
				 	 getRepliesPosted() );
	}
	private String getListOfTopics()
	{
		String return_value = "Interests:\t";
		for (int i = 0; i < topics_of_interest.size(); i++)
		{
			TopicInterest topic_of_interest = topics_of_interest.get( i );
			return_value += "(" + topic_of_interest.getTopic().getLabel() + ", " + probability_formatter.format( topic_of_interest.getInterestLevel() ) + ")\t";
		}
		return return_value;
	}
	
	public int getMainMessagesPosted()
	{
		return main_messages_posted;
	}

	public int getRepliesPosted()
	{
		return replies_posted;
	}

	public static void main( String[] args )
	{
		for (int i = 0; i < 200; i++)
			System.out.println( "For " + i + ": " + Math.pow( 0.8, i ));
	}


}
