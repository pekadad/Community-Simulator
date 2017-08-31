package communitySimulator;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;

public class CommunityMembers
{
	ArrayList<CommunityMember> members = null;

	public CommunityMembers( CommunityMemberFactory factory )
	{
		members = createMembers( factory );
	}
	
	public ArrayList<CommunityMember> createMembers( CommunityMemberFactory factory )
	{
		ArrayList<CommunityMember> members = new ArrayList<CommunityMember>( factory.numberOfMembers() );
		
		while (factory.hasNext())
			members.add( factory.next() );
		
		return members;
	}

	public Iterator<CommunityMember> iterator()
	{
		return members.iterator();
	}
	
	public int getNumberOfMembersWhoPosted()
	{
		int number_of_members = 0;
		for (Iterator<CommunityMember> i = iterator(); i.hasNext(); )
		{
			CommunityMember member = i.next();
			if ((member.getMainMessagesPosted() + member.getRepliesPosted()) > 0)
				number_of_members++;
		}
		return number_of_members;
	}

	public void output( PrintStream out )
	{
		for (Iterator<CommunityMember> i = members.iterator(); i.hasNext(); )
		{
			CommunityMember member = i.next();
			member.output( out );
		}
	}
	
	public void outputAsTable( PrintStream out, int run_number, String experiment, int number_of_members )
	{
		boolean include_headers = true;
		for (Iterator<CommunityMember> i = members.iterator(); i.hasNext(); )
		{
			CommunityMember member = i.next();
			member.outputAsTableRow( out, run_number, experiment, include_headers, number_of_members );
			include_headers = false;
		}
	}
	
	public void outputTable( int run, int day, PrintStream out, boolean include_headers )
	{
		if (include_headers)
			out.println( "Run\t" +
					 	 "Day\t" +
						 "Name\t" +
					 	 "Probability to start conversation\t" +
					 	 "Probability to reply\t" +
					 	 "Visbility\t" +
					 	 "Number of main threads started\t" +
					 	 "Number of replies" );
		for (Iterator<CommunityMember> i = members.iterator(); i.hasNext(); )
		{
			CommunityMember member = i.next();
			member.outputTableRow( run, day, out );
		}
	}

}
