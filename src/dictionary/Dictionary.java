package dictionary;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;

/**
 * 
 * @author Mathieu Dodard
 * @author Quentin Monod
 *
 */
public class Dictionary {

	private TreeMap<String, Integer> URI_to_ID;
	private HashMap<Integer, String> ID_to_URI;

	///////////////////
	/** CONSTRUCTOR **/
	///////////////////

	public Dictionary(HashSet<String> toMap) {

		URI_to_ID = new TreeMap<>();
		ID_to_URI = new HashMap<>();

		for (String string : toMap) 
			URI_to_ID.put(string, 0);
		
		int index = 0;
		for (String string : URI_to_ID.keySet()) {
			URI_to_ID.put(string, index);
			ID_to_URI.put(index, string);
			index++;
		}
	}

	//////////////////////
	/** PUBLIC METHODS **/
	//////////////////////

	public Integer getID(String URI) {
		return URI_to_ID.get(URI);
	}

	public String getURI(Integer ID) {
		return ID_to_URI.get(ID);
	}

	public int getSize() {
		return URI_to_ID.size();
	}

	/**
	 * Shows all the translations that the dictionary contains
	 * 
	 * @param outputPath
	 *            The folder that contains the file in which you want to write
	 * 
	 * @throws IOException
	 */
	public void showDico(String outputPath) throws IOException {
		FileWriter writer = new FileWriter(outputPath + "/theDico");

		for (String string : URI_to_ID.keySet()) 
			writer.write(string + " --> " + URI_to_ID.get(string) + "\n");
		
		writer.close();
	}

}
