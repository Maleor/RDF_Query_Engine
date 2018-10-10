package Dico;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

public class HashMapDictionary {

	private TreeMap<String,Integer> URI_to_ID;
	private HashMap<Integer,String> ID_to_URI;
	
	public Integer getID(String URI) {
		return URI_to_ID.get(URI);
	}
	
	public String getURI(Integer ID) {
		return ID_to_URI.get(ID);
	}
	
	
	public HashMapDictionary(ArrayList<String> toMap) {
		
		URI_to_ID = new TreeMap<>();
		
		for(String string : toMap) 
			URI_to_ID.put(string, 0);
		
		int index = 0;
		for(String string : URI_to_ID.keySet()) {
			URI_to_ID.put(string, index);
			ID_to_URI.put(index, string);
			index++;
		}
	}
	
}
