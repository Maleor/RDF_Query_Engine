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

	public static void main(String[] args) throws IOException {

		RDFParser parser = new RDFParser();
		tripleData = new ArrayList<>();
		fullData = new ArrayList<>();
		stringFile = "data/watdiv-mini-projet/data_RDFXML/100K.rdfxml";

		tripleData = parser.parseFile(stringFile);

		for (int index = 0; index < tripleData.size(); index++)
			for (String string : tripleData.get(index))
				fullData.add(string);

		daDico = new HashMapDictionary(fullData);
		daDico.showDico();
	}
}
