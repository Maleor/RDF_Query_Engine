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

import Dico.ThreeValues_Index.INDEX_TYPE;

public class Requester {

	public static HashMapDictionary daDico;

	public static ArrayList<ArrayList<String>> tripleData;
	public static ArrayList<String> fullData;

	public static String stringFile;
	public static RDFRawParser parser;

	public static void initRequester() {
		parser = new RDFRawParser();
		tripleData = new ArrayList<>();
		fullData = new ArrayList<>();
		stringFile = "data/data_RDFXML/500K.rdfxml";
	}

	/*******************/
	/** MAIN FUNCTION **/
	/*******************/
	public static void main(String[] args) throws IOException {
		Instant t1;
		initRequester();

		tripleData = parser.parseFile(stringFile); /* creates a list of 3 lists (subjects, predicates and objects ) from the given file */

		for (int index = 0; index < tripleData.size(); index++)
			for (String string : tripleData.get(index))
				fullData.add(string); /* creates a list that contains all the URI */
		
		t1 = Instant.now();
		daDico = new HashMapDictionary(fullData); /* creates the dictionary from the list of URI */
		System.out.println("Creation du dictionnaire : " + Duration.between(t1, Instant.now()).toMillis());
		//daDico.showDico();
		
		ThreeValues_Index OPS = new ThreeValues_Index(daDico, tripleData, INDEX_TYPE.POS);
		t1 = Instant.now();
		OPS.IndexBuilder();
		System.out.println("Initialisation de l'index OPS : " + Duration.between(t1, Instant.now()).toMillis());
		OPS.showIndex();
	}
}
