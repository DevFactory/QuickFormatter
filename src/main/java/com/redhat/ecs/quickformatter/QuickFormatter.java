package com.redhat.ecs.quickformatter;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

public class QuickFormatter 
{
	private String _inputfile="";
	private String _outputfile="";
	private String _delimiter=" ";
	private String _template="";
	private Boolean _isXML = false;

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
		//create a velocity context
	    	Velocity.init();
	    VelocityContext vc = new VelocityContext();
	    
	    vc.put("DATA", data);			// put our data in the context
	        
	    //go get the template
	    Template template = null;
	    try
	    {
	    		template = Velocity.getTemplate(_template);
	    }
	    catch( Exception e )
	    {
	       exitWithMessage("Error processing template:\n"+e.toString());
	    }

		// this is all kind of crap
	    
		StringWriter sw = new StringWriter();
		template.merge( vc, sw );
	    
		StringWriter outputsw = new StringWriter();
		String outputString = sw.toString();

		if (_isXML)
		{
			Document doc;
			try 
			{
				doc = DocumentHelper.parseText(sw.toString());
				OutputFormat format = OutputFormat.createPrettyPrint();  
				XMLWriter xw = new XMLWriter(outputsw, format);  
				xw.write(doc);
				outputString = outputsw.toString();
			} 
			catch (Exception e) 
			{
				exitWithMessage("Output was not parsable XML:\n"+e.toString());
			} 
		}
		
	    // out to screen or output file if there is one
	    if (_outputfile == "")
	    {
			System.out.println(outputString);
	    }
	    else
	    {
	        try 
	        {
	        		File foFile = new File(_outputfile);
	        		FileWriter fw = new FileWriter(foFile);
	        		//template.merge(vc,fw);
	        		fw.write(outputString);
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

	public void setIsXML(Boolean isXML) {
		this._isXML = isXML;
	}

	public Boolean getIsXML() {
		return _isXML;
	}


}
