package communitySimulator;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;

public class Community
{
	CommunityMembers members = null;
	CommunityDiscussionBoard discussion_board = null;
	CommunityConfiguration config = null;
	ArrayList<Topic> topics = null;

	public Community( CommunityConfiguration config )
	{
		this.config = config;
		topics = createTopicList( config.getNumberOfTopics() );
		config.setTopics( topics );
		
		// Create community members
		members = new CommunityMembers( config.getCommunityMemberFactory() );
		
		// Create community discussion board
		discussion_board = new CommunityDiscussionBoard( config.getPostInterestGenerator() );
	}
	
	public ArrayList<Topic> createTopicList(int number_of_topics)
	{
		ArrayList<Topic> topic_list = new ArrayList<Topic>( number_of_topics );
		
		for (int i = 0; i < number_of_topics; i++)
		{
			topic_list.add( new Topic( ) );
		}

		return topic_list;
	}

	public void simulate( int number_of_days )
	{
		for (int day = 0; day < number_of_days; day++)
		{
			discussion_board.simulateDay( members, day );
			//discussion_board.output( day, members, System.out );
		}
	}
	
	public Iterator<CommunityMember> memberIterator()
	{
		return members.iterator();
	}
	
	public int getNumberOfMembersWhoPosted()
	{
		return members.getNumberOfMembersWhoPosted();
	}
	
	public Iterator<CommunityDiscussionBoardPost> postIterator()
	{
		return discussion_board.getMainThreadIterator();
	}
	
	public void outputMembers( PrintStream out )
	{
		members.output( out );
	}
	
	public void outputMembersAsTable( PrintStream out, int run_number, String experiment )
	{
		members.outputAsTable( out, run_number, experiment, config.getNumberOfMembers() );
	}
	
	public void outputMemberTable( int run, int day, PrintStream out, boolean include_headers )
	{
		members.outputTable(run, day, out, include_headers);
	}

	public void outputDiscussionBoard( int number_of_days, PrintStream out)
	{
		discussion_board.output( number_of_days, members, out );
	}

	public int totalThreads()
	{
		return discussion_board.threadCount();
	}

	public int totalMessages()
	{
		return discussion_board.totalMessageCount();
	}

}
