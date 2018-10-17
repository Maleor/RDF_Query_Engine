package Requester;

import java.io.IOException;

/**
 * 
 * @author Mathieu Dodard
 * @author Quentin Monod
 *
 */
public class Runner {

	/**
	 * Main method to execute the program
	 * 
	 * @param args
	 *            The arguments needed to execute the program
	 * 
	 * @throws IOException
	 */
	public static void main(String args[]) throws IOException {

		if ((args.length != 7 && args.length != 2) || (args.length == 1 && args[0].equals("--help"))) {
			System.err.println(
					"Usage : File_to_queries File_to_data Output_file Verbose Export_results Export_stats Workload_time\n");
			System.err.println("File_to_queries : The path to the file that contains your queries");
			System.err.println("File_to_data : The path to the file that contains your data");
			System.err.println("Output_file : The path to the file in which you want to export your results");
			System.err.println("Verbose : 1 to show what the program does in the terminal, 0 otherwise");
			System.err.println("Export_results : 1 to export the results of your queries, 0 otherwise");
			System.err.println("Export_stats : 1 to export the stats of your queries, 0 otherwise");
			System.err.println(
					"Workload_time : 1 to visualize the duration that has been necessery to evaluate each query, 0 otherwise");
			return;
		}

		// if((args[3] != "0") && (args[3] != "1")) {
		// System.out.println(args[3]);
		// System.err.println("Usage of Verbose : 1 to show what the program does in the
		// terminal, 0 otherwise");
		// return;
		// }
		// if(args[4] != "0" || args[4] != "1") {
		// System.err.println("Usage of Export_results : 1 to export the results of your
		// queries, 0 otherwise");
		// return;
		// }
		// if(args[5] != "0" || args[5] != "1") {
		// System.err.println("Usage of Export_stats : 1 to export the stats of your
		// queries, 0 otherwise");
		// return;
		// }
		// if(args[6] != "0" || args[6] != "1") {
		// System.err.println("Usage of Workload_time : 1 to visualize the duration that
		// has been necessery to evaluate each query, 0 otherwise");
		// return;
		// }

		Requester requester = new Requester(args);
		requester.run();

	}
}
