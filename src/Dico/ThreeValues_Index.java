package Dico;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.TreeSet;import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

public class ThreeValues_Index {

	TreeMap<Integer, TreeMap<Integer, TreeSet<Integer>>> index;
	ArrayList<ArrayList<String>> data;
	HashMapDictionary dico;
	
	public enum INDEX_TYPE {POS, OPS, SPO};
	
	private INDEX_TYPE type;
	
	
	public ThreeValues_Index(HashMapDictionary dico, ArrayList<ArrayList<String>> data, INDEX_TYPE type) {
		index = new TreeMap<>();
		this.data = data;
		this.type = type;
		this.dico = dico;
	}
	
	public void IndexBuilder(){
		
		
		
		int nbTriple = 0;
		
		
		switch(type) {
		case POS :
			
			
			
			for(int jndex = 0 ; jndex < data.get(0).size() ; jndex++){
	            Integer obj = dico.getID(data.get(1).get(jndex));
	            Integer pred = dico.getID(data.get(0).get(jndex));
	            Integer subj = dico.getID(data.get(2).get(jndex));
	            index.putIfAbsent(obj, new TreeMap<>());
	            TreeMap<Integer,TreeSet<Integer>> objMap = index.get(obj);
	            objMap.putIfAbsent(pred, new TreeSet<>());
	            objMap.get(pred).add(subj);
	            nbTriple++;
			}
				
			
			break;
			
		case OPS :

			
			for(int jndex = 0 ; jndex < data.get(0).size() ; jndex++){
	            Integer obj = dico.getID(data.get(0).get(jndex));
	            Integer pred = dico.getID(data.get(1).get(jndex));
	            Integer subj = dico.getID(data.get(2).get(jndex));
	            index.putIfAbsent(obj, new TreeMap<>());
	            TreeMap<Integer,TreeSet<Integer>> objMap = index.get(obj);
	            objMap.putIfAbsent(pred, new TreeSet<>());
	            objMap.get(pred).add(subj);
	            nbTriple++;
			}
	            
			
		break;
		
		default:
			break;

		}
		System.out.println("Nombre de triplets : " + nbTriple);
	}
	
	public void showIndex() throws IOException {
		
		FileWriter findex = new FileWriter("data/Index"+type.toString());
		
		for(Integer s_key : index.keySet()) 
			for(Integer p_key : index.get(s_key).keySet()) 
				for(Integer obj : index.get(s_key).get(p_key)) 
					findex.write(s_key+" : " + p_key + " : " + obj+"\n");
		
		findex.close();
	}
}
