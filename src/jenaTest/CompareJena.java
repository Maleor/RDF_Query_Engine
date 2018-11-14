package jenaTest;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;

/**
 * 
 * @author Mathieu Dodard
 * @author Quentin Monod
 *
 */
public class CompareJena {

	double timeForJena;

	///////////////////
	/** CONSTRUCTOR **/
	///////////////////

	public CompareJena() {
	}

	///////////////////////
	/** PRIVATE METHODS **/
	///////////////////////

	/**
	 * Compares 2 lists of solutions
	 * 
	 * @param jena
	 *            The list of solutions returned by Jena
	 * 
	 * @param ours
	 *            The list of solutions returned by the system
	 * 
	 * @return true if they are the same, false otherwise
	 */
	private boolean compareLists(ArrayList<String> jena, ArrayList<String> ours) {

		ArrayList<String> save = new ArrayList<>(jena);

		jena.retainAll(ours);

		return jena.equals(save);
	}

	/**
	 * Executes Jena with the given query
	 * 
	 * @param queryS
	 *            The query you want to evaluate
	 * 
	 * @param data
	 *            The path to the file that contains the data
	 * 
	 * @return The solutions of the query
	 * 
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	private ArrayList<String> execJena(String queryS, String data)
			throws FileNotFoundException, UnsupportedEncodingException {

		ArrayList<String> toReturn = new ArrayList<>();

		String q = queryS;

		Model model = ModelFactory.createDefaultModel();

		String pathToOntology = data;

		InputStream in = FileManager.get().open(pathToOntology);

		model.read(in, null);

		com.hp.hpl.jena.query.Query query = QueryFactory.create(q);

		QueryExecution qexec = QueryExecutionFactory.create(query, model);

		try {
			com.hp.hpl.jena.query.ResultSet rs = qexec.execSelect();

			// ResultSetFormatter.out(System.out, rs, query);

			while (rs.hasNext()) {
				QuerySolution qs = rs.next();
				toReturn.add(qs.get("v0").toString());
			}

		} finally {

			qexec.close();
		}

		return toReturn;
	}

	//////////////////////
	/** PUBLIC METHODS **/
	//////////////////////

	/**
	 * Compares your results of a query with the results given bu Jena
	 * 
	 * @param query
	 *            The query you want to evaluate
	 *            
	 * @param data
	 *            The path to the file that contains the data
	 *            
	 * @param results
	 *            The results your system returned
	 * 
	 * @return true if the results are the same, false otherwise
	 * 
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	public boolean compare(String query, String data, ArrayList<String> results)
			throws FileNotFoundException, UnsupportedEncodingException {

		boolean res;

		ArrayList<String> jena = execJena(query, data);

		res = compareLists(jena, results);

		return res;
	}

}
