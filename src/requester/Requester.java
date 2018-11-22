package requester;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import dataParser.RDFRawParser;
import dictionary.Dictionary;
import index.SingleURI_Selectivity;
import index.ThreeURI_Index;
import index.ThreeURI_Index.INDEX_TYPE;
import jenaTest.CompareJena;
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
	public boolean jena;

	FileWriter fw;

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
		jena = args[7].equals("1") ? true : false;
	}

	///////////////////////
	/** PRIVATE METHODS **/
	///////////////////////

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

	/**
	 * Initializes the set of query, first it parses the files, then it sorts the
	 * conditions and finally builds a query handler.
	 * 
	 * @throws FileNotFoundException
	 */
	private void initQuerySet() throws FileNotFoundException {

		if (verbose)
			System.out.print("--- Parsing of the queries files ---> ");

		querySet = new QuerySet(dictionary);
		Instant beg = Instant.now();
		querySet.ParseQueryFile(queryFile);
		Instant end = Instant.now();

		if (verbose)
			System.out.println(Duration.between(beg, end).toMillis() + " ms");

		try {
			fw.write("Parsing of the queries files," + Duration.between(beg, end).toMillis() + " ms\n");
		} catch (IOException e) {
		}

		if (verbose)
			System.out.print("--- Optimization of the queries ----> ");

		beg = Instant.now();

		for (int index = 0; index < querySet.getSize(); index++) {
			querySet.get(index).orderConditions(POS, numberOfTriples);
			querySet.get(index).chooseIndex(p_frequency, o_frequency);
		}

		end = Instant.now();

		if (verbose)
			System.out.println(Duration.between(beg, end).toMillis() + " ms");

		try {
			fw.write("Optimization of the queries," + Duration.between(beg, end).toMillis() + " ms\n");
		} catch (IOException e) {
		}

		queryHandler = new QueryHandler(POS, OPS);
		Collections.shuffle(querySet.querySet);
	}

	/**
	 * Initializes all the indexes
	 * 
	 * @throws IOException
	 */
	private void initIndexes() throws IOException {

		OPS = new ThreeURI_Index(dictionary, tripleData, INDEX_TYPE.OPS);
		OPS.IndexBuilder();

		POS = new ThreeURI_Index(dictionary, tripleData, INDEX_TYPE.POS);
		POS.IndexBuilder();

		o_frequency = new SingleURI_Selectivity(OPS);
		o_frequency.ComputeSelectivity(numberOfTriples);

		p_frequency = new SingleURI_Selectivity(POS);
		p_frequency.ComputeSelectivity(numberOfTriples);
	}

	/**
	 * Exports the results and the stats if it is asked in the arguments
	 * 
	 * @throws IOException
	 */
	private void export() throws IOException {

		if (export_results) {
			querySet.showResultsAsURIInCSV(outputPath);
			querySet.showResultsAsIntegerInCSV(outputPath);
		}

		if (export_stats)
			querySet.showStats(outputPath);
	}

	/**
	 * Evaluates a random set of query with 10% of the queries contained in the0
	 */
	private void evaluationWarmer() {

		QuerySet qs = new QuerySet(dictionary);

		for (int index = 0; index < (int) (querySet.querySet.size() / 10); index++)
			qs.querySet.add(querySet.querySet.get(index));

		for (int jndex = 0; jndex < qs.querySet.size(); jndex++)
			if (querySet.get(jndex).toBeEvaluated)
				queryHandler.getSolutions(qs.get(jndex));

	}

	private void systemWarmer() {
		evaluationWarmer();
		Collections.shuffle(querySet.querySet);
	}

	private void compareWithJena() throws FileNotFoundException, UnsupportedEncodingException {

		NumberFormat format = new DecimalFormat("#0.0000");

		if (verbose)
			System.out.println("\nComparing with Jena");

		CompareJena cp = new CompareJena(dictionary);

		boolean same = true;

		if (!cp.compare(querySet, dataFile)) {
			same = false;
		}

		if (verbose) {
			System.out.println("--- Time for Jena to evaluate the queries : " + cp.timeForJena + " ms");
			System.out.println(
					"--- Which is approximately " + format.format(cp.timeForJena / numberOfTriples) + " ms per query");

			if (same)
				System.out.println("--- This system and Jena return the same results");
			else
				System.out.println("--- This system and Jena do not return the same results");
		}
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
	public void run() throws IOException {

		Instant beginExec = Instant.now();
		Instant endExec;
		Instant t1 = Instant.now();
		Instant t1fin;

		fw = new FileWriter(outputPath + "/executionStats.csv");

		/*************** Init data ***************/

		if (verbose)
			System.out.print("Initialization of the data ---------> ");

		initData();

		t1fin = Instant.now();

		if (verbose)
			System.out.println(Duration.between(t1, t1fin).toMillis() + " ms");

		fw.write("Initialization of the data," + Duration.between(t1, t1fin).toMillis() + " ms\n");

		/*************** Init dico ***************/

		t1 = Instant.now();

		if (verbose)
			System.out.print("Initialization of the dictionary ---> ");

		dictionary = new Dictionary(fullData);

		t1fin = Instant.now();

		if (verbose)
			System.out.println(Duration.between(t1, t1fin).toMillis() + " ms");

		fw.write("Initialization of the dictionary," + Duration.between(t1, t1fin).toMillis() + " ms\n");

		/*************** Init indexes ***************/

		t1 = Instant.now();

		if (verbose)
			System.out.print("Initialization of the indexes ------> ");

		initIndexes();
		t1fin = Instant.now();

		if (verbose)
			System.out.println(Duration.between(t1, t1fin = Instant.now()).toMillis() + " ms");

		fw.write("Initialization of the indexes," + Duration.between(t1, t1fin).toMillis() + " ms\n");

		/*************** Init query set ***************/

		t1 = Instant.now();

		if (verbose)
			System.out.println("Initialization of the query set");

		initQuerySet();

		/*************** Evaluation of the queries ***************/

		t1 = Instant.now();
		systemWarmer();
		t1fin = Instant.now();

		t1 = Instant.now();

		if (verbose)
			System.out.print("\nEvaluation of the queries");

		for (int index = 0; index < querySet.getSize(); index++) {
			if (querySet.get(index).toBeEvaluated) {
				queryHandler.getSolutions(querySet.get(index));
			}
		}

		t1fin = Instant.now();

		querySet.computeEvaluationTime(Duration.between(t1, t1fin).toMillis());

		if (workload_time)
			System.out.println("  ---------->" + Duration.between(t1, t1fin).toMillis() + " ms");

		fw.write("Evaluation of the queries," + Duration.between(t1, t1fin).toMillis() + " ms\n");

		endExec = Instant.now();

		if (jena)
			compareWithJena();

		if (verbose)
			System.out.println("\nTotal execution time ---------------> "
					+ Duration.between(beginExec, endExec).toMillis() + " ms");

		fw.write("Total execution time," + Duration.between(beginExec, endExec).toMillis() + " ms\n");

		fw.close();

		export();
	}
}
