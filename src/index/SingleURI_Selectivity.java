package index;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

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
	 * @return The frequency of the given URI
	 */
	private Double computeFrequency(int key, int numberOfTriples) {
		return ((double) data.get(key).keySet().size() / (double) numberOfTriples);
	}

	//////////////////////
	/** PUBLIC METHODS **/
	//////////////////////

	/**
	 * Fills the hash map with the frequency of each URI
	 * 
	 * @param numberOfTriples
	 *            The number of triples in your data
	 */
	public void ComputeSelectivity(int numberOfTriples) {

		Double frequency = 0.0;

		for (int key : data.getKeySet()) {
			frequency = computeFrequency(key, numberOfTriples);
			uri_selectivity.put(key, frequency);

		}
	}
	
	public double get(Integer i) {
		Double ret = 0.0;
		
		return uri_selectivity.get(i) == null ? ret : uri_selectivity.get(i);
	}
	
	public int getSize() {
		return uri_selectivity.size();
	}
	
	public Set<Integer> getKeySet(){
		return uri_selectivity.keySet();
	}

	/**
	 * Shows the frequency of each URI
	 * 
	 * @param objectFrequency
	 *            The object of the index (objects or predicates)
	 * 
	 * @param outputPath
	 *            The folder that contains the file in which you want to write
	 * 
	 * @throws IOException
	 */
	public void Show_URI_Selectivity(String objectFrequency, String outputPath) throws IOException {

		FileWriter fw = new FileWriter(outputPath + "/" + objectFrequency + "Frequency");

		for (int key : uri_selectivity.keySet())
			fw.write(key + " -> " + uri_selectivity.get(key) + "\n");

		fw.close();
	}

}