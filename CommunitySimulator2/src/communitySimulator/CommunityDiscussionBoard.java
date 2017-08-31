package communitySimulator;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;

public class CommunityDiscussionBoard
{
	ArrayList<CommunityDiscussionBoardPost> main_threads = null;
	ArrayList<CommunityDiscussionBoardPost> all_messages = null;
	ProbabilityGenerator interest_generator = null;
	
	public CommunityDiscussionBoard( ProbabilityGenerator interest_generator )
	{
		main_threads = new ArrayList<CommunityDiscussionBoardPost>();
		all_messages = new ArrayList<CommunityDiscussionBoardPost>();
		this.interest_generator = interest_generator;
	}
	
	public void simulateDay( CommunityMembers members, int day )
	{
		CommunitySimulator.output_stream.println( "**** Processing day " + day );
		for (Iterator<CommunityMember> i = members.iterator(); i.hasNext(); )
		{
			CommunityMember member = i.next();
			member.simulateDay( this, day );
		}
	}

	public CommunityDiscussionBoardPost addPost( CommunityMember member, int day, Topic topic )
	{
		CommunityDiscussionBoardPost new_post = new CommunityDiscussionBoardPost( this, "" + threadCount(), member, null, interest_generator.getValue( 0 ) , day, topic );
		
		getAllMainThreads().add( new_post );
		
		// Add at the front of the list so that reading later on is in reverse chronological order
		getAllMessages().add( 0, new_post );
		return new_post;
	}
	
	public CommunityDiscussionBoardPost postReply(CommunityMember member, int day, Topic topic, String subject, CommunityDiscussionBoardPost parent )
	{
		CommunityDiscussionBoardPost new_post = new CommunityDiscussionBoardPost( this, subject, member,  parent,  interest_generator.getValue( 0 ), day, topic );
		parent.addReply( new_post );
		
		// TODO Figure out how to get the reading of messages / replies to be in reverse chronological order - the following doesn't work because it results in modifying the collection during iteration later on
		// Add at the front of the list so that reading later on is in reverse chronological order
		getAllMessages().add( 0, new_post );

		// Now add to the parent's count
		CommunityDiscussionBoardPost parent_post = parent;
		while (parent_post != null)
		{
			parent_post.total_replies++;
			parent_post = parent_post.getParent();
		}
		return new_post;
	}

	private ArrayList<CommunityDiscussionBoardPost> getAllMainThreads()
	{
		return main_threads;
	}
	
	public Iterator<CommunityDiscussionBoardPost> getMainThreadIterator()
	{
		return getAllMainThreads().iterator();
	}
	
	private ArrayList<CommunityDiscussionBoardPost> getAllMessages()
	{
		return all_messages;
	}
	
	public Iterator<CommunityDiscussionBoardPost> getAllMessageIterator()
	{
		return getAllMessages().iterator();
	}
	
	public int threadCount()
	{
		return getAllMainThreads().size();
	}

	public int totalMessageCount()
	{
		int total_count = 0;
		if (getAllMainThreads() != null)
			for (Iterator<CommunityDiscussionBoardPost> i = getMainThreadIterator(); i.hasNext(); )
			{
				CommunityDiscussionBoardPost post = i.next();
				total_count += post.total_replies + 1;
			}

		return total_count;
	}
	
	public void output(int number_of_days, CommunityMembers members, PrintStream out)
	{
		out.println( "******" );
		out.println( "\tTotal threads posted in " + number_of_days + " days is " + threadCount() );
		out.println( "\tTotal messages posted in " + number_of_days + " days is " + totalMessageCount() );
		out.println( "\tMessages: "  );
		//outputPostList( messages, "\t\t", out );
	}
	
	public void outputPostList( ArrayList<CommunityDiscussionBoardPost> posts, String prefix, PrintStream out )
	{
		if (posts != null)
			for (Iterator<CommunityDiscussionBoardPost> i = posts.iterator(); i.hasNext(); )
			{
				CommunityDiscussionBoardPost post = i.next();
				out.println( prefix + "Post\t" + post.getSubject() +"\tby:\t" + post.getAuthor().getName() + "\ton day:\t" + post.getTimestamp() + 
							"\tTotal replies: " + post.getTotalReplies() + "\tInterestingness: " + post.getInterestingness() );
				outputPostList( post.getReplies(), prefix + "\t", out );
			}
	}

}
