package Query;

import java.util.TreeSet;

import Index.SingleURI_Selectivity;
import Index.ThreeURI_Index;

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
	 * @param index
	 *            The index that contains the data
	 * 
	 * @param cond
	 *            The condition to respect
	 * 
	 * @return The solution set
	 */
	private TreeSet<Integer> getSingleConditionSolutions(ThreeURI_Index index, Integer[] cond) {
		return index.index.get(cond[0]).get(cond[1]);
	}

	//////////////////////
	/** PUBLIC METHODS **/
	//////////////////////

	public void getSolutions(Query query) {		
		TreeSet<Integer> tmpSol = new TreeSet<>();
		for(int index = 0 ; index < query.conditions.size() ; index++) {
			tmpSol = getSingleConditionSolutions(POS, query.conditions.get(index));   /* TOOOOOO DOOOOOOOOOO */
			for(Integer i : tmpSol) query.answer.add(i);
		}
	}
}
