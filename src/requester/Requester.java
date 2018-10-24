package requester;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;

import javax.management.Query;

import dataParser.RDFRawParser;
import dictionary.Dictionary;
import index.SingleURI_Selectivity;
import index.ThreeURI_Index;
import index.ThreeURI_Index.INDEX_TYPE;
import query.QueryHandler;
import query.QuerySet;

/**
 * 
 * @author Mathieu Dodard
 * @author Quentin Monod
 *
 */
public class Requester {

	public Dictionary dictionary;

	public ArrayList<ArrayList<String>> tripleData;
	public HashSet<String> fullData;

	public String dataFile;
	public String queryFile;
	public String outputPath;

	public int numberOfTriples;

	public RDFRawParser parser;

	public ThreeURI_Index POS;
	public ThreeURI_Index OPS;

	public SingleURI_Selectivity o_frequency;
	public SingleURI_Selectivity p_frequency;

	public QuerySet querySet;
	
	public QueryHandler queryHandler;
	
	public boolean verbose;
	public boolean export_results;
	public boolean export_stats;
	public boolean workload_time;

	///////////////////
	/** CONSTRUCTOR **/
	///////////////////

	public Requester(String[] args) {
		parser = new RDFRawParser();
		tripleData = new ArrayList<>();
		fullData = new HashSet<>();
		dataFile = args[1];
		queryFile = args[0];
		outputPath = args[2];
		verbose = args[3].equals("1") ? true : false; 
		export_results = args[4].equals("1") ? true : false;
		export_stats = args[5].equals("1") ? true : false;
		workload_time = args[6].equals("1") ? true : false;
	}

	/**
	 * Initializes the structures that contain the data
	 * 
	 * @throws FileNotFoundException
	 */
	private void initData() throws FileNotFoundException {
		tripleData = parser.parseFile(dataFile); /*
													 * creates a list of 3 lists (subjects, predicates and objects )
													 * from the given file
													 */
		numberOfTriples = tripleData.get(0).size();

		for (int index = 0; index < tripleData.size(); index++)
			for (String string : tripleData.get(index))
				fullData.add(string); /* creates a list that contains all the URI */
	}

	//////////////////////
	/** PUBLIC METHODS **/
	//////////////////////

	/**
	 * Initializes and executes the evaluation of the queries
	 * 
	 * @throws IOException 
	 * 
	 */
	public void run() throws IOException{
		
		Instant t2 = Instant.now();
		
		Instant t1 = Instant.now();
		initData();
		if(verbose) System.out.println("Initialization of the data ---------> " + Duration.between(t1, Instant.now()).toMillis() + " ms");
		
		t1 = Instant.now();
		dictionary = new Dictionary(fullData);	
		dictionary.showDico(outputPath);
		if(verbose) System.out.println("Initialization of the dictionary ---> " + Duration.between(t1, Instant.now()).toMillis() + " ms");
		
		t1 = Instant.now();
		initIndexes();
		if(verbose) System.out.println("Initialization of the indexes ------> " + Duration.between(t1, Instant.now()).toMillis() + " ms");
		
		t1 = Instant.now();
		initQuerySet();	
		if(verbose) System.out.println("Initialization of the query set ----> " + Duration.between(t1, Instant.now()).toMillis() + " ms");
				
		t1 = Instant.now();
		for(int index = 0 ; index < querySet.getSize() ; index++) 
			queryHandler.getSolutions(querySet.get(index));	
		if(verbose) System.out.println("Time to find solutions -------------> " + Duration.between(t1 , Instant.now()).toMillis() + " ms");
		
		if(verbose) System.out.println("Total execution time ---------------> " + Duration.between(t2, Instant.now()).toMillis() + " ms");
		
		querySet.showResultsAsURI(outputPath);
	}

	public void initQuerySet() throws FileNotFoundException {
	
		
		querySet = new QuerySet(dictionary);
		querySet.ParseQueryFile(queryFile);
		queryHandler = new QueryHandler(POS, OPS, o_frequency, p_frequency);	
	}
	
	public void initIndexes() throws IOException {

		OPS = new ThreeURI_Index(dictionary, tripleData, INDEX_TYPE.OPS);
		OPS.IndexBuilder();
		OPS.showIndex(outputPath);

		POS = new ThreeURI_Index(dictionary, tripleData, INDEX_TYPE.POS);
		POS.IndexBuilder();
		POS.showIndex(outputPath);

		o_frequency = new SingleURI_Selectivity(OPS);
		o_frequency.ComputeSelectivity(numberOfTriples);
		o_frequency.Show_URI_Selectivity("o", outputPath);

		p_frequency = new SingleURI_Selectivity(POS);
		p_frequency.ComputeSelectivity(numberOfTriples);
	}
}
