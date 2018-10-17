package DataParser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.openrdf.model.Statement;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.Rio;
import org.openrdf.rio.helpers.RDFHandlerBase;

/**
 * 
 * @author Mathieu Dodard
 * @author Quentin Monod
 *
 */
public final class RDFRawParser {

	/**
	 * 
	 * Private class that handles the statements creating 3 lists with the data, one
	 * for the subjects, one for the predicates and one for the objects.
	 *
	 */
	private static class RDFListener extends RDFHandlerBase {

		public ArrayList<String> subjects = new ArrayList<>();
		public ArrayList<String> predicates = new ArrayList<>();
		public ArrayList<String> objects = new ArrayList<>();

		/**
		 * Takes the values in a statement (triple) and puts them in lists
		 * 
		 * @param statement
		 *            the statement you want to handle
		 */
		@Override
		public void handleStatement(Statement statement) {
			subjects.add(statement.getSubject().toString());
			predicates.add(statement.getPredicate().toString());
			objects.add(statement.getObject().toString());
		}

	};

	/**
	 * Parses a file to get all the triples into an array list of 3 array lists
	 * 
	 * @param file
	 *            The file you want to parse
	 * 
	 * @return A list of 3 lists that contains all the data
	 * 
	 * @throws FileNotFoundException
	 */
	public ArrayList<ArrayList<String>> parseFile(String file) throws FileNotFoundException {

		Reader reader = new FileReader(file);

		org.openrdf.rio.RDFParser rdfParser = Rio.createParser(RDFFormat.RDFXML);
		RDFListener listener = new RDFListener();
		rdfParser.setRDFHandler(listener);

		try {
			rdfParser.parse(reader, "");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			reader.close();
		} catch (IOException e) {
		}

		ArrayList<ArrayList<String>> datas = new ArrayList<>();

		for (int index = 0; index < 3; index++)
			datas.add(new ArrayList<>());

		for (String string : listener.subjects)
			datas.get(0).add(string);

		for (String string : listener.predicates)
			datas.get(1).add(string);

		for (String string : listener.objects)
			datas.get(2).add(string);

		return datas;
	}

}
