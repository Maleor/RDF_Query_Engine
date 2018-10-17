package Requester;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

import DataParser.RDFRawParser;
import Dictionary.Dictionary;
import Index.SingleURI_Selectivity;
import Index.ThreeURI_Index;
import Index.ThreeURI_Index.INDEX_TYPE;
import Query.QuerySet;

/**
 * 
 * @author Mathieu Dodard
 * @author Quentin Monod
 *
 */
public class Requester {

	public Dictionary daDico;

	public ArrayList<ArrayList<String>> tripleData;
	public ArrayList<String> fullData;

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

	///////////////////
	/** CONSTRUCTOR **/
	///////////////////

	public Requester(String[] args) {
		parser = new RDFRawParser();
		tripleData = new ArrayList<>();
		fullData = new ArrayList<>();
		dataFile = args[1];
		queryFile = args[0];
		outputPath = args[2];
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
	 * Executes
	 * 
	 * @param args
	 * @throws IOException
	 */
	public void run() throws IOException {

		initData();

		daDico = new Dictionary(fullData);

		querySet = new QuerySet(daDico);

		querySet.ParseQueryFile(queryFile);

		querySet.showQuerySet(outputPath);

	}

	public void initIndexes() {

		OPS = new ThreeURI_Index(daDico, tripleData, INDEX_TYPE.OPS);
		OPS.IndexBuilder();

		POS = new ThreeURI_Index(daDico, tripleData, INDEX_TYPE.POS);
		POS.IndexBuilder();

		o_frequency = new SingleURI_Selectivity(OPS);
		o_frequency.ComputeSelectivity(numberOfTriples);

		p_frequency = new SingleURI_Selectivity(POS);
		p_frequency.ComputeSelectivity(numberOfTriples);
	}
}
