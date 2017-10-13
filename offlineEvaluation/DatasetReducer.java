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
		ArrayList<Integer> userIDs = new ArrayList<Integer>();
		for (int i=1;i<=TOTAL_NUMBER_OF_USERS;i++){
			userIDs.add(i);
		}
		
		int temp;
		while (userIDs.size()>DESIRED_SIZE){
			temp = (int) (Math.random()*userIDs.size());
			userIDs.remove(temp);
		}
		
		
		try {
	        File input = new File("ratings.txt");
	        File output = new File("ratings2k.txt");
	        Scanner scanner = new Scanner(input);
	        String line = scanner.nextLine();
	        int index=0;
	        
	        int lastUser = 0;
	        PrintWriter printer = new PrintWriter(output);
	        while(scanner.hasNextLine()) {
	            line = scanner.nextLine()+"\n";
	            StringTokenizer st = new StringTokenizer(line, "\t");
				Integer userId = Integer.valueOf(st.nextToken());
				lastUser = userIDs.get(0);
				if (userId.equals(userIDs.get(0))){
					printer.write(line);
					//System.out.println(userId+", "+userIDs.get(0));
				}
				if (userId>userIDs.get(0)){
					userIDs.remove(0);
				}
				index++;
				
			}
	        scanner.close();
	        printer.close();
	    }
	    catch(FileNotFoundException e) {
	        System.err.println("File not found. Please scan in new file.");
	    }
		
	}
}
