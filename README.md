# RSQ Engine
Master's Degree in Computer Science project consisting in creating a RDF Star query engine.

This program is able to compare his own results with Jena using the Jena API.

## How to use it
To use the system : 
	java -jar [jar_name].jar
		-queries folder/containing/queries
		-data file/containing/data/in/rdfxml
		-output folder/where/exports/are/sent
		-verbose [optional] : Turns on the verbose mode
		-export_results [optional] : Exports the queries results in the output folder as CSV
		-export_stats [optional] : Exports the queries statistics in the output folder
		-workload_time [optional] : Shows the
		-jena [optional] : Compares your results with Jena's
