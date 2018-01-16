import java.io.File;
import java.io.FileNotFoundException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

public class DatasetManipulator {
	
	public DatasetManipulator(){
		
	}

	
	public ArrayList<Profile> getProfiles(String filename){
		ArrayList<Profile> profiles = new ArrayList<Profile>();
		File input = new File(filename);
		try {
			Scanner scanner = new Scanner(input);
			String line = scanner.nextLine();
			line = scanner.nextLine();
			StringTokenizer st = new StringTokenizer(line, ",");
			int lastProfile = Integer.valueOf(st.nextToken());
			Profile p = new Profile(lastProfile);			
			p.addRating(Integer.valueOf(st.nextToken()), Float.valueOf(st.nextToken()));
			
			int userID, itemID;
			float rating;
			while (scanner.hasNextLine()){
				line = scanner.nextLine();
				st = new StringTokenizer(line, ",");
				userID = Integer.valueOf(st.nextToken());
				itemID = Integer.valueOf(st.nextToken());
				rating = Float.valueOf(st.nextToken());
				if (userID!=lastProfile){
					profiles.add(p);
					p = new Profile(userID);
					lastProfile = userID;
					if (profiles.size()%1000 == 0){
						System.out.println(profiles.size());
					}
				}
				p.addRating(itemID, rating);
			}
			profiles.add(p);
			
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("Could not open file. Check file name and location are correct");
		}
		
		
		return profiles;
	}
	
	public ArrayList<Integer> getCommonItems(Profile a, Profile b){
		ArrayList<Integer> aItems = a.getItemsRated();
		ArrayList<Integer> bItems = b.getItemsRated();
		ArrayList<Integer> commonItems = new ArrayList<Integer>();
		for (int index=0;index<aItems.size();index++){
			int currentItem = aItems.get(index);
			if (bItems.contains(currentItem)){
				commonItems.add(currentItem);
			}
		}
		return commonItems;
	}
	
	public double computeRatingSimilarity(Profile a, Profile b){
		ArrayList<Integer> aItems = a.getItemsRated();
		ArrayList<Integer> bItems = b.getItemsRated();
		ArrayList<Float> aRatings = a.getRatings();
		ArrayList<Float> bRatings = b.getRatings();
		double numerator=0;
		for (int index=0;index<aItems.size();index++){
			int currentItem = aItems.get(index);
			int bIndex = bItems.indexOf(currentItem);
			if (bIndex>=0){
				numerator += (aRatings.get(index) * bRatings.get(bIndex));
			}
		}
		
		return numerator/(a.getRatingDistribution()*b.getRatingDistribution());
	}
	
	public double computePreferenceSimilarity(Profile a, Profile b){
		// TODO Remove Print Statements
		ArrayList<Integer> aItems = a.getItemsRated();
		ArrayList<Integer> bItems = b.getItemsRated();
		ArrayList<Float> aRatings = a.getRatings();
		ArrayList<Float> bRatings = b.getRatings();
		ArrayList<Float> A = new ArrayList<Float>();
		ArrayList<Float> B = new ArrayList<Float>();

		for (int index=0;index<aItems.size();index++){
			int currentItem = aItems.get(index);
			int bIndex = bItems.indexOf(currentItem);
			if (bIndex>=0){
				A.add(aRatings.get(index));
				B.add(bRatings.get(bIndex));
			}
		}
		double numerator = 0;
		for (int i=0;i<A.size();i++){
			for (int j=i+1;j<B.size();j++){
				if (Float.compare(A.get(i), A.get(j))==Float.compare(B.get(i), B.get(j))){
					numerator++;
				}
			}
		}
		System.out.print(A.size()+"\t"+numerator+"\t");
		double aSize = (double) aItems.size();
		aSize = (aSize*(aSize-1))/2.0;
		double bSize = (double) bItems.size();
		bSize = (bSize*(bSize-1))/2.0;
		double similarity = numerator/(Math.sqrt(aSize*bSize));
		return similarity;
	}
	
	public ArrayList<Movie> getMovieList(String filename){
		ArrayList<Movie> movies = new ArrayList<Movie>();
		File input = new File(filename);
		
		try {
			Scanner scanner = new Scanner(input);
			int userID, movieID, lastMovie;
			float rating;
			String line = scanner.nextLine();
			StringTokenizer st = new StringTokenizer(line, ",");
			userID = Integer.valueOf(st.nextToken());
			movieID = Integer.valueOf(st.nextToken());
			rating = Float.valueOf(st.nextToken());
			lastMovie = movieID;
			Movie m = new Movie(movieID);
			m.addRating(userID, rating);
			
			while (scanner.hasNextLine()){
				line = scanner.nextLine();
				st = new StringTokenizer(line, ",");
				userID = Integer.valueOf(st.nextToken());
				movieID = Integer.valueOf(st.nextToken());
				rating = Float.valueOf(st.nextToken());
				if (lastMovie!=movieID){
					if (m.getSize()>5000){
						movies.add(m);
					}
					m = new Movie(movieID);
					lastMovie = movieID;
				}
				m.addRating(userID, rating);
			}
			if (m.getSize()>5000){
				movies.add(m);
			}
			
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("Could not open file, check file name and location are correct");
		}
		
		return movies;
	}
	
	public float computeEntropy(Movie a, Movie b){
		ArrayList<Integer> aUsers = a.getUsersWhoRated();
		ArrayList<Integer> bUsers = b.getUsersWhoRated();
		ArrayList<Float> aRatings = a.getRatings();
		ArrayList<Float> bRatings = b.getRatings();
		ArrayList<Float> A = new ArrayList<Float>();
		ArrayList<Float> B = new ArrayList<Float>();

		for (int index=0;index<aUsers.size();index++){
			int currentItem = aUsers.get(index);
			int bIndex = bUsers.indexOf(currentItem);
			if (bIndex>=0){
				A.add(aRatings.get(index));
				B.add(bRatings.get(bIndex));
			}
		}
		
		float numerator = 0, aDistribution=0, bDistribution=0;
		float aMean = a.getAverageRating();
		float bMean = b.getAverageRating();
		if (A.size()==0){
			return 0;
		}
		for (int index=0;index<A.size();index++){
			float aRating = A.get(index);
			float bRating = B.get(index);
			numerator += (aRating-aMean)*(bRating-bMean);
			aDistribution += Math.pow((aRating-aMean), 2);
			bDistribution += Math.pow((bRating-bMean), 2);
		}
		aDistribution = (float) Math.sqrt(aDistribution);
		bDistribution = (float) Math.sqrt(bDistribution);
		float pearsonCorrelation = numerator/(aDistribution*bDistribution);
		//return pearsonCorrelation;
		return (float) (Math.log(a.getSize())*Math.log(b.getSize())*(1-pearsonCorrelation));
	}
	
	
	public static void main(String[] args){
		DatasetManipulator dm = new DatasetManipulator();
		long startTime = System.currentTimeMillis();

		ArrayList<Movie> movies = dm.getMovieList("ratings.csv");
		int size = movies.size();
		float pvScores[][] = new float[size][size];
		for (int i=0;i<size;i++){
			for (int j=i;j<size;j++){
				pvScores[i][j]=dm.computeEntropy(movies.get(i), movies.get(j));
			}
		}
		
		long endTime = System.currentTimeMillis();
		System.out.println(size);
		System.out.println(endTime-startTime);
	}
}
