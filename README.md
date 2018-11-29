# RSQ Engine

Master's Degree in Computer Science project consisting in creating a RDF Star query engine.

This program is able to compare his own results with Jena using the Jena API.


# How to use it

To use the system : 

	java -jar RSQEngine.jar
		-queries folder/containing/queries
		-data file/containing/data/in/rdfxml
		-output folder/where/exports/are/sent
		-verbose [optional] : Turns on the verbose mode
		-export_results [optional] : Exports the queries results in the output folder as CSV
		-export_stats [optional] : Exports the queries statistics in the output folder
		-workload_time [optional] : Shows the time that was necessary to evaluate the queries
		-jena [optional] : Compares your results with Jena's

# The code

In the classes Query and QuerySet, some export methods are not used in the program but are still fully suitable.

Be careful, this system uses the hexastore approach but is only able to handle star queries around subjects because only two indexes are created : POS and OPS. If you want to handle star queries around predicates or objects, you will have to create the other indexes and also the subjects frequencies index. 
