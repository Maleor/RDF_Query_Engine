package Dico;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

import org.openrdf.model.Statement;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.Rio;
import org.openrdf.rio.helpers.RDFHandlerBase; 


public final class RDFRawParser {
	
	
	
	private static class RDFListener extends RDFHandlerBase {

		public ArrayList<String> subjects = new ArrayList<>();
		public ArrayList<String> predicates = new ArrayList<>();
		public ArrayList<String> objects = new ArrayList<>();
		
		@Override
		public void handleStatement(Statement st) {
			subjects.add(st.getSubject().toString());
			predicates.add(st.getPredicate().toString());
			objects.add(st.getObject().toString());
		}

	};
	
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
		
		for(int index = 0 ; index < 3 ; index++)
			datas.add(new ArrayList<>());
		
		for(String string : listener.subjects)
			datas.get(0).add(string);
		
		for(String string : listener.predicates)
			datas.get(1).add(string);
		
		for(String string : listener.objects)
			datas.get(2).add(string);
			
		
		return datas;
	}
	
	
}
