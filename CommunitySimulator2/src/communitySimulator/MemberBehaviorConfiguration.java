package communitySimulator;

import java.io.PrintStream;
import java.util.ArrayList;

public class MemberBehaviorConfiguration
{
	double	base_interest_in_topic = 0.2;
	ProbabilityGenerator topic_interest_generator = null;
	double	age_attenuation_factor = 0.8;
	double	energy_drop_per_post = 0.8;
	int		days_to_gain_all_energy = 5;
	
	ArrayList<Double>	age_attenuation_array = null;
	
	ProbabilityGenerator self_reply_generator = null;

	/**
	 * @param base_interest_in_topic
	 * @param age_attenuation_factor
	 * @param energy_drop_per_post
	 * @param days_to_gain_all_energy
	 * @param probability_of_self_reply
	 */
	public MemberBehaviorConfiguration(double base_interest_in_topic, ProbabilityGenerator topic_interest_generator, double age_attenuation_factor,
			double energy_drop_per_post, int days_to_gain_all_energy, ProbabilityGenerator self_reply_generator )
	{
		super();
		this.base_interest_in_topic = base_interest_in_topic;
		this.topic_interest_generator = topic_interest_generator;
		this.age_attenuation_factor = age_attenuation_factor;
		this.energy_drop_per_post = energy_drop_per_post;
		this.days_to_gain_all_energy = days_to_gain_all_energy;
		this.self_reply_generator = self_reply_generator;
	}

	public double getBaseInterestInTopic()
	{
		return base_interest_in_topic;
	}
	
	public double getInterestLevelForNextTopic( int index )
	{
		return topic_interest_generator.getValue( index );
	}
	
	public double getAgeImpact( int days_between )
	{
		if (age_attenuation_array == null)
		{
			age_attenuation_array = new ArrayList<Double>();
		}
		if (days_between >= age_attenuation_array.size())
		{
			age_attenuation_array.ensureCapacity( days_between + 1 );
			for (int i = age_attenuation_array.size(); i < days_between + 1; i++)
			{
				if (i == 0)
					age_attenuation_array.add( new Double( 1.0 ) );
				else
					age_attenuation_array.add( new Double( Math.pow( getAgeAttenuationFactor(), days_between ) ) );
			}
		}
		return age_attenuation_array.get( days_between );
	}

	public double getAgeAttenuationFactor()
	{
		return age_attenuation_factor;
	}

	public double getEnergyDropPerPost()
	{
		return energy_drop_per_post;
	}

	public int getDaysToGainAllEnergy()
	{
		return days_to_gain_all_energy;
	}

	public double getProbabilityOfSelfReply()
	{
		return self_reply_generator.getValue( 0 );
	}

	public void outputHeaders( PrintStream stream )
	{
		stream.print( "Base Interest in Topics\t" + 
	   			   	  "Age Attenuation Factor\t" +
	   			   	  "Energy Drop Per Post\t" +
	   			   	  "Days to gain all energy back\t" +
	   			   	  "Probability of self-reply" );
	}
	
	public void output( PrintStream stream )
	{
		stream.print(  getBaseInterestInTopic() + "\t" + 
				   	   getAgeAttenuationFactor() + "\t" +
				   	   getEnergyDropPerPost() + "\t" +
				   	   getDaysToGainAllEnergy()  + "\t" +
				   	   getProbabilityOfSelfReply() );
	}
}
