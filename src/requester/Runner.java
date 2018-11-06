package requester;

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
	 * @param args The arguments needed to execute the program
	 * 
	 * @throws IOException
	 */
	public static void main(String args[]) throws IOException {
		
		if ((args.length < 3) || (args.length == 1 && args[0].equals("--help"))) {
			System.err.println(
					"Usage : -queries 'File_to_queries' -data 'File_to_data' output 'Output_file' -verbose -export_results -export_stats -workload_time\n");
			System.err.println("File_to_queries : The path to the file that contains your queries");
			System.err.println("File_to_data : The path to the file that contains your data");
			System.err.println("Output_file : The path to the file in which you want to export your results");
			System.err.println("Verbose (Optional) : to show what the program does in the terminal");
			System.err.println("Export_results (Optional) : to export the results of your queries");
			System.err.println("Export_stats (Optional) : to export the stats of your queries");
			System.err.println(
					"Workload_time (Optional) : to visualize the duration that has been necessery to evaluate each query");
			return;
		}
		
		String[] args2 = new String[7];
		
		args2[3] = "0";
		args2[4] = "0";
		args2[5] = "0";
		args2[6] = "0";
		
		for(int index = 0 ; index < args.length ; index++) {
			
			if(args[index].equals("-queries"))
				args2[0] = args[index+1];
			
			if(args[index].equals("-data"))
				args2[1] = args[index+1];
			
			
			if(args[index].equals("-output"))
				args2[2] = args[index+1];
			
			if(args[index].equals("-verbose"))
				args2[3] = "1";
			
			if(args[index].equals("-export_results"))
				args2[4] = "1";
			
			if(args[index].equals("-export_stats"))
				args2[5] = "1";
			
			if(args[index].equals("-workload_time"))
				args2[6] = "1";
			
		}
		
		if(args2[3].equals("1")) {//if verbose
			System.out.println("#################################");
			System.out.println("####### PROGRAM EXECUTION #######");
			System.out.println("#################################\n");
		}
		
		Requester requester = new Requester(args2);
		requester.run();
		
		if(args2[3].equals("1")) {//if verbose
			System.out.println("\n##################################");
			System.out.println("####### END OF THE PROGRAM #######");
			System.out.println("##################################");
		}
	}
}
