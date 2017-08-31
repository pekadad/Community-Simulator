package communitySimulator;

public class Topic
{
	String label;
	
	static int possible_labels_used = 0;
	static String possible_labels[] =
		{
				"Accounting Advisory",
				"Acquisitions",
				"Advertisements",
				"Aerospace and Defense",
				"Analyses, Recommendations and Reports",
				"Analyst Reports",
				"Announcements",
				"Annual Reports",
				"Assessment",
				"Assurance Services",
				"Automotive",
				"Banking and Securities",
				"Benchmarks",
				"Best Practices",
				"Blog Homepage",
				"Brand Elements",
				"Brand Management",
				"Budgeting and Forecasting",
				"Business Cases",
				"Business Model",
				"Business Plans and Initiatives",
				"Business Process Solutions (SC)",
				"Case Studies",
				"Certification and Curriculum",
				"Change Management and Communication Materials",
				"Client Lists",
				"Client Training Materials",
				"Communications Tools",
				"Community Homepage",
				"Community Involvement",
				"Company Analyses",
				"Compensation",
				"Competitor Intelligence",
				"Consumer Products",
				"Contact Lists",
				"Controls Transformation and Assurance",
				"Corporate Finance Advisory",
				"Corporate Governance",
				"Country and Economic Analyses",
				"Crisis Management",
				"Customers",
				"Cyber Risk Services",
				"Data Risk",
				"Debt",
				"Deloitte Analytics (DA)",
				"Deloitte Hosted External Event",
				"Deloitte Internal Event",
				"Deloitte Legal (SC)",
				"Discussion Main Page",
				"Dispute Resolution",
				"Diversified Services",
				"Diversity and Inclusion",
				"Divestitures",
				"e-Books",
				"Education",
				"e-Learning",
				"Employee and Pension Costs",
				"Employee Benefits",
				"Enterprise Applications",
				"Ethics",
				"Extended Enterprise",
				"External Articles and Newsletters",
				"Featured Stories",
				"Finance",
				"Finance Transformation",
				"Financial Models",
				"Financial Reporting",
				"Financial Statement Audit (SC)",
				"Forensic",
				"Fraud",
				"General Financial Advisory",
				"Global Business Tax Services",
				"Global Employer Services (SC)",
				"Globalization Strategy",
				"Governance, Regulatory, and Risk",
				"Governance, Risk and Compliance (GRC)",
				"Government Support",
				"Health Care",
				"Human Capital",
				"Indirect Tax (SC)",
				"Industrial Products and Services",
				"Industry and Market Analyses",
				"Information Technology",
				"Infrastructure and Capital Projects",
				"Innovation",
				"Instructor-led Program Materials",
				"Insurance",
				"Integrated Services (SC)",
				"Internal Communications Plan",
				"Internal Newsletters",
				"International Government Organizations",
				"International, Not-for-Profit, and Non-Government Organizations",
				"Interviews",
				"Inventory",
				"Investment Management",
				"Investments",
				"Job Aids",
				"Laws and Regulations",
				"Leadership Communication",
				"Legal Technical Materials",
				"Life Sciences",
				"Liquidity and Capital Management",
				"Live Instructor-led",
				"Live Online Event",
				"Local or Municipal Government",
				"M and A Transaction Services",
				"Media",
				"Media Interviews",
				"Mergers and Acquisitions (M and A)",
				"Methods",
				"Mining",
				"National Government",
				"Non-Deloitte Event",
				"Office Facilities and Service",
				"Oil and Gas",
				"Online Game",
				"Organization Design",
				"Organization Overviews",
				"Outsourcing (Consulting)",
				"People Profiles",
				"Performance Management",
				"Points of View",
				"Policy and Compliance",
				"Policy, Standards and Compliance",
				"Postal and Logistics",
				"Power",
				"Practice Descriptions, Plans and Initiatives",
				"Press Releases",
				"Pricing",
				"Process Design",
				"Process Industries",
				"Product and Services Documentation",
				"Project Documentation and Workpapers",
				"Project Finances and Resources",
				"Project Lessons Learned",
				"Project Reporting and Communications",
				"Project Scope and Contracts",
				"Project Workplans",
				"Promotional Materials",
				"Proposals",
				"Qualifications",
				"Quality and Risk Management Materials",
				"Real Estate",
				"Real Estate and Infrasture",
				"Recorded Event",
				"Reference Guides",
				"Regional or State Governments",
				"Requirements",
				"Restructuring Services",
				"Retail, Wholesale and Distribution",
				"Revenue Protection",
				"Risk Management",
				"Sales Presentations",
				"Shipping and Ports",
				"Speeches",
				"Strategy and Operations",
				"Strategy and Vision",
				"Succession Management",
				"Supply Chain",
				"System Design and Architecture",
				"Talent Acquisition",
				"Talent Management",
				"Talking Points",
				"Target Client Lists",
				"Tax Technical Materials",
				"Taxes",
				"Technology",
				"Technology Integration",
				"Telecommunications",
				"Testimonials and Awards",
				"Testing Materials",
				"Third Party Alliances",
				"Tools",
				"Travel, Hospitality and Leisure",
				"Valuation Services",
				"Vendor Assessments",
				"Vendor Product and Technology Analyses",
				"Virtual Instructor-led",
				"Water",
				"Win Announcements",
				"Workforce Planning"
		};
	
	public Topic( String label )
	{
		this.label = label;
	}
	
	public Topic()
	{
		this.label = getRandomLabel();
	}
	
	static private String getRandomLabel()
	{
		Randomizer randomizer = new Randomizer();
		int index = randomizer.nextInt( possible_labels_used, possible_labels.length );
		String label = possible_labels[index];
		
		// Now switch the label at that index with the next spot
		String to_move = possible_labels[possible_labels_used];
		possible_labels[possible_labels_used] = label;
		possible_labels[index] = to_move;
		possible_labels_used++;
		
		if (possible_labels_used >= possible_labels.length)
			possible_labels_used = 0;
		
		return label;
	}

	public String getLabel()
	{
		return label;
	}
	
	public String toString()
	{
		return getLabel();
	}
	
	public boolean equals( Topic t )
	{
		return getLabel().equals( t.getLabel() );
	}
	public boolean equals( String s )
	{
		return getLabel().equals( s );
	}

}
