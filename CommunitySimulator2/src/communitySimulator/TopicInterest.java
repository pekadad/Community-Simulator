package communitySimulator;

public class TopicInterest
{
	Topic 	topic;
	double	interest_level = 0.0;
	
	public TopicInterest( Topic t, double interest_level )
	{
		this.topic = t;
		this.interest_level = interest_level;
	}

	public TopicInterest( String t, double interest_level )
	{
		topic = new Topic( t );
		this.interest_level = interest_level;
	}

	public Topic getTopic()
	{
		return topic;
	}

	public double getInterestLevel()
	{
		return interest_level;
	}
	
}
