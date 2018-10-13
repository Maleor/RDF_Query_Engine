package Dico;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

/**
 * 
 * @author Mathieu Dodard
 * @author Quentin Monod
 *
 */
public class SingleURI_Selectivity {

	public HashMap<Integer, Double> uri_selectivity;

	private ThreeURI_Index data;

	///////////////////
	/** CONSTRUCTOR **/
	///////////////////
	public SingleURI_Selectivity(ThreeURI_Index data) {
		this.uri_selectivity = new HashMap<>();
		this.data = data;
	}

	///////////////////////
	/** PRIVATE METHODS **/
	///////////////////////

	/**
	 * Finds the frequency of a specific URI
	 * 
	 * @param key
	 *            The URI for which you want to find the frequency
	 * 
	 * @param numberOfTriples
	 *            The number of triples in your data
	 * 
	 * @return The frequency
	 */
	private Double computeFrequency(int key, int numberOfTriples) {
		return ((double) data.index.get(key).keySet().size() / (double) numberOfTriples);
	}

	//////////////////////
	/** PUBLIC METHODS **/
	//////////////////////

	/**
	 * Fills the hashmap with the frequency of each URI
	 * 
	 * @param numberOfTriples
	 *            The number of triples in your data
	 */
	public void ComputeSelectivity(int numberOfTriples) {

		Double frequency = 0.0;

		for (int key : data.index.keySet()) {
			frequency = computeFrequency(key, numberOfTriples);
			uri_selectivity.put(key, frequency);

		}
	}

	/**
	 * Shows the frequency of each URI
	 * 
	 * @param objectFrequency The object of the index (objects or predicates)
	 * 
	 * @throws IOException
	 */
	public void Show_URI_Selectivity(String objectFrequency) throws IOException {

		FileWriter fw = new FileWriter("data/" + objectFrequency +"Frequency");

		for (int key : uri_selectivity.keySet()) {
			fw.write(key + " -> " + uri_selectivity.get(key) + "\n");
		}

		fw.close();
	}

}
