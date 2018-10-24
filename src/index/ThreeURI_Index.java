package index;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

import dictionary.Dictionary;

/**
 * 
 * @author Mathieu Dodard
 * @author Quentin Monod
 *
 */
public class ThreeURI_Index {

	public HashMap<Integer, HashMap<Integer, HashSet<Integer>>> index;

	private ArrayList<ArrayList<String>> data;

	private Dictionary dico;

	private int nbTriple;

	public enum INDEX_TYPE {
		POS, OPS
	};

	public INDEX_TYPE type;

	/**
	 * Creates an empty index and initializes the data you need
	 * 
	 * @param dico
	 *            The dictionary to transform data into integer
	 * @param data
	 *            The data that contains all the data with string format
	 * @param type
	 *            The order of the URI in the index (POS, OPS, ...)
	 */
	public ThreeURI_Index(Dictionary dico, ArrayList<ArrayList<String>> data, INDEX_TYPE type) {
		index = new HashMap<>();
		this.data = data;
		this.type = type;
		this.dico = dico;
	}

	/**
	 * Builds the index using the data you gave in the constructor
	 */
	public void IndexBuilder() {

		nbTriple = 0;
		/*
		 * to create an index we use a loop on the number of triples ; for each triple we
		 * take the subject, the predicate and the object ; we translate them in integer
		 * format with the dictionary ; we put them in the index changing the order into a
		 * specific one (like o p s)
		 */
		switch (type) {

		case POS:
			for (int jndex = 0; jndex < data.get(0).size(); jndex++) {
				
				Integer first = dico.getID(data.get(1).get(jndex));
				Integer second = dico.getID(data.get(2).get(jndex));
				Integer third = dico.getID(data.get(0).get(jndex));
				
				index.putIfAbsent(first, new HashMap<>());
				HashMap<Integer, HashSet<Integer>> objMap = index.get(first);
				objMap.putIfAbsent(second, new HashSet<>());
				objMap.get(second).add(third);
				nbTriple++;
			}
			break;

		case OPS:
			for (int jndex = 0; jndex < data.get(0).size(); jndex++) {
				
				Integer first = dico.getID(data.get(2).get(jndex));
				Integer second = dico.getID(data.get(1).get(jndex));
				Integer third = dico.getID(data.get(0).get(jndex));
				
				index.putIfAbsent(first, new HashMap<>());
				HashMap<Integer, HashSet<Integer>> objMap = index.get(first);
				objMap.putIfAbsent(second, new HashSet<>());
				objMap.get(second).add(third);
				nbTriple++;
			}

			break;

		default:
			break;

		}
	}

	public HashMap<Integer, HashSet<Integer>> get(Integer i) {
		return index.get(i);
	}

	public int getSize() {
		return index.size();
	}

	public Set<Integer> getKeySet() {
		return index.keySet();
	}

	public int getNbTriple() {
		return nbTriple;
	}

	/**
	 * Shows all the triples that are contained in the index
	 * 
	 * @param outputPath
	 *            The folder that contains the file in which you want to write
	 * 
	 * @throws IOException
	 */
	public void showIndex(String outputPath) throws IOException {

		FileWriter findex = new FileWriter(outputPath + "/Index" + type.toString());

		for (Integer s_key : index.keySet())
			for (Integer p_key : index.get(s_key).keySet())
				for (Integer obj : index.get(s_key).get(p_key))
					findex.write(s_key + " : " + p_key + " : " + obj + "\n");

		findex.close();
	}
}
