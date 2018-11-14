package query;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Random;
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

	public int nbrEmpty;

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
	
	public void computeEvaluationTime(double timeEv) {
		
		
		for(Query query : querySet) {
			if(!query.toBeEvaluated) {
				query.evaluationTime = 0.0;
				nbrEmpty++;
			}
		}
		
		for(Query query : querySet) {
			if(query.toBeEvaluated)
				query.evaluationTime = timeEv/(double)(querySet.size() - nbrEmpty);
		}
	}


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

			boolean accolade = true;

			scanner = new Scanner(new File(f.getPath()));

			StringBuilder stringB = new StringBuilder();

			while (scanner.hasNextLine()) {

				String line = scanner.nextLine();

				if ((line.contains("select"))
						|| (line.contains("SELECT"))) { /* if a line starts with 'select' then we create a new query */
					if (!accolade) {
						querySet.add(new Query(stringB, dico));
					}
					accolade = false;
					stringB = new StringBuilder();
				}

				stringB.append(line);

				if (line.contains("}")) {
					accolade = true;
					querySet.add(new Query(stringB, dico));
					stringB = new StringBuilder();
				}
			}
			if (stringB.length() > 6)
				querySet.add(new Query(stringB, dico));
		}
	}
	
	/**
	 * Randomizes the queries contained into the queries set.
	 * This method should not be used.
	 * 
	 * @param qs
	 */
	@Deprecated
	public void randomizeQuerySet(QuerySet qs) {
		int size = qs.querySet.size();
		int random;
		Random rand = new Random();
		ArrayList<Query> finalList = new ArrayList<>();
		
		for(int index = 0 ; index < size ; index++) {
			random = rand.nextInt(qs.querySet.size());
			finalList.add(qs.querySet.get(random));
			qs.querySet.remove(random);
		}
		
		qs.querySet = finalList;
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

		FileWriter fw = new FileWriter(outputPath + "/resultsAsInteger_text.txt");

		for (Query query : querySet) {
			fw.write(query.showQuery() + "\n");
			fw.write(query.showResultAsInteger() + "\n");
		}

		fw.close();
	}

	public void showResultsAsURIInCSV(String outputPath) throws IOException {

		FileWriter fw = new FileWriter(outputPath + "/resultsAsURI.csv");

		for (Query query : querySet) {
			fw.write(query.showQueryOnSingleLine());
			fw.write(query.showResultAsURIInCSV() + "\n");
		}

		fw.close();
	}

	public void showResultsAsIntegerInCSV(String outputPath) throws IOException {

		FileWriter fw = new FileWriter(outputPath + "/resultsAsInteger.csv");

		for (Query query : querySet) {
			fw.write(query.showQueryOnSingleLine());
			fw.write(query.showResultAsIntegerInCSV() + "\n");
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
	public void showResultsAsURIInText(String outputPath) throws IOException {

		FileWriter fw = new FileWriter(outputPath + "/resultsAsURI_text.txt");

		for (Query query : querySet) {
			fw.write(query.showQuery() + "\n");
			fw.write(query.showResultAsURIInText() + "\n");
		}

		fw.close();
	}

	public void showStats(String outputPath) throws IOException {
		FileWriter fw = new FileWriter(outputPath + "/queryStats.txt");

		fw.write(
				"The selectivity is a value between 0 and 1, the bigger it is, the smaller is the set of solutions.\n");
		fw.write(
				"The selectivity estimation is equal to 1 - (the appearance frequency of the couple P O from the first condition).\n");
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
