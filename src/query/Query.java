package query;

import java.util.ArrayList;

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
	public Dictionary dico;

	///////////////////
	/** CONSTRUCTOR **/
	///////////////////

	public Query(StringBuilder query, Dictionary dico) {
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
	 * @param query
	 *            The sparql query from which you want to get the condtions
	 */
	private void initConditions(String query) {

		String[] tmpWords = query.split(" |\t");

		boolean accolade = false;

		for (int index = 0; index < tmpWords.length; index++) {
			if (tmpWords[index].equals("{"))
				accolade = true;

			if ((tmpWords[index].contains("?")) && (accolade)) {
				conditions.add(new Integer[2]);
				String cnd1 = tmpWords[index + 1].substring(1, tmpWords[index + 1].length() - 1);
				String cnd2 = tmpWords[index + 2].substring(1, tmpWords[index + 2].length() - 1);
				conditions.get(conditions.size() - 1)[0] = dico.getID(cnd1);
				conditions.get(conditions.size() - 1)[1] = dico.getID(cnd2);
			}
		}
	}

	//////////////////////
	/** PUBLIC METHODS **/
	//////////////////////

	/**
	 * Sorts the conditions so that the couple P O of the first condition has the
	 * lowest frequency in the data and the last condition contains the couple P O
	 * with the highest frequency
	 * 
	 * @param POS
	 *            The index that contains the data
	 * 
	 * @param nbT
	 *            The number of triples
	 */
	public void orderConditions(ThreeURI_Index POS, int nbT) {

		ArrayList<Integer[]> sortedCdt = new ArrayList<>();

		int rankToRemove = 0;

		for (int jndex = 0; jndex < conditions.size(); jndex++) {
			Double minFre = 100000.0;

			for (int kndex = 0; kndex < conditions.size(); kndex++) {
				
				if (POS.get(conditions.get(kndex)[0]).get(conditions.get(kndex)[1]) != null) {
					
					double toCompare = (double) (POS.get(conditions.get(kndex)[0]).get(conditions.get(kndex)[1])).size()
							/ (double) nbT;
					
					if (toCompare < minFre) {
						
						minFre = toCompare;

						rankToRemove = kndex;
					}
				}
			}

			sortedCdt.add(conditions.get(rankToRemove));
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

		toShow.append("Results :\n");

		for (Integer i : answer)
			toShow.append("-- " + i + "\n");

		toShow.append("\n\n --------------------------------------- \n\n");

		return toShow.toString();
	}

	public String showResultAsURI() {

		StringBuilder toShow = new StringBuilder();

		toShow.append("Results :\n");

		for (Integer i : answer)
			toShow.append("-- " + dico.getURI(i) + "\n");

		toShow.append("\n\n --------------------------------------- \n\n");

		return toShow.toString();
	}

}
