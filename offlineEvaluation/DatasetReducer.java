//	Darragh O'Keeffe
//	14702321
//	12 Oct 2017
package offlineEvaluation;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

public class DatasetReducer {
	public static final int TOTAL_NUMBER_OF_USERS = 7120;
	public static final int DESIRED_SIZE = 2000;
	
	public static void main(String[] args){
		
		//Create list of all integers for which a userID exists
		ArrayList<Integer> userIDs = new ArrayList<Integer>();
		for (int i=1;i<=TOTAL_NUMBER_OF_USERS;i++){
			userIDs.add(i);
		}
		
		//randomly remove numbers from the list until 2000 remain
		int temp;
		while (userIDs.size()>DESIRED_SIZE){
			temp = (int) (Math.random()*userIDs.size());
			userIDs.remove(temp);
		}
		
		
		try {
			//create file scanner and printer
	        File input = new File("ratings.txt");
	        File output = new File("ratings2k.txt");
	        Scanner scanner = new Scanner(input);
	        String line = scanner.nextLine();
	        PrintWriter printer = new PrintWriter(output);
	        
	        /* for each line in the ratings.txt, check if the userID of the
	         * line matches the lowest ID in the list. If it does, add that
	         * line to the ratings2k.txt file. If it is greater than the lowest
	         * in the list, remove that value from the list
	         */
	        while(scanner.hasNextLine()) {
	            line = scanner.nextLine()+"\n";
	            StringTokenizer st = new StringTokenizer(line, "\t");
				Integer userId = Integer.valueOf(st.nextToken());
				
				if (userId.equals(userIDs.get(0))){
					printer.write(line);
				}
				if (userId>userIDs.get(0)){
					userIDs.remove(0);
				}
				
			}
	        scanner.close();
	        printer.close();
	    }
	    catch(FileNotFoundException e) {
	        System.err.println("File not found. Please scan in new file.");
	    }
		
	}
}
