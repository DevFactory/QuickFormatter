# QuickFormatter

QuickFormatter is a simple command line utility written in Java that reads
delimited data from text files (e.g. a CSV file) and produces output using
Apache Velocity templates.  

The language reference for Apache Velocity is [here](http://velocity.apache.org/engine/releases/velocity-1.5/user-guide.html#velocity_template_language_vtl:_an_introduction).

Sample templates are included for docbook and wiki text tables.
 
### Requirements
 
1. You need to have a Java Virtual Machine (JVM) installed.  Only tested with 1.6
2. You need to be capable of creating Apache Velocity templates for your output.  Samples are included.

## Install
1. Download the package.  Scroll back up the page, click on the Download button and click on `QuickFormatter.zip`
2. Unzip it somewhere, e.g. `~/Applications/QuickFormatter/`.  This directory will be referred to as `QF_PATH` for the rest of this document.
4. Done

## Building
If you need to build QuickFormatter from source:

1. Download the source or clone the repo
2. build using `mvn assembly:assembly` to build with included dependencies


## Usage

QuickFormatter is run using the `java -jar` command:

    $ java -jar QF_PATH/QuickFormatter.jar 

For convenience you could create an alias for this, e.g. `alias qf="java -jar QF_PATH/QuickFormatter.jar"`

Running QuickFormatter without parameters displays the help:

    usage: java -jar QuickFormatter.jar -i inputfile [-o outputfile] [-d delimiter] [-x]
     -d,--delimiter <arg>   delimiter
     -h,--help              Print this usage information
     -i,--input <arg>       input file
     -o,--ouput <arg>       output file
     -t,--template <arg>    template file
     -x,--XML               output is XML (pretty printing)

* The delimiter argument supplies the string (often a single character) that separates each item of each line in the input file.  For a CSV files this is a comma (,).
* The input file is the file that you are reading data from.
* The output file is the file that the output will be written to.  It is optional and if you don't supply it then the output goes to standard out.
* The template file is an Apache Velocity template that is used to format the data.
* If your template produces XML output then -x argument will format the XML nicely.  If the output from the template is not valid XML then it will fail with an error.

### Example
    $ cd ~/Applications/QuickFormatter/
    $ java -jar QuickFormatter.jar -i survey.csv -o results.xml -d , -t Samples/docbook-table.vm -x

## How Does it Work ?

QuickFormatter takes the input file and reads it into a two dimensional data 
structure.  This structure is then inserted into the template that you specify.
The template is processed and the output is sent either to standard out or to
the file that you specify.

The data structure is an array of arrays (actually `ArrayList`s) and is inserted into the template with 
the name `DATA`.  Some example of accessing elements of this data structure:

     ## show first item of the first row
     $DATA.get(0).get(0)
     
     ## show number of items in the third row
     $DATA.get(2).size()

     ## 

Note that just like normal Java arrays the indexes start at `0` and not `1`.

Generally you would iterate over this in a nested manner using `#foreach`. The
example below creates a simple HTML table:
   
    <table>
    #foreach( $line in $DATA)
      <tr>
      #foreach( $item in $DATA)
        <td>$item</td>
      #end
      </tr>
     #end
     </table>


## Sample Templates

The following templates are included:

`simple-docbook-table.vm` and `simple-wiki-table.vm` both output very simple
tables in docbook XML and wiki text respectively.

`docbook-table.vm` and `wiki-table.vm` both output a more complex table than the
'simple' table examples. It uses the first row of your data file as the table
headers. If any row has less items than the first then it appends empty cells
for them.

`docbook-variablelist.vm` outputs a docbook XML variable list using the first
item in each row as the `<term>` and each other item as an `<itemizedlist>`.

`simple-trac-table.vm` outputs a simple table for a Trac wiki.




