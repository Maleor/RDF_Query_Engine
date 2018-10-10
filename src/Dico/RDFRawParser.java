package Dico;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import org.openrdf.model.Statement;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.Rio;
import org.openrdf.rio.helpers.RDFHandlerBase;

public final class RDFRawParser {

	private static class RDFListener extends RDFHandlerBase {

		@Override
		public void handleStatement(Statement st) {
			System.out.println("\n" + st.getSubject() + "\t " + st.getPredicate() + "\t " + st.getObject());
		}

	};

	public static void main(String args[]) throws FileNotFoundException {

		Reader reader = new FileReader("data/watdiv-mini-projet/data_RDFXML/100K.rdfxml");

		org.openrdf.rio.RDFParser rdfParser = Rio.createParser(RDFFormat.RDFXML);
		rdfParser.setRDFHandler(new RDFListener());

		try {
			rdfParser.parse(reader, "");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			reader.close();
		} catch (IOException e) {
		}

	}

}