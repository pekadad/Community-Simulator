package communitySimulator;

import java.io.BufferedReader;
import java.io.IOException;

public class DelimitedFileReader
{

	public static String[] readNextLine( BufferedReader input ) throws IOException
	{
		String input_line = null;
		while ((input_line = input.readLine()) != null)
		{
			if (input_line != null && input_line.length() > 0 && !input_line.startsWith( "#" ))
			{
				String[] components = input_line.split( "\t" );
				
				return components;
			}
		}
		return null;
	}

}
