package communitySimulator;

import java.util.ArrayList;

public class CommunityMemberFactory
{
	int number_of_members;
	int current_index = 0;
	MemberBehaviorConfiguration member_behavior_config = null;
	ProbabilityGenerator probability_to_post_generator = null;
	ProbabilityGenerator probability_to_reply_generator = null;
	ProbabilityGenerator visibility_generator = null;
	
	ArrayList<Topic> potential_topics = null;
	
	Randomizer randomizer = new Randomizer();
	
	public CommunityMemberFactory( int number_of_members, MemberBehaviorConfiguration config,
								   ProbabilityGenerator probability_to_post_generator,
								   ProbabilityGenerator probability_to_reply_generator,
								   ProbabilityGenerator visibility_generator,
								   ArrayList<Topic> potential_topics )
	{
		this.number_of_members = number_of_members;
		this.member_behavior_config = config;
		this.probability_to_post_generator = probability_to_post_generator;
		this.probability_to_reply_generator = probability_to_reply_generator;
		this.visibility_generator = visibility_generator;
		this.potential_topics = potential_topics;
	}
	
	public void reset()
	{
		current_index = 0;
	}
	
	public boolean hasNext()
	{
		return (current_index < number_of_members); 
	}
	
	public CommunityMember next()
	{
		if (hasNext())
		{
			double probability_to_post = probability_to_post_generator.getValue( current_index );
			double probability_to_reply = probability_to_reply_generator.getValue( current_index );
			double visibility = visibility_generator.getValue( current_index );
			ArrayList<Topic> this_members_topics = determineMembersTopics();
			
/*			if (current_index == 0)
			{
				CommunitySimulator.output_stream.println( "Curve value: " + curve_value + 
														  "\tprobability_to_post: " + probability_to_post + 
														  "\tprobability_to_reply: " + probability_to_reply + 
														  "\tvisibility: " + visibility );
			}
*/			
			CommunityMember community_member = new CommunityMember( "" + current_index, probability_to_post, probability_to_reply, visibility, this_members_topics, member_behavior_config );
			
			current_index++;
			
			return community_member;
		}
		else
			return null;
	}
	
	private ArrayList<Topic> determineMembersTopics()
	{
		return potential_topics;
	}

	public int numberOfMembers()
	{
		return number_of_members;
	}

}
