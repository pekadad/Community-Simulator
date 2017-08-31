package communitySimulator;

import java.util.ArrayList;
import java.util.Iterator;

public class CommunityDiscussionBoardPost
{
	static Randomizer 						randomizer = new Randomizer();
	String									subject = null;
	CommunityMember							author = null;
	CommunityDiscussionBoardPost			parent = null;
	CommunityDiscussionBoard				discussion_board = null;
	Topic									topic = null;
	double									interestingness = 0.0;
	int										timestamp = 0;
	ArrayList<CommunityDiscussionBoardPost>	replies = null;
	int										total_replies = 0;
	

	public CommunityDiscussionBoardPost( CommunityDiscussionBoard discussion_board, 
										 String subject, 
										 CommunityMember author, 
										 CommunityDiscussionBoardPost parent, 
										 double interestingness, 
										 int timestamp, 
										 Topic topic )
	{
		this.discussion_board = discussion_board;
		this.subject = subject;
		this.author = author;
		this.parent = parent;
		this.interestingness = interestingness;
		this.timestamp = timestamp;
		this.replies = new ArrayList<CommunityDiscussionBoardPost>();
		this.topic = topic;
	}

	public String getSubject()
	{
		return subject;
	}
	
	public CommunityMember getAuthor()
	{
		return author;
	}


	public CommunityDiscussionBoardPost getParent()
	{
		return parent;
	}


	public Topic getTopic()
	{
		return topic;
	}


	public double getInterestingness()
	{
		return interestingness;
	}


	public int getTimestamp()
	{
		return timestamp;
	}


	public ArrayList<CommunityDiscussionBoardPost> getReplies()
	{
		return replies;
	}
	
	public Iterator<CommunityDiscussionBoardPost> getReplyIterator()
	{
		return getReplies().iterator();
	}
	
	public int getTotalReplies()
	{
		return total_replies;
	}


	public CommunityDiscussionBoardPost postReply( CommunityMember member, int day )
	{
		total_replies++;
		return discussion_board.postReply( member, day, getTopic(), this.subject + "." + total_replies, this );
	}
	
	public CommunityDiscussionBoardPost addReply( CommunityDiscussionBoardPost reply )
	{
		replies.add( reply );
		return reply;
	}	

}
