package query;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import dictionary.Dictionary;

/**
 * 
 * @author Mathieu Dodard
 * @author Quentin Monod
 *
 */
public class QuerySet {

	public ArrayList<Query> querySet;
	
	private Dictionary dico;

	///////////////////
	/** CONSTRUCTOR **/
	///////////////////

	public QuerySet(Dictionary dico) {
		this.dico = dico;
		querySet = new ArrayList<>();
	}

	//////////////////////
	/** PUBLIC METHODS **/
	//////////////////////

	/**
	 * 
	 * @param file
	 *            The file that contain all the queries that you want to evaluate
	 * 
	 * @throws FileNotFoundException
	 */
	public void ParseQueryFile(String file) throws FileNotFoundException {

		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(new File(file));

		StringBuilder stringB = new StringBuilder();

		while (scanner.hasNextLine()) {

			String line = scanner.nextLine();

			if ((line.contains("select"))
					|| (line.contains("SELECT"))) /* if a line starts with 'select' then we create a new query */
				stringB = new StringBuilder();

			stringB.append(line);

			if (line.contains("}")) {
				querySet.add(new Query(stringB, dico));
			}

		}
	}

	/**
	 * Shows all the queries that are contained in the query set
	 * 
	 * @param outputPath
	 *            The folder that contains the file in which you want to write
	 * 
	 * @throws IOException
	 */
	public void showQuerySet(String outputPath) throws IOException {

		FileWriter fw = new FileWriter(outputPath + "/querySet");

		for (Query query : querySet)
			fw.write(query.showQuery() + "\n");

		fw.close();
	}
	
	/**
	 * Shows the results as integer of each query in the query set
	 * 
	 * @param outputPath
	 *            The folder that contains the file in which you want to write
	 * 
	 * @throws IOException
	 */
	public void showResultsAsInteger(String outputPath) throws IOException {

		FileWriter fw = new FileWriter(outputPath + "/resultsAsInteger");

		for (Query query : querySet) {
			fw.write(query.showQuery() + "\n");
			fw.write(query.showResultAsInteger() + "\n");
		}

		fw.close();
	}
	
	/**
	 * Shows the results as URI of each query in the query set
	 * 
	 * @param outputPath
	 *            The folder that contains the file in which you want to write
	 * 
	 * @throws IOException
	 */
	public void showResultsAsURI(String outputPath) throws IOException {

		FileWriter fw = new FileWriter(outputPath + "/resultsAsURI");

		for (Query query : querySet) {
			fw.write(query.showQuery() + "\n");
			fw.write(query.showResultAsURI() + "\n");
		}

		fw.close();
	}
	
	public Query get(int index) {
		return querySet.get(index);
	}
	
	public int getSize() {
		return querySet.size();
	}

}
