package jenaTest;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;

import dictionary.Dictionary;
import query.Query;
import query.QuerySet;

/**
 * 
 * @author Mathieu Dodard
 * @author Quentin Monod
 *
 */
public class CompareJena {

	public double timeForJena;
	Dictionary dico;
	ArrayList<HashSet<Integer>> jenaRes;

	///////////////////
	/** CONSTRUCTOR **/
	///////////////////

	public CompareJena(Dictionary dico) {
		jenaRes = new ArrayList<>();
		this.dico = dico;
		timeForJena = 0.0;
	}

	///////////////////////
	/** PRIVATE METHODS **/
	///////////////////////

	/**
	 * Compares the results of the 2 systems
	 * 
	 * @param jena
	 *            The list of solutions returned by Jena
	 * 
	 * @param ours
	 *            The list of solutions returned by the system
	 * 
	 * @return true if they are the same, false otherwise
	 */
	private boolean compareLists(QuerySet queries) {

		boolean res = true;
		
		for(int index = 0 ; index < queries.getSize() ; index++) {		
			HashSet<Integer> sysRes = new HashSet<>();
			sysRes.addAll(queries.get(index).answer);
			
			if(!sysRes.equals(jenaRes.get(index)))
				res = false;
		}
		return res;
	}

	/**
	 * Executes Jena with the given set of queries
	 * 
	 * @param queryS
	 *            The queries you want to evaluate
	 * 
	 * @param data
	 *            The path to the file that contains the data

	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	private void execJena(ArrayList<String> queries, String data)
			throws FileNotFoundException, UnsupportedEncodingException {

		Model model = ModelFactory.createDefaultModel();

		String pathToOntology = data;

		InputStream in = FileManager.get().open(pathToOntology);

		model.read(in, null);

		 

		for (String q : queries) {
			
			com.hp.hpl.jena.query.Query query = QueryFactory.create(q);

			QueryExecution qexec = QueryExecutionFactory.create(query, model);

			double beg = System.currentTimeMillis();

			com.hp.hpl.jena.query.ResultSet rs = qexec.execSelect();

			timeForJena = timeForJena + (System.currentTimeMillis() - beg);

			jenaRes.add(new HashSet<>());
			while (rs.hasNext()) {
				QuerySolution qs = rs.next();
				jenaRes.get(jenaRes.size()-1).add(dico.getID(qs.get("v0").toString()));
			}
		}
	}

	//////////////////////
	/** PUBLIC METHODS **/
	//////////////////////

	/**
	 * Compares your results with Jena's
	 * 
	 * @param queries
	 *            The queries you want to evaluate
	 * 
	 * @param data
	 *            The path to the file that contains the data
	 * 
	 * @return true if the results are the same, false otherwise
	 * 
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	public boolean compare(QuerySet queries, String data)
			throws FileNotFoundException, UnsupportedEncodingException {

		boolean res = true;
		
		ArrayList<String> strQ = new ArrayList<>();
		
		for(Query q : queries.querySet) {
			strQ.add(q.showQuery());
		}

		execJena(strQ, data);

		
		res = compareLists(queries);

		return res;
	}

}
