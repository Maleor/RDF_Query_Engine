package Dico;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Requester {

	public static void main(String[] args) throws IOException {
		
		
		File file = new File("data/watdiv-mini-projet/testsuite/dataset/100K.rdf");
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line;
		while ((line = br.readLine()) != null) {
		   System.out.println(line);
		}
		br.close();
	}
}
