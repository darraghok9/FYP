//	Darragh O'Keeffe
//	14702321
//	16 Oct 2017
package offlineEvaluation;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;


public class Test {
	public static void main(String[] args){
		
		int count = 0;
		ArrayList<Profile> profiles = new ArrayList<Profile>();
		ArrayList<Rating> userRatings = new ArrayList<Rating>();
		int lastProfile = 0;
		try {
			//create file scanner and printer
	        File input = new File("ratings2k.txt");
	        Scanner scanner = new Scanner(input);
	        
	        while(scanner.hasNextLine()) {
	        	String line = scanner.nextLine();
	        	StringTokenizer st = new StringTokenizer(line, "\t");
				Integer userID = Integer.valueOf(st.nextToken());
				Integer itemID = Integer.valueOf(st.nextToken());
				double rating = Double.valueOf(st.nextToken());
				
				if (userID>lastProfile && lastProfile>0 && !userRatings.isEmpty()){
					Profile p = new Profile(lastProfile);
					for (int i=0;i<(userRatings.size()-2);i++){
						for (int j=i+1;j<userRatings.size()-1;j++){
							//if (userRatings.get(i).getRating()!=userRatings.get(j).getRating()){
								p.addPreference(userRatings.get(i), userRatings.get(j));
							//}
						}						
					}
					profiles.add(p);
					userRatings.clear();
					count++;
					if (count%100==0){
						System.out.println(count);
					}
				}
				
				lastProfile = userID;
				userRatings.add(new Rating(userID, itemID, rating));
	        }
	        Profile p = new Profile(lastProfile);
			for (int i=0;i<(userRatings.size()-2);i++){
				for (int j=i+1;j<userRatings.size()-1;j++){
					p.addPreference(userRatings.get(i), userRatings.get(j));
				}						
			}
			profiles.add(p);
	        scanner.close();
		}
		catch(FileNotFoundException e) {
	        System.err.println("File not found. Please scan in new file.");
	    }
		int sum=0;
		for (int i=0;i<profiles.size();i++){
			sum+=profiles.get(i).size();
		}
		System.out.println(sum);
	}
}
