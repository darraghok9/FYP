import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.StringTokenizer;

//	Darragh O'Keeffe
//	14702321
//	14 Jan 2018

public class DataReader {
	private static final int TOTAL_USERS = 138493;
	private static final int TOTAL_MOVIES = 131262;
	//private static final int TOTAL_RATINGS = 131262;
	
	public DataReader(){	}
	
	public ArrayList<Profile> getProfiles(File input){
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
	
	public ArrayList<Movie> getMovies(File input){
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
	
	public ArrayList<Preference> getTestPreferences(File input){
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
			e.printStackTrace();
			System.out.println("Could not open file. Check name and location are correct.");
		}
		return preferences;
	}
	
	public ArrayList<Movie> getMovieDetails(File input){
		ArrayList<Movie> movies = new ArrayList<Movie>();
		try {
			Scanner scanner = new Scanner(input);
			String line = scanner.nextLine();
			StringTokenizer st;
			
			while (scanner.hasNextLine()){
				line = scanner.nextLine();
				st = new StringTokenizer(line, ",");
				int length = 0;
				String temp = st.nextToken();
				length += (temp.length()+1);
				int movieID = Integer.valueOf(temp);
				
				temp = st.nextToken();
				length += (temp.length()+1);
				int IMDBid = Integer.valueOf(temp);
				
				temp = st.nextToken();
				length += (temp.length()+1);
				int TMDBid = Integer.valueOf(temp);
				
				temp = st.nextToken();
				length += (temp.length()+1);
				
				String name = line.substring(length);
				length = 0;
				movies.add(new Movie(movieID, IMDBid, TMDBid, name));
				
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("Could not open file. Check name and location are correct.");
		}
		return movies;
	}
	
	public void orderByMovieID(ArrayList<Profile> profiles, File output){
		Movie[] movies = new Movie[TOTAL_MOVIES];
		for (int i=0;i<=TOTAL_MOVIES;i++){
			movies[i] = new Movie(i);
		}
		for (Profile p: profiles){
			ArrayList<Integer> moviesRated = p.getItemsRated();
			ArrayList<Float> ratings = p.getRatings();
			int userID = p.getID();
			for (int index=0;index<moviesRated.size();index++){
				movies[moviesRated.get(index)].addRating(userID, ratings.get(index));
			}
		}
		writeMovies(new ArrayList<>(Arrays.asList(movies)), output, true);
	}
	
	private ArrayList<Integer> createRandomIDSample(int N){
		if (N<=0){
			return null;
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
		return userIDs;
	}
	
	public void createRandomProfileSample(ArrayList<Profile> profiles, File output, int N){
		ArrayList<Integer> userIDs = createRandomIDSample(N);
		ArrayList<Profile> randomProfiles = new ArrayList<Profile>();
		int i=0, j=0;
		while (i<profiles.size() && j<userIDs.size()){
			if (profiles.get(i).getID()>userIDs.get(j)){
				j++;
			} else {
				if (profiles.get(i).getID()<userIDs.get(j)){
					i++;
				} else {
					randomProfiles.add(profiles.get(i));
					i++;
					j++;
				}
			}
		}
		writeProfiles(randomProfiles, output);
	}
	
	private float computeVariance(Movie a){
		ArrayList<Float> ratings = a.getRatings();
		float meanRating = a.getAverageRating();
		float ratingSize = a.getSize();
		float variance = 0;
		for (int i=0;i<ratings.size();i++){
			variance += (float) Math.pow((ratings.get(i)-meanRating),2);
		}
		variance = (float) Math.log(ratingSize)*(variance/ratingSize);
		return variance;
	}
	
	public void computeRatingVariance(ArrayList<Movie> movies, File output, int threshold, int N){
		ArrayList<Float> varianceScores = new ArrayList<Float>();
		ArrayList<Movie> movieIDs = removeSparselyRatedMovies(movies,threshold);
		for (int index=0;index<movies.size();index++){
			float variance = computeVariance(movieIDs.get(index));
			varianceScores.add(variance);
		}
		ArrayList<Movie> testMovies = getTopN(varianceScores, movies, N);
		writeMovies(testMovies,output,false);
	}
	
	private float computeVariance(Movie a, Movie b){
		ArrayList<Integer> commonUsers = a.getCommonProfiles(b);
		ArrayList<Float> aRatings = a.getRatingsFor(commonUsers);
		ArrayList<Float> bRatings = b.getRatingsFor(commonUsers);
		float aMean = a.getAverageRating();
		float bMean = b.getAverageRating();
		float numerator=0, aVariance=0, bVariance=0;
			
		for (int index=0;index<commonUsers.size();index++){
			float aRating = aRatings.get(index);
			float bRating = bRatings.get(index);
			numerator += (aRating-aMean)*(bRating-bMean);
			aVariance += Math.pow((aRating-aMean), 2);
			bVariance += Math.pow((bRating-bMean), 2);
		}
		aVariance = (float) Math.sqrt(aVariance);
		bVariance = (float) Math.sqrt(bVariance);
		float pearsonCorrelation = numerator/(aVariance*bVariance);
		return (float) (Math.log(commonUsers.size())*(1-pearsonCorrelation));
	}
	
	private ArrayList<Movie> removeSparselyRatedMovies(ArrayList<Movie> movies, int threshold){
		for (int index=0;index<movies.size();index++){
			if (movies.get(index).getSize()<threshold){
				movies.remove(index);
				index--;
			}
		}
		return movies;
	}
	
	private <S,T extends Comparable<T>> ArrayList<S> getTopN(ArrayList<T> values, ArrayList<S> objects, int N){
		if (objects.size()<=1){
			return objects;
		}
		ArrayList<S> o = new ArrayList<S>();
		ArrayList<T> v = new ArrayList<T>();
		o.add(objects.get(0));
		v.add(values.get(0));
		if (values.get(1).compareTo(v.get(0))>0){
			o.add(objects.get(1));
			v.add(values.get(1));
		}else{
			o.add(1,objects.get(1));
			v.add(1,values.get(1));
		}
		for (int i=2;i<objects.size();i++){
			int size = v.size();
			T value = values.get(i);
			if (value.compareTo(v.get(size-1))>0){
				int k=0;
				while (k<size){
					if (value.compareTo(v.get(k))>0){
						v.add(k,value);
						o.add(k,objects.get(i));
					}
				}
			}else{
				if (size<N){
					v.add(value);
					o.add(objects.get(i));
				}
			}
		}
		return o;
	}
	
	public ArrayList<Preference> computePairwiseRatingVariance(ArrayList<Movie> movies, File output, int threshold, int N){
		movies = removeSparselyRatedMovies(movies, threshold);
		
		ArrayList<Float> varianceScores = new ArrayList<Float>();
		ArrayList<Preference> preferences = new ArrayList<Preference>();
		
		for (int i=0;i<movies.size();i++){
			for (int j=i+1;j<movies.size();j++){
				
				Movie m = movies.get(i);
				Movie n = movies.get(j);
				Preference p = new Preference(m,n,(byte) 0);
				float variance = computeVariance(m,n);
				preferences.add(p);
				varianceScores.add(variance);
			}
		}
		ArrayList<Preference> testPairs = getTopN(varianceScores, preferences, N);
		return testPairs;
	}

	
	public ArrayList<Preference> getPreferences(File input){
		ArrayList<Preference> preferences = new ArrayList<Preference>();
		try {
			Scanner scanner = new Scanner(input);
			String line = scanner.nextLine();
			StringTokenizer st;
			Movie m, n;
			Preference p;
			while (scanner.hasNextLine()){
				line = scanner.nextLine();
				st = new StringTokenizer(line, ",");
				m = new Movie(Integer.valueOf(st.nextToken()));
				n = new Movie(Integer.valueOf(st.nextToken()));
				p = new Preference(m,n, (byte)0);
				preferences.add(p);
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("Could not open file, check file name and location are correct");
		}	
		return preferences;
	}
	
	public ArrayList<Profile> profilesWhichRated(ArrayList<Profile> profiles, ArrayList<Movie> movies, int threshold){
		ArrayList<Profile> testProfiles = new ArrayList<Profile>();
		for (Profile p: profiles){
			if (p.numberOfMoviesRated(movies)>=threshold){
				testProfiles.add(p);
			}
		}
		return testProfiles;
	}
	
	public void writeProfiles(ArrayList<Profile> profiles, File output){
		try {
			PrintWriter printer = new PrintWriter(output);
			printer.write("userId,movieId,rating\n");
		    for (Profile p: profiles){
		    	printer.write(p.toRatingString());
		    }
			printer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("Could not open file. Check name and location are correct.");
		}
	}
	
	public void writeMovies(ArrayList<Movie> movies, File output, boolean writeRatings){
		try {
			PrintWriter printer = new PrintWriter(output);
			if (writeRatings){
				printer.write("userId,movieId,rating\n");
			} else {
				printer.write("movieId\n");
			}
		    for (Movie m: movies){
		    	if (writeRatings){
		    		printer.write(m.toRatingString());
				} else {
					printer.write(m.toString());
				}
		    }
			printer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("Could not open file. Check name and location are correct.");
		}
	}
	
	public void writePreferences(ArrayList<Preference> preferences, File output){
		try {
			PrintWriter printer = new PrintWriter(output);
			printer.write("movieId,movieId\n");
		    for (Preference p: preferences){
		    	printer.write(p.getItemA().getID()+","+p.getItemB().getID()+"\n");
		    }
			printer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("Could not open file. Check name and location are correct.");
		}
	}
	
	public static void main(String[] args){
		DataReader dr = new DataReader();
		ArrayList<Preference> preferences = dr.getTestPreferences(new File("test.csv"));
		ArrayList<Integer> ids = new ArrayList<Integer>();
		for (Preference p : preferences){
			int a = p.getItemA().getID();
			int b = p.getItemB().getID();
			if (!ids.contains(a)){
				ids.add(a);
			}
			if (!ids.contains(b)){
				ids.add(b);
			}
			
		}
		System.out.println(ids.size());
	}
	
}		
		

