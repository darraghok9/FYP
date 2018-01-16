import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

//	Darragh O'Keeffe
//	14702321
//	14 Jan 2018

public class DataReader {
	private File input;
	private static final int TOTAL_USERS = 138493;
	private static final int TOTAL_MOVIES = 131262;
	//private static final int TOTAL_RATINGS = 131262;
	
	public DataReader(File input){
		this.input = input;
	}
	
	public ArrayList<Profile> getProfiles(){
		ArrayList<Profile> profiles = new ArrayList<Profile>();
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
	
	public ArrayList<Movie> getMovieList(){
		ArrayList<Movie> movies = new ArrayList<Movie>();
				
		try {
			Scanner scanner = new Scanner(input, "UTF-8");
			int userID, movieID, lastMovie;
			float rating;
			String line = scanner.nextLine();
			line = scanner.nextLine();
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
					movies.add(m);
					m = new Movie(movieID);
					lastMovie = movieID;
				}
				m.addRating(userID, rating);
			}
			movies.add(m);
			
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("Could not open file, check file name and location are correct");
		}
		
		return movies;
	}
	
	public void orderByMovieID(File output){
		Movie[] movies = new Movie[TOTAL_MOVIES];
		for (int i=0;i<=TOTAL_MOVIES;i++){
			movies[i] = new Movie(i);
		}
		try {
			Scanner scanner = new Scanner(input);
			String line = scanner.nextLine();
			line = scanner.nextLine();
			StringTokenizer st = new StringTokenizer(line, ",");
			
			int userID, itemID;
			float rating;
			
			userID = Integer.valueOf(st.nextToken());
			itemID = Integer.valueOf(st.nextToken());
			rating = Float.valueOf(st.nextToken());
			
			movies[itemID].addRating(userID, rating);
			
			while (scanner.hasNextLine()){
				line = scanner.nextLine();
				st = new StringTokenizer(line, ",");
				userID = Integer.valueOf(st.nextToken());
				itemID = Integer.valueOf(st.nextToken());
				rating = Float.valueOf(st.nextToken());
				
				movies[itemID].addRating(userID, rating);
				
			}
			scanner.close();
			
			
		    PrintWriter printer = new PrintWriter(output);
		    printer.write("userId,movieId,rating\n");
		    
		    for (int i=0;i<=TOTAL_MOVIES;i++){
		    	ArrayList<Integer> users = movies[i].getUsersWhoRated();
		    	ArrayList<Float> ratings = movies[i].getRatings();
		    	for (int j=0;j<users.size();j++){
		    		printer.write(users.get(j)+","+i+","+ratings.get(j)+"\n");
		    	}
		    }
			
			printer.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("Could not open file. Check file name and location are correct");
		}
	}
	
	public void createRandomProfileSample(File output, int N){
		if (N<=0){
			return;
		}
		//Create list of all integers for which a userID exists
		ArrayList<Integer> userIDs = new ArrayList<Integer>();
		for (int i=1;i<=TOTAL_USERS;i++){
			userIDs.add(i);
		}
		
		//randomly remove numbers from the list until N remain
		int temp;
		while (userIDs.size()>N){
			temp = (int) (Math.random()*userIDs.size());
			userIDs.remove(temp);
		}
		
		try {
			Scanner scanner = new Scanner(input);
			PrintWriter printer = new PrintWriter(output);
			String line = scanner.nextLine();
			printer.write(line+"\n");
			int userID, testID=userIDs.get(0);
			userIDs.remove(0);
			StringTokenizer st;
			
			while(scanner.hasNextLine()){
				line = scanner.nextLine();
				st = new StringTokenizer(line, ",");
				userID = Integer.valueOf(st.nextToken());
				if (userID==testID){
					printer.write(line+"\n");
				}
				if (userID>testID && !userIDs.isEmpty()){
					testID = userIDs.get(0);
					userIDs.remove(0);
					
				}
			}
			scanner.close();
			printer.close();
		} catch (FileNotFoundException e){
			e.printStackTrace();
			System.out.println("Could not open file. Check file name and location are correct");
		}
	}
	
	public void computeRatingVariance(ArrayList<Movie> movies, File output, int threshold, int size){
		ArrayList<Float> varianceScores = new ArrayList<Float>();
		ArrayList<Integer> movieIDs = new ArrayList<Integer>();
		for (int index=0;index<movies.size();index++){
			Movie m = movies.get(index);
			if (m.getSize()>=threshold){
				float meanRating = m.getAverageRating();
				ArrayList<Float> ratings = m.getRatings();
				float variance = 0;
				float ratingSize = (float) ratings.size();
				for (int i=0;i<ratings.size();i++){
					variance += (float) Math.pow((ratings.get(i)-meanRating),2);
				}
				variance = (float) Math.log(ratingSize)*(variance/ratingSize);
				if (variance>0.0 || (variance<varianceScores.size()-1 && varianceScores.size()<size)){
					if(varianceScores.isEmpty() ){
						varianceScores.add(variance);
						movieIDs.add(m.getID());
					}
					else{
						int j=0;
						do {
							if(variance>varianceScores.get(j)){
								varianceScores.add(j,variance);
								movieIDs.add(j,m.getID());
								if (varianceScores.size()>size){
									varianceScores.remove(size);
									movieIDs.remove(size);
								}
								break;
							}
							j++;
						} while (j<varianceScores.size() && j<size);
					}
				}
			}
		}
		try {
			PrintWriter printer = new PrintWriter(output);
			printer.write("movieID, variance\n");
			for (int index=0;index<varianceScores.size();index++){
				printer.write(movieIDs.get(index)+","+varianceScores.get(index)+"\n");
			}
			printer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("Could not open file. Check name and location are correct.");
		}
	}
	
	public float computeVariance(Movie a, Movie b){
		ArrayList<Integer> aUsers = a.getUsersWhoRated();
		ArrayList<Integer> bUsers = b.getUsersWhoRated();
		ArrayList<Float> aRatings = a.getRatings();
		ArrayList<Float> bRatings = b.getRatings();
		float aMean = a.getAverageRating();
		float bMean = b.getAverageRating();
		float numerator=0, aVariance=0, bVariance=0;
		int i=0, j=0;
		int aSize = a.getSize();
		int bSize = b.getSize();
		
		while (i<aSize && j <bSize){
			if (aUsers.get(i)>bUsers.get(j)){
				j++;
			} else {
				if (aUsers.get(i)<bUsers.get(j)){
					i++;
				} else {
					
					float aRating = aRatings.get(i);
					float bRating = bRatings.get(j);
					numerator += (aRating-aMean)*(bRating-bMean);
					aVariance += Math.pow((aRating-aMean), 2);
					bVariance += Math.pow((bRating-bMean), 2);
					i++;
					j++;
					
					
				}
			}
		}
		
		aVariance = (float) Math.sqrt(aVariance);
		bVariance = (float) Math.sqrt(bVariance);
		float pearsonCorrelation = numerator/(aVariance*bVariance);
		return (float) (Math.log(a.getSize())*Math.log(b.getSize())*(1-pearsonCorrelation));
	}
	
	public void computePairwiseRatingVariance(ArrayList<Movie> movies, File output, int threshold, int maxSize){
		for (int index=0;index<movies.size();index++){
			if (movies.get(index).getSize()<threshold){
				movies.remove(index);
				index--;
			}
			if (movies.get(index).getID()==296){
				movies.remove(index);
				index--;
			}
		}
		// TODO
		long startTime = System.currentTimeMillis();
		System.out.println("Small movies removed, "+movies.size()+" movies remain");
		ArrayList<Float> varianceScores = new ArrayList<Float>();
		ArrayList<Integer> aMovieIDs = new ArrayList<Integer>();
		ArrayList<Integer> bMovieIDs = new ArrayList<Integer>();
		
		for (int i=0;i<movies.size();i++){
			
			
			for (int j=i+1;j<movies.size();j++){
				Movie m = movies.get(i);
				Movie n = movies.get(j);
				
					float variance = computeVariance(m,n);	
					int size = varianceScores.size();
					
					if (size==0){
						varianceScores.add(variance);
						aMovieIDs.add(m.getID());
						bMovieIDs.add(n.getID());
					} else {
						if (variance>varianceScores.get(0)){
							varianceScores.add(0,variance);
							aMovieIDs.add(0,m.getID());
							bMovieIDs.add(0,n.getID());
						} else{
							if (variance<varianceScores.get(size-1) && size<maxSize){
								varianceScores.add(variance);
								aMovieIDs.add(m.getID());
								bMovieIDs.add(n.getID());
							}else{
								
								if (variance<varianceScores.get(0) && variance>varianceScores.get(size-1)){
									int k = size-2;
									while (k>=0){
										if (variance<varianceScores.get(k)){
											varianceScores.add(k+1,variance);
											aMovieIDs.add(k+1, m.getID());
											bMovieIDs.add(k+1, n.getID());
											break;
										}
										k--;
									}
								}
							}
						}
						if (varianceScores.size()>maxSize){
							varianceScores.remove(maxSize);
						}
					}
						
				
			}
			// TODO
			System.out.println(i);
		}
		// TODO
		long endTime = System.currentTimeMillis();
		System.out.println("Total time: "+(endTime-startTime));
		System.out.println("Time per pair: "+(endTime-startTime)/((movies.size()*(movies.size()-1))/2));
		try {
			PrintWriter printer = new PrintWriter(output);
			printer.write("movieID,movieID,variance\n");
			for (int index=0;index<varianceScores.size();index++){
				printer.write(aMovieIDs.get(index)+","+bMovieIDs.get(index)+","+varianceScores.get(index)+"\n");
			}
			printer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("Could not open file. Check name and location are correct.");
		}
	}
	
	public ArrayList<Movie> getTestMovies(){
		File testMovies = new File("testMovies.csv");
		File movieDetails = new File("movies.csv");
		ArrayList<Movie> movies = new ArrayList<Movie>();
		ArrayList<Integer> movieIDs = new ArrayList<Integer>();
		try {
			Scanner scanner = new Scanner(testMovies);
			String line = scanner.nextLine();
			StringTokenizer st;
			while (scanner.hasNextLine()){
				line = scanner.nextLine();
				st = new StringTokenizer(line, ",");
				movieIDs.add(Integer.valueOf(st.nextToken()));
			}
			
			scanner.close();
			scanner = new Scanner(movieDetails, "UTF-8");
			line = scanner.nextLine();
			int movieID;
			while (scanner.hasNextLine()){
				line = scanner.nextLine();
				st = new StringTokenizer(line);
				int length = 0;
				String temp = st.nextToken(",");
				length += (temp.length()+1);
				movieID = Integer.valueOf(temp);
				if (movieIDs.contains(movieID)){
					temp = st.nextToken(",");
					length += (temp.length()+1);
					int IMDBid = Integer.valueOf(temp);
					
					temp = st.nextToken(",");
					length += (temp.length()+1);
					int TMDBid = Integer.valueOf(temp);
					
					temp = st.nextToken(",");
					length += (temp.length()+1);
					
					String name = line.substring(length);
					length = 0;
					movies.add(new Movie(movieID, IMDBid, TMDBid, name));
				}
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return movies;
	}
	
	
	public ArrayList<Preference> getTestPreferences(){
		File testMovies = new File("testPairs.csv");
		File movieDetails = new File("movies.csv");
		ArrayList<Movie> movies = new ArrayList<Movie>();
		ArrayList<Preference> preferences = new ArrayList<Preference>();
		ArrayList<Integer> movieIDs = new ArrayList<Integer>();
		try {
			Scanner scanner = new Scanner(testMovies);
			String line = scanner.nextLine();
			StringTokenizer st;
			while (scanner.hasNextLine()){
				line = scanner.nextLine();
				st = new StringTokenizer(line, ",");
				movieIDs.add(Integer.valueOf(st.nextToken()));
				movieIDs.add(Integer.valueOf(st.nextToken()));
			}
			scanner.close();
			scanner = new Scanner(movieDetails, "UTF-8");
			line = scanner.nextLine();
			int movieID;
			while (scanner.hasNextLine()){
				line = scanner.nextLine();
				st = new StringTokenizer(line);
				int length = 0;
				String temp = st.nextToken(",");
				length += (temp.length()+1);
				movieID = Integer.valueOf(temp);
				if (movieIDs.contains(movieID)){
					temp = st.nextToken(",");
					length += (temp.length()+1);
					int IMDBid = Integer.valueOf(temp);
					
					temp = st.nextToken(",");
					length += (temp.length()+1);
					int TMDBid = Integer.valueOf(temp);
					
					temp = st.nextToken(",");
					length += (temp.length()+1);
					
					String name = line.substring(length);
					length = 0;
					movies.add(new Movie(movieID, IMDBid, TMDBid, name));
				}
			}
			for (int index=0;index<movieIDs.size();index+=2){
				Movie a = new Movie(0), b= new Movie(0);
				for (Movie m: movies){
					if (m.getID()==movieIDs.get(index)){
						a = m;
					}
					if (m.getID()==movieIDs.get(index+1)){
						b = m;
					}
				}
				preferences.add(new Preference(a, b, (byte) 0));
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return preferences;
	}
	
	public static void main(String[] args){
		DataReader dr = new DataReader(new File("ratings.csv"));
		dr.createRandomProfileSample(new File("ratingsSample.csv"), 500);
		
	}
	
}		
		

