package Dico;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

import Dico.ThreeURI_Index.INDEX_TYPE;

/**
 * 
 * @author Mathieu Dodard
 * @author Quentin Monod
 *
 */
public class Requester {

	private static HashMapDictionary daDico;

	private static ArrayList<ArrayList<String>> tripleData;
	private static ArrayList<String> fullData;

	private static String stringFile;

	private static int numberOfTriples;

	private static RDFRawParser parser;

	private static ThreeURI_Index POS;
	private static ThreeURI_Index OPS;

	private static SingleURI_Selectivity o_frequency;
	private static SingleURI_Selectivity p_frequency;

	///////////////////////
	/** PRIVATE METHODS **/
	///////////////////////
	
	/**
	 * Initializes the structures used in the requester
	 */
	private static void initRequester() {
		parser = new RDFRawParser();
		tripleData = new ArrayList<>();
		fullData = new ArrayList<>();
		stringFile = "data/data_RDFXML/500K.rdfxml";
	}

	/**
	 * Initializes the structures that contain the data
	 * 
	 * @throws FileNotFoundException
	 */
	private static void initData() throws FileNotFoundException {
		tripleData = parser.parseFile(stringFile); /*
													 * creates a list of 3 lists (subjects, predicates and objects )
													 * from the given file
													 */
		numberOfTriples = tripleData.get(0).size();

		for (int index = 0; index < tripleData.size(); index++)
			for (String string : tripleData.get(index))
				fullData.add(string); /* creates a list that contains all the URI */
	}

	/////////////////////
	/** MAIN FUNCTION **/
	/////////////////////
	public static void main(String[] args) throws IOException {
		Instant t1;
		initRequester();
		initData();

		t1 = Instant.now();
		daDico = new HashMapDictionary(fullData); /* creates the dictionary from the list of URI */
		System.out.println("Creation of the dictionary : " + Duration.between(t1, Instant.now()).toMillis() + " ms\n");
		System.out.println("Size of the dictionary : " + daDico.getSize());
		daDico.showDico();

		OPS = new ThreeURI_Index(daDico, tripleData, INDEX_TYPE.OPS);
		t1 = Instant.now();
		OPS.IndexBuilder();
		System.out.println(
				"Initialization of the OPS index : " + Duration.between(t1, Instant.now()).toMillis() + " ms\n");
		OPS.showIndex();

		o_frequency = new SingleURI_Selectivity(OPS);
		t1 = Instant.now();
		o_frequency.ComputeSelectivity(numberOfTriples);
		System.out.println("Initialization of the objects frequency index : "
				+ Duration.between(t1, Instant.now()).toMillis() + " ms\n");
		o_frequency.Show_URI_Selectivity("objects");

		POS = new ThreeURI_Index(daDico, tripleData, INDEX_TYPE.POS);
		t1 = Instant.now();
		POS.IndexBuilder();
		System.out.println(
				"Initialization of the POS index : " + Duration.between(t1, Instant.now()).toMillis() + " ms\n");
		POS.showIndex();

		p_frequency = new SingleURI_Selectivity(POS);
		t1 = Instant.now();
		p_frequency.ComputeSelectivity(numberOfTriples);
		System.out.println("Initialization of the predicates frequency index : "
				+ Duration.between(t1, Instant.now()).toMillis() + " ms\n");
		p_frequency.Show_URI_Selectivity("predicates");
	}
}
