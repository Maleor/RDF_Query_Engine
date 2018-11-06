package query;

import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;

import index.SingleURI_Selectivity;
import index.ThreeURI_Index;
import index.ThreeURI_Index.INDEX_TYPE;

/**
 * 
 * @author Mathieu Dodard
 * @author Quentin Monod
 *
 */
public class QueryHandler {

	private ThreeURI_Index POS;
	private ThreeURI_Index OPS;

	public QueryHandler(ThreeURI_Index POS, ThreeURI_Index OPS) {
		this.POS = POS;
		this.OPS = OPS;
	}

	///////////////////////
	/** PRIVATE METHODS **/
	///////////////////////

	/**
	 * Looks for the triples that respect a single P O condition
	 * 
	 * @param index The index that contains the data
	 * 
	 * @param cond  The condition to respect
	 * 
	 * @return The solution set
	 */
	private HashSet<Integer> getSingleConditionSolutions(ThreeURI_Index index, Integer[] cond) {
		HashSet<Integer> solutions = new HashSet<>();

		if (index.type.equals(INDEX_TYPE.POS))
			if ((index.get(cond[0]) != null) && index.get(cond[0]).get(cond[1]) != null)
				solutions.addAll(index.get(cond[0]).get(cond[1]));

		if (index.type.equals(INDEX_TYPE.OPS))
			if ((index.get(cond[1]) != null) && index.get(cond[1]).get(cond[0]) != null)
				solutions.addAll(index.get(cond[1]).get(cond[0]));

		return solutions;
	}

	//////////////////////
	/** PUBLIC METHODS **/
	//////////////////////

	/**
	 * Finds all the solutions of a specific query using its conditions
	 * 
	 * @param query The query you want to evaluate
	 */
	public void getSolutions(Query query) {

		HashSet<Integer> merged = new HashSet<>();
		HashSet<Integer> toMerge = new HashSet<>();

		if(query.usedIndex.get(0).equals("unexistant"))
			return;
		
		if(query.usedIndex.get(0).equals("POS"))
			merged = getSingleConditionSolutions(POS, query.conditions.get(0));
		else 
			merged = getSingleConditionSolutions(OPS, query.conditions.get(0));

		if (query.conditions.size() > 1 && !merged.isEmpty()) {

			for (int i = 1; i < query.conditions.size(); i++) {

				if(query.usedIndex.get(i).equals("unexistant")) {
					merged = new HashSet<>();
					break;
				}
				
				if(query.usedIndex.get(i).equals("POS"))
					toMerge = getSingleConditionSolutions(POS, query.conditions.get(i));
				else 
					toMerge = getSingleConditionSolutions(OPS, query.conditions.get(i));

				merged.retainAll(toMerge);

				if (merged.isEmpty())
					break;
			}
		}
		query.answer.addAll(merged);
	}
}
