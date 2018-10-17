package Dico;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * 
 * @author Mathieu Dodard
 * @author Quentin Monod
 *
 */
public class QuerySet {

	ArrayList<Query> querySet;
	Dictionary dico;

	public QuerySet(Dictionary dico) {
		this.dico = dico;
		querySet = new ArrayList<>();
	}

	public void ParseQueryFile(String file) throws FileNotFoundException {

		Scanner scanner = new Scanner(new File(file));

		StringBuilder stringB = new StringBuilder();

		while (scanner.hasNextLine()) {

			String line = scanner.nextLine(); // lecture de la ligne

			if ((line.contains("select")) || (line.contains("SELECT")))
				stringB = new StringBuilder();

			stringB.append(line);

			if (line.contains("}")) {
				querySet.add(new Query(stringB, dico));
			}
				

		}
	}

	public void showQuerySet() throws IOException {

		FileWriter fw = new FileWriter("data/querySet");

		for (Query query : querySet)
			fw.write(query.showQuery() + "\n");

		fw.close();
	}

}
