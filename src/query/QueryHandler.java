package query;

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

	private SingleURI_Selectivity o_frequency;
	private SingleURI_Selectivity p_frequency;

	public QueryHandler(ThreeURI_Index POS, ThreeURI_Index OPS, SingleURI_Selectivity o_freq,
			SingleURI_Selectivity p_freq) {
		this.POS = POS;
		this.OPS = OPS;
		o_frequency = o_freq;
		p_frequency = p_freq;
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

		Double cond1_frequency = p_frequency.get(query.conditions.get(0)[0]);
		Double cond2_frequency = o_frequency.get(query.conditions.get(0)[1]);

		if (cond1_frequency == 0.0 || cond2_frequency == 0.0)
			return;

		if (cond1_frequency < cond2_frequency)
			merged = getSingleConditionSolutions(POS, query.conditions.get(0));
		else
			merged = getSingleConditionSolutions(OPS, query.conditions.get(0));

		if (query.conditions.size() > 1) {

			for (int i = 1; i < query.conditions.size(); i++) {

				cond1_frequency = p_frequency.get(query.conditions.get(i)[0]);
				cond2_frequency = o_frequency.get(query.conditions.get(i)[1]);

				if (cond1_frequency == 0.0 || cond2_frequency == 0.0) {
					merged = new HashSet<>();
					break;
				}

				if (cond1_frequency < cond2_frequency)
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
