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

		File files = new File(file);

		File[] fi = files.listFiles();
		
		Scanner scanner;
		
		for (File f : fi) {

			scanner = new Scanner(new File(f.getPath()));

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

		FileWriter fw = new FileWriter(outputPath + "/resultsAsInteger.csv");
		
		
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

		FileWriter fw = new FileWriter(outputPath + "/resultsAsURI.csv");

		for (Query query : querySet) {
			fw.write(query.showQuery() + "\n");
			fw.write(query.showResultAsURI() + "\n");
		}

		fw.close();
	}
	
	public void showStats(String outputPath) throws IOException {
		FileWriter fw = new FileWriter(outputPath + "/queryStats.csv");
		
		fw.write("The selectivity is a valu between 0 and 1, the bigger it is, the smaller is the set of solutions.\n");
		fw.write("The selectivity estimation is equal to 1 - (the appearance frequency of the couple P O from the first condition).\n");
		fw.write("If the estimation is equal to 1, then the set of solution is empty.\n\n");
		
		
		for (Query query : querySet) {
			fw.write(query.showQuery() + "\n");
			fw.write(query.showQueryStats() + "\n");
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
