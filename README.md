# CSV Converter
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
`./gradlew convertFile -PinputFile="path/to/input.csv" -PoutputFile="path/to/output.csv" -PlogFile="path/to/log.txt`

If you do not specify parameters, the defaults of orders.csv, output.csv, and log.txt will be used. Invalid lines will not be converted but will be logged as failed. All non failing lines should be converted
