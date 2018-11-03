package query;

import java.util.ArrayList;
import java.util.HashMap;

import dictionary.Dictionary;
import index.ThreeURI_Index;

/**
 * 
 * @author Mathieu Dodard
 * @author Quentin Monod
 *
 */
public class Query {

	public ArrayList<Integer> answer;
	public ArrayList<Integer[]> conditions;

	public double evaluationTime;

	public boolean toBeEvaluated;

	private Dictionary dico;

	private double estimatedSelectivity;
	private double parsingTime;

	private int estimatedNumberOfResults;

	///////////////////
	/** CONSTRUCTOR **/
	///////////////////

	public Query(StringBuilder query, Dictionary dico) {
		toBeEvaluated = true;
		conditions = new ArrayList<Integer[]>();
		answer = new ArrayList<>();
		this.dico = dico;
		initConditions(query.toString());
	}

	///////////////////////
	/** PRIVATE METHODS **/
	///////////////////////

	/**
	 * Gets the collection of conditions from a sparql query
	 * 
	 * @param query The sparql query from which you want to get the condtions
	 */
	private void initConditions(String query) {

		String[] tmpWords = query.split(" |\t");

		boolean accolade = false;
		int nbConditions = 0;

		double begin = System.nanoTime();

		for (int index = 0; index < tmpWords.length; index++) {
			if (tmpWords[index].equals("{"))
				accolade = true;

			if ((tmpWords[index].contains("?")) && (accolade)) {
				nbConditions++;
				conditions.add(new Integer[3]);
				String cnd1 = tmpWords[index + 1].substring(1, tmpWords[index + 1].length() - 1);
				String cnd2 = tmpWords[index + 2].substring(1, tmpWords[index + 2].length() - 1);
				conditions.get(conditions.size() - 1)[0] = dico.getID(cnd1);
				conditions.get(conditions.size() - 1)[1] = dico.getID(cnd2);
				conditions.get(conditions.size() - 1)[2] = nbConditions;
			}
		}

		double end = System.nanoTime();

		parsingTime = end - begin;
	}

	//////////////////////
	/** PUBLIC METHODS **/
	//////////////////////

	/**
	 * Sorts the conditions so that the couple P O of the first condition has the
	 * lowest frequency in the data and the last condition contains the couple P O
	 * with the highest frequency
	 * 
	 * @param POS The index that contains the data
	 * 
	 * @param nbT The number of triples
	 */
	public void orderConditions(ThreeURI_Index POS, int nbT) {

		ArrayList<Integer[]> sortedCdt = new ArrayList<>();

		double toCompare;

		int rankToRemove = 0;
		int loop = conditions.size();

		for (int jndex = 0; jndex < loop; jndex++) {

			Double minFre = 100000.0;

			for (int kndex = 0; kndex < conditions.size(); kndex++) {

				if (POS.get(conditions.get(kndex)[0]).get(conditions.get(kndex)[1]) != null)
					toCompare = (double) (POS.get(conditions.get(kndex)[0]).get(conditions.get(kndex)[1])).size()
							/ (double) nbT;
				else {
					toBeEvaluated = false;
					toCompare = 0;
				}

				if (toCompare < minFre) {
					minFre = toCompare;
					rankToRemove = kndex;
				}
			}

			if (jndex == 0) {
				if (minFre != 100000) {
					estimatedSelectivity = 1.0 - minFre;
					double toto = nbT * minFre;
					estimatedNumberOfResults = (int) toto;
				} else {
					estimatedSelectivity = 1;
					estimatedNumberOfResults = 0;
				}
			}

			sortedCdt.add(new Integer[3]);
			sortedCdt.get(jndex)[0] = conditions.get(rankToRemove)[0];
			sortedCdt.get(jndex)[1] = conditions.get(rankToRemove)[1];
			sortedCdt.get(jndex)[2] = conditions.get(rankToRemove)[2];
			conditions.remove(conditions.get(rankToRemove));
		}
		conditions = sortedCdt;

	}

	/**
	 * Transforms the condition collection of the query into a full SPARQL query
	 * 
	 * @return the query as a String
	 */
	public String showQuery() {

		StringBuilder toShow = new StringBuilder();

		toShow.append("SELECT ?v0 WHERE {\n");

		for (int index = 0; index < conditions.size(); index++) {
			toShow.append("\t?v0 ");
			toShow.append("<" + dico.getURI(conditions.get(index)[0]) + "> ");
			toShow.append("<" + dico.getURI(conditions.get(index)[1]) + "> .\n");
		}
		toShow.append("}\n");
		return toShow.toString();
	}

	public String showResultAsInteger() {

		StringBuilder toShow = new StringBuilder();

		toShow.append("Results (" + answer.size() + ") :\n");

		for (Integer i : answer)
			toShow.append("-- " + i + "\n");

		toShow.append("\n\n --------------------------------------- \n\n");

		return toShow.toString();
	}

	public String showResultAsURI() {

		StringBuilder toShow = new StringBuilder();

		toShow.append("Results (" + answer.size() + ") :\n");

		for (Integer i : answer)
			toShow.append("-- " + dico.getURI(i) + "\n");

		toShow.append("\n\n --------------------------------------- \n\n");

		return toShow.toString();
	}

	public String showQueryStats() {
		StringBuilder toShow = new StringBuilder();

		toShow.append("Parsing time : " + parsingTime + " ns\n");
		toShow.append("Evaluation time : " + evaluationTime + " ns\n");
		toShow.append("Evaluated : " + toBeEvaluated + "\n");
		toShow.append("Estimated selectivity : " + estimatedSelectivity + "\n");
		toShow.append("Estimated number of results : " + estimatedNumberOfResults + "\n");

		if (toBeEvaluated)
			toShow.append("Number of results : " + answer.size() + "\n");

		toShow.append("Conditions order : ");
		if (toBeEvaluated) {
			toShow.append(conditions.get(0)[2]);
			if (conditions.size() > 1) {
				for (int index = 1; index < conditions.size(); index++) {
					toShow.append(" --> " + conditions.get(index)[2]);
				}
			}
		} else
			toShow.append("The query was not evaluated, no results expected.\n");

		toShow.append("\n\n --------------------------------------- \n\n");

		return toShow.toString();
	}

}
