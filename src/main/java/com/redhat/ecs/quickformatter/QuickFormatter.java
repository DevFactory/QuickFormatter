package com.redhat.ecs.quickformatter;

import java.io.*;
import java.util.ArrayList;

import org.apache.velocity.*;
import org.apache.velocity.app.*;

public class QuickFormatter 
{
	private String _inputfile="";
	private String _outputfile="";
	private String _delimiter=" ";
	private String _template="";

	public QuickFormatter() {}

	public void exitWithMessage(String message)
	{
		System.out.println("ERROR: "+message+"\n\n");
		System.exit(1);
	}
	
	public void doFormatting()
	{
		processTemplate(processInput());
	}
	
	public ArrayList<ArrayList<String>> processInput()
	{
		ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();

		//check for existence of input file
		FileInputStream  fstream = null;
        try 
        {
			fstream = new FileInputStream(_inputfile);
		} 
        	catch (FileNotFoundException e) 
        	{
			exitWithMessage("Input file \"" + _inputfile + "\" not found.");
		}
        	
         DataInputStream in = new DataInputStream(fstream);
         BufferedReader br = new BufferedReader(new InputStreamReader(in));
         String strLine;
         
         //Read File Line By Line
            try 
            {
	            while ((strLine = br.readLine()) != null)   
	            {
	                if (strLine.trim().length() > 0)
	                {
	            			data.add(new ArrayList<String>());		//new line
	                		String[] elements=strLine.split(_delimiter);
	                    for (int i=0;i<elements.length;i++)
	                    {
	                        elements[i] = elements[i].trim();
	                        if (elements[i].length() > 0) data.get(data.size()-1).add(elements[i]);
	                    }
	                }
	            }
	            in.close();
            }
	        catch(Exception e)
	        {
	        		exitWithMessage("some error processing:" + e.toString());
	        }
			return data;
		
	}
	
	public void processTemplate(ArrayList<ArrayList<String>> data)
	{
	    	Velocity.init();
	    VelocityContext vc = new VelocityContext();
	    
	    vc.put("DATA", data);
	        
	    Template template = null;
	    try
	    {
	    		template = Velocity.getTemplate(_template);
	    }
	    catch( Exception e )
	    {
	       exitWithMessage("Error processing template:\n"+e.toString());
	    }
	    
	    if (_outputfile == "")
	    {
	    		StringWriter sw = new StringWriter();
	    		template.merge( vc, sw );
	    		System.out.println(sw );
	    }
	    else
	    {
			File foFile= null;
			FileWriter fw = null;
	        try 
	        {
				foFile= new File(_outputfile);
	        		fw = new FileWriter(foFile);
	        		template.merge(vc,fw);
	        		fw.flush();
	        		System.out.println("Output written to file \""+_outputfile+"\" successfully.\n");
			} 
	        	catch (Exception e) 
	        	{
				exitWithMessage("Couldn't open output file \"" + _outputfile + "\" not found.");
			}

	    }

	}
	
	//mostly superfluous accessors & mutators below (they got auto-generated, it would have been rude to say no)
	public void setInputfile(String _inputfile) 
	{
		this._inputfile = _inputfile;
	}

	public String getInputfile() 
	{
		return _inputfile;
	}

	public void setOutputfile(String _outputfile) 
	{
		this._outputfile = _outputfile;
	}

	public String getOutputfile() 
	{
		return _outputfile;
	}

	public void setDelimiter(String _delimiter) 
	{
		this._delimiter = _delimiter;
	}

	public String getDelimiter() 
	{
		return _delimiter;
	}

	public void setTemplate(String _template) 
	{
		this._template = _template;
	}

	public String getTemplate() 
	{
		return _template;
	}

}
