package Query;

import java.util.ArrayList;

import Dictionary.Dictionary;

/**
 * 
 * @author Mathieu Dodard
 * @author Quentin Monod
 *
 */
public class Query {

	ArrayList<Integer> answer;
	ArrayList<Integer[]> conditions;
	Dictionary dico;

	///////////////////
	/** CONSTRUCTOR **/
	///////////////////

	public Query(StringBuilder query, Dictionary dico) {
		conditions = new ArrayList<Integer[]>();
		this.dico = dico;
		initConditions(query.toString());
	}

	//////////////////////
	/** PRIVATE METHODS **/
	//////////////////////
	
	/**
	 * Gets the collection of conditions from a sparql query
	 * 
	 * @param query The sparql query from which you want to get the condtions
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

}
