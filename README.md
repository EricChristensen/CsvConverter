# CSV Converter
## Overview of requirements, deliverables, and what Crisp is looking for
About 6 hours were spent on this project. The following requirements were asked for:
* The transformations should be configurable with an external configuration file
    * Met
* The functionality should be implemented as a library, without (significant) external
dependencies
    * Met, if ./gradlew build is acceptable. A code repo is specified as an acceptable deliverable so I assumed this was ok.
* Invalid rows should be collected, with errors describing why they are invalid (logging
them is fine for now)
    * Met 
* The data tables can have a very large number of rows
    * Assumed to be met*
  
The following deliverables were asked for:
* Running code and test suite provided through online code repo or in a tar-ball
    * Met (This repo) 
* Instructions on how to build and run the code with example data
    * Met 
* Short architectural overview and technology choices made
    * Met 
* (Basic) documentation, unless it’s completely self-documenting (to a fellow software
developer)
    * Met 
* List of assumptions or simplifications made
    * Met 
* List of the next steps you would want to do if this were a real project
    * Met

## What did Crips ask for?
* A working system
    * Met
* An extensible system that can perform various transformations
    * NOT MET
* Easy to use API and easy to understand configuration file
    * Met
* Well structured, readable and maintainable code
    * Met (In my biased opinion) 
* Tests
    * Met, with an exception (CsvHelper could use its own tests. I ran out of time working towards extensability as CsvHelper is tested through testing the converter)
* Quality of documentation
    * Met (In my biased opinion)

### NOT MET
An extensable system that can perform various transformations. This part was not met. The working system that is tested
and performs a transformation on the example input was completed fairly quickly. Being able to perform a transformation on
any input with minimal configuration was not completed. The attempt can be viewed in https://github.com/EricChristensen/CsvConverter/pull/1
and the idea for this architecture and next steps will be described.

### Next steps and architecture choices.
This originally seemed like a very simple task. I was originally going to use python to
get it done quickly, but since the row classes had 6+ columns, Kotlin made more sense for
organizing those classes. Given that this was supposed to be extended further, kotlin would
also make more sense to me because python is good for small and quick things, but the extensability
of this could grow beyond what python is nice for and even the original example made more
sense to organize with kotlin.

The architecture is pretty simple. There is a runner class that has the programs main method.
This main method can be run via two gradle tasks. One gradle task allows for specifying the input,
output, and log file via the command line, and the other task allows the user to specify each of these
via a configuration file. If the input file is specified but does not exist, the program will indicate
that and not run. If the input file does not have enough arguments, the program will also specify that
to the user and cease to run. Defaults can be used with the convertFileCliInput task.

The runner then calls the Converter class's convertAndOutput method. The Converter class has input and
output data classes as well as an inputToOutput method. There is a CsvHelper class that the converter has
access to which can read and write generically to and from Csv files. The convertAndOutput method takes in the file
path of the input, the file path of the output, and the file path of a log file. The input is read
from the csv file with the CsvHelper, converted into the output objects and then written to the output csv
file with the CsvHelper. Any conversions that cannot be completed are logged to the log file.

#### Next steps
Turning the converted into an interface was the next step. The classes that extend from the interface
would just need to define their input object, output object, and inputToOutput function. This would
open up the door to being able to work with many inputs and outputs and use collections of different converters
in classes that cared to do so. And additional next step would be to define the input and output objects
in a more readable language like google protobuf, that could be more easily reviewed and looked at by non
technical people. This would definitely be out of the 6 hour scope I believe. Something that could have been
inside the scope is refining the failed row logger. Another method, `validate` could be added to the converter interface.
Currently, rows are only not converted if they cannot be converted, however, it would make sense to have
additional cases where the input rows should not make it to the output row. Missing fields are converted just
fine without error, but it would make sense to omit some of these from the output document. For example
an input row with an empty productId or productNumber may be converted just fine, but it might not make sense
to include these in the output document. This would be a very logical thing to include in the converter interface.

# Documentation of existing project
This is a simple CSV converter. It is meant to convert a csv file formatted like:

|Order Number|Year|Month|Day|Product Number|Product Name|Count|Extra Col1|Extra Col2|Empty Column|
|------------|----|-----|---|--------------|------------|-----|----------|----------|------------|
|1000|2018|1|1|P-10001|Arugola|"5,250.50"|Lorem|Ipsum|
|1001|2017|12|12|P-10002|Iceberg lettuce|500.00|Lorem,Ipsum|

To

|OrderID|OrderDate|ProductId|ProductName|Quantity|Unit|
|-------|---------|---------|-----------|--------|----|
|1000|2018;1;1|P-10001|Arugola|5250.50|kg|
|1001|2017;12;12|P-10002|"Iceberg lettuce"|500.00|kg|

## Usage
To run the project run:
`./gradlew build convertFileFileInput -PconfigurationFile="configurationFile.txt"`
The configuration file should have 3 lines:
input.csv
output.csv
log.txt
If the configuration file does not have these or if the input file does not exist, the
program will exit before trying any conversion.

If you want to run the program via the command line and specify the input, output, and log files
via the command line, you can run:
`./gradlew convertFile -PinputFile="path/to/input.csv" -PoutputFile="path/to/output.csv" -PlogFile="path/to/log.txt`

If you do not specify parameters, the defaults of orders.csv, output.csv, and log.txt will be used.