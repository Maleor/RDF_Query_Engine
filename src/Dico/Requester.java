package Dico;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

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
		stringFile = "data/data_RDFXML/100K.rdfxml";
	}

	public static void main(String[] args) throws IOException {

		initRequester();

		tripleData = parser.parseFile(stringFile);

		for (int index = 0; index < tripleData.size(); index++)
			for (String string : tripleData.get(index))
				fullData.add(string);

		daDico = new HashMapDictionary(fullData);
		daDico.showDico();
	}
}
