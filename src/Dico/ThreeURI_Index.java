package Dico;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.TreeSet;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

/**
 * 
 * @author Mathieu Dodard
 * @author Quentin Monod
 *
 */
public class ThreeURI_Index {

	public TreeMap<Integer, TreeMap<Integer, TreeSet<Integer>>> index;
	
	private ArrayList<ArrayList<String>> data;
	
	private HashMapDictionary dico;

	public enum INDEX_TYPE {
		POS, OPS, SPO
	};

	private INDEX_TYPE type;

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
	public ThreeURI_Index(HashMapDictionary dico, ArrayList<ArrayList<String>> data, INDEX_TYPE type) {
		index = new TreeMap<>();
		this.data = data;
		this.type = type;
		this.dico = dico;
	}

	/**
	 * Builds the index using the data you gave in the copnstructor
	 */
	public void IndexBuilder() {

		int nbTriple = 0;
		/*
		 * to create an index we use a loop on the number of triples
		 * for each triple we take the subject,the predicate and the object
		 * we translate them in integer format with the dictionary
		 * we put them in the index changing the order into a specific one (like o p s)
		 */
		switch (type) {

		case POS:
			for (int jndex = 0; jndex < data.get(0).size(); jndex++) { 
				Integer obj = dico.getID(data.get(1).get(jndex));
				Integer pred = dico.getID(data.get(0).get(jndex));
				Integer subj = dico.getID(data.get(2).get(jndex));
				index.putIfAbsent(obj, new TreeMap<>());
				TreeMap<Integer, TreeSet<Integer>> objMap = index.get(obj);
				objMap.putIfAbsent(pred, new TreeSet<>());
				objMap.get(pred).add(subj);
				nbTriple++;
			}
			break;

		case OPS:
			for (int jndex = 0; jndex < data.get(0).size(); jndex++) {
				Integer obj = dico.getID(data.get(0).get(jndex));
				Integer pred = dico.getID(data.get(1).get(jndex));
				Integer subj = dico.getID(data.get(2).get(jndex));
				index.putIfAbsent(obj, new TreeMap<>());
				TreeMap<Integer, TreeSet<Integer>> objMap = index.get(obj);
				objMap.putIfAbsent(pred, new TreeSet<>());
				objMap.get(pred).add(subj);
				nbTriple++;
			}

			break;

		default:
			break;

		}
		System.out.println("Number of triples in the " + type.toString() + " index : " + nbTriple);
	}

	/**
	 * Shows all the triples that are contained in the index
	 * 
	 * @throws IOException
	 */
	public void showIndex() throws IOException {

		FileWriter findex = new FileWriter("data/Index" + type.toString());

		for (Integer s_key : index.keySet())
			for (Integer p_key : index.get(s_key).keySet())
				for (Integer obj : index.get(s_key).get(p_key))
					findex.write(s_key + " : " + p_key + " : " + obj + "\n");

		findex.close();
	}
}
