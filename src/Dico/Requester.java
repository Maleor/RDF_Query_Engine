package Dico;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Requester {

	public static void main(String[] args) throws IOException {
		
		
//		File file = new File("data/watdiv-mini-projet/testsuite/dataset/100K.rdf");
//		BufferedReader br = new BufferedReader(new FileReader(file));
//		String line;
//		while ((line = br.readLine()) != null) {
//		   System.out.println(line);
//		}
//		br.close();
		
		RDFParser parser = new RDFParser();
		ArrayList<ArrayList<String>> tripleData = new ArrayList<>();
		ArrayList<String> fullData = new ArrayList<>();
		tripleData = parser.parseFile("data/watdiv-mini-projet/data_RDFXML/100K.rdfxml");
		
		for(int index = 0 ; index < tripleData.size() ; index++) {
			for(String string : tripleData.get(index)) {
				fullData.add(string);
			}
		}
		
		HashMapDictionary daDico = new HashMapDictionary(fullData);
		daDico.showDico();
	}
}
