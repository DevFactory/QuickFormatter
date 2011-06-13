package com.redhat.ecs.quickformatter;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;



public class App {

	private static String input_file = "";
	private static String output_file = "";
	private static String delimiter = " ";
	private static String template_file = "";
	private static Boolean isXML = false;

	public static void main(String[] args) 
	{
		App thisApp = new App();
		thisApp.parseCommandLine(args);

		QuickFormatter qf = new QuickFormatter();
		qf.setDelimiter(delimiter);
		qf.setInputfile(input_file);
		qf.setOutputfile(output_file);
		qf.setTemplate(template_file);
		qf.setIsXML(isXML);
		qf.doFormatting();
		
	}

	// ================================ setup command line parameters
	   public void parseCommandLine(String args[]) 
	   {
		   // Create a Parser
		   CommandLineParser parser = new BasicParser( );
		   Options options = new Options( );

		   try
		   {
			   options.addOption("h", "help", false, "Print this usage information");
			   options.addOption("i", "input", true, "input file");
			   options.addOption("o", "ouput", true, "output file");
			   options.addOption("d", "delimiter", true, "delimiter");
			   options.addOption("t", "template", true, "template file");
			   options.addOption("x", "XML", false, "output is XML (pretty printing)");
			   
			   
			   CommandLine commandLine = parser.parse( options, args );
		      
		      if ( commandLine.hasOption('h') ) printUsage(options, true);
		      
		      if ( !commandLine.hasOption('i') ) 
		      {
		    	  	System.out.println("ERROR: Input file not specified.\n\n");
		    	  	printUsage(options, true);
		      }

		      if ( !commandLine.hasOption('t') ) 
		      {
		    	  	System.out.println("ERROR: Template file not specified.\n\n");
		    	  	printUsage(options, true);
		      }
	
		      if ( commandLine.hasOption('i')) input_file = commandLine.getOptionValue('i');
		      if ( commandLine.hasOption('o')) output_file = commandLine.getOptionValue('o');
		      if ( commandLine.hasOption('d')) delimiter = commandLine.getOptionValue('d');
		      if ( commandLine.hasOption('t')) template_file = commandLine.getOptionValue('t');
		      if ( commandLine.hasOption('x')) isXML = true;
		   }
		   catch (Exception e) 
		   {
			   System.out.println("ERROR: Unexpected error occured when parsing the command line.\n\n");
			   e.printStackTrace();
			   System.out.println("\n\n");
			   printUsage(options, true);
		   }
	   }
	   
	   private  void printUsage(Options options, Boolean andExit) 
	   {
		   String HEADER = "";
		   String USAGE = "java -jar QuickFormatter.jar -i inputfile [-o outputfile] [-d delimiter] [-x]";
		   String FOOTER = "\n\n";
		   HelpFormatter helpFormatter = new HelpFormatter( );
		   helpFormatter.setWidth( 100 );
		   helpFormatter.printHelp( USAGE, HEADER, options, FOOTER);
		   if (andExit)System.exit(0);	
	   }
	
	
}
