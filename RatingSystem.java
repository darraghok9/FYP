import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

//	Darragh O'Keeffe
//	14702321
//	16 Jan 2018

public class RatingSystem {
	private DataReader dataReader;
	private ArrayList<Profile> profiles;
	private float[][] similarities;
	private Profile testProfile;
	
	public RatingSystem(DataReader dr){
		dataReader = dr;
		profiles = dr.getProfiles();
		similarities = new float[profiles.size()][profiles.size()];
	}
	
	public float computeSimilarity(Profile a, Profile b){
		ArrayList<Integer> aItems = a.getItemsRated();
		ArrayList<Integer> bItems = b.getItemsRated();
		ArrayList<Float> aRatings = a.getRatings();
		ArrayList<Float> bRatings = b.getRatings();
		double numerator=0;
		int i=0, j=0;
		int aSize = aItems.size();
		int bSize = bItems.size();
				
		while (i<aSize && j<bSize){
			if (aItems.get(i)>bItems.get(j)){
				j++;
			} else {
				if (aItems.get(i)<bItems.get(j)){
					i++;
				} else {
					numerator += (aRatings.get(i)*bRatings.get(j));
					i++;
					j++;
				}
			}
		}
		return (float) (numerator/(a.getRatingDistribution()*b.getRatingDistribution()));
	}
	
	public void computeAllSimilarities(){
		float similarity;
		for (int i=0;i<profiles.size();i++){
			for (int j=i;j<profiles.size();j++){
				if (i==j){
					similarity = 0;
				} else {
					similarity = computeSimilarity(profiles.get(i),profiles.get(j));
				}
				similarities[i][j] = similarity;
				similarities[j][i] = similarity;
			}
		}
	}
	
	public HashMap<Float,Profile> getNeighbours(Profile p, int k){
		HashMap<Float, Profile> map = new HashMap<Float, Profile>();
		
		ArrayList<Profile> neighbours = new ArrayList<Profile>();
		ArrayList<Float> similarities = new ArrayList<Float>();
		for (int index=0;index<profiles.size();index++){
			Profile q = profiles.get(index);
			float similarity = computeSimilarity(p, q);
			int size = neighbours.size();
			
			if (size==0){
				neighbours.add(q);
				similarities.add(similarity);
			} else {
				if (similarity>similarities.get(0)){
					neighbours.add(0, q);
					similarities.add(0, similarity);
				} else{
					if (similarity<similarities.get(size-1) && size<k){
						neighbours.add(q);
						similarities.add(similarity);
					}else{
						
						if (similarity<similarities.get(0) && similarity>similarities.get(size-1)){
							int i = size-2;
							while (i>=0){
								if (similarity<similarities.get(i)){
									neighbours.add(q);
									similarities.add(similarity);
									break;
								}
								i--;
							}
						}
					}
				}
				if (similarities.size()>k){
					neighbours.remove(k);
					similarities.remove(k);
				}
			}
		
		}
		for (int index=0;index<k;index++){
			map.put(similarities.get(index), neighbours.get(index));
		}
		return map;
	}
	
	public float getPredictedPosition(Profile p, int movie, Map<Float,Profile> neighbours){
		Set<Float> keys = neighbours.keySet();
		float numerator = 0, denominator=0;
		for (Float key: keys){
			Profile q = neighbours.get(key);
			if (q.hasRated(movie)){
				float rating = q.getRating(movie);
				numerator += (key*rating);
				denominator += key;
			}
		}
		
		return (numerator/denominator);
	}
	
	/*
	public int getRecommendation(Profile p){
		// TODO
		long startTime = System.currentTimeMillis();
		Map<Float,Profile> neighbours = getNeighbours(p,50);
		Map<Integer,Float> predictedRatings = new HashMap<Integer,Float>();
		
		for(Float key: neighbours.keySet()){
			ArrayList<Integer> itemsRated = neighbours.get(key).getItemsRated();
			for (Integer movie: itemsRated){
				predictedRatings.put(movie, getPredictedPosition(p,movie,neighbours));
			}
		}
		float maxRating = 0;
		int maxID = 0;
		float rating;
		for (Integer movie: predictedRatings.keySet()){
			rating = predictedRatings.get(movie);
			if (rating>maxRating && !p.hasRated(movie)){
				maxRating = rating;
				maxID = movie;
			}
		}
		// TODO
		long endTime = System.currentTimeMillis();
		System.out.println(endTime-startTime);
		return maxID;
	}
	*/
	
	public int getRecommendation(Profile p){
		// TODO
		long startTime = System.currentTimeMillis();
		Map<Float,Profile> neighbours = getNeighbours(p,50);
		Map<Integer,Float> predictedRatingNumerators = new HashMap<Integer,Float>();
		Map<Integer,Float> predictedRatingDenominators = new HashMap<Integer,Float>();
		for(Float similarity: neighbours.keySet()){
			Profile q = neighbours.get(similarity);
			ArrayList<Integer> itemsRated = q.getItemsRated();
			ArrayList<Float> ratings = q.getRatings();
			for (int index=0;index<ratings.size();index++){
				int testMovie = itemsRated.get(index);
				float rating = ratings.get(index);
				if (predictedRatingNumerators.containsKey(testMovie)){
					float value = predictedRatingNumerators.get(testMovie);
					value += similarity*rating;
					predictedRatingNumerators.put(testMovie, value);
					value = predictedRatingDenominators.get(testMovie);
					value += similarity;
					predictedRatingDenominators.put(testMovie, value);
				} else {
					predictedRatingNumerators.put(testMovie, rating*similarity);
					predictedRatingDenominators.put(testMovie, similarity);
				}
			}
		}
		float maxRating = 0;
		int maxID = 0;
		float rating;
		for (Integer movie: predictedRatingNumerators.keySet()){
			rating = predictedRatingNumerators.get(movie)/predictedRatingDenominators.get(movie);
			System.out.println(movie+"\t"+rating);
			if (rating>maxRating && !p.hasRated(movie)){
				maxRating = rating;
				maxID = movie;
			}
		}
		long endTime = System.currentTimeMillis();
		System.out.println(endTime-startTime);
		return maxID;
	}
	
	
	public static void main(String[] args){
		DataReader dr = new DataReader(new File("ratingsSample.csv"));
		RatingSystem r = new RatingSystem(dr);
		Profile p = new Profile(0);
		p.addRating(2710, (int)(Math.random()*5)-1);
		p.addRating(2657, (int)(Math.random()*5)+1);
		p.addRating(1721, (int)(Math.random()*5)+1);
		p.addRating(2628, (int)(Math.random()*5)+1);
		p.addRating(19, (int)(Math.random()*5)+1);
		p.addRating(327, (int)(Math.random()*5)+1);
		p.addRating(2384, (int)(Math.random()*5)+1);
		p.addRating(1917, (int)(Math.random()*5)+1);
		p.addRating(344, (int)(Math.random()*5)+1);
		p.addRating(1405, (int)(Math.random()*5)+1);
		p.addRating(2712, (int)(Math.random()*5)+1);
		p.addRating(1183, (int)(Math.random()*5)+1);
		p.addRating(4308, (int)(Math.random()*5)+1);
		p.addRating(2706, (int)(Math.random()*5)+1);
		p.addRating(34, (int)(Math.random()*5)+1);
		p.addRating(4310, (int)(Math.random()*5)+1);
		p.addRating(3608, (int)(Math.random()*5)+1);
		p.addRating(1416, (int)(Math.random()*5)+1);
		p.addRating(3785, (int)(Math.random()*5)+1);
		p.addRating(2700, (int)(Math.random()*5)+1);
		p.addRating(780, (int)(Math.random()*5)+1);
		p.addRating(1380, (int)(Math.random()*5)+1);
		p.addRating(1676, (int)(Math.random()*5)+1);
		p.addRating(1088, (int)(Math.random()*5)+1);
		p.addRating(5378, (int)(Math.random()*5)+1);
		p.addRating(2683, (int)(Math.random()*5)+1);
		p.addRating(2427, (int)(Math.random()*5)+1);
		p.addRating(4015, (int)(Math.random()*5)+1);
		p.addRating(1391, (int)(Math.random()*5)+1);
		p.addRating(8376, (int)(Math.random()*5)+1);
		p.addRating(4369, (int)(Math.random()*5)+1);
		p.addRating(2431, (int)(Math.random()*5)+1);
		p.addRating(2021, (int)(Math.random()*5)+1);
		p.addRating(4718, (int)(Math.random()*5)+1);
		p.addRating(1590, (int)(Math.random()*5)+1);
		p.addRating(788, (int)(Math.random()*5)+1);
		p.addRating(673, (int)(Math.random()*5)+1);
		p.addRating(6934, (int)(Math.random()*5)+1);
		p.addRating(1517, (int)(Math.random()*5)+1);
		p.addRating(1347, (int)(Math.random()*5)+1);
		p.addRating(585, (int)(Math.random()*5)+1);
		p.addRating(924, (int)(Math.random()*5)+1);
		p.addRating(920, (int)(Math.random()*5)+1);
		p.addRating(1373, (int)(Math.random()*5)+1);
		p.addRating(1035, (int)(Math.random()*5)+1);
		p.addRating(1407, (int)(Math.random()*5)+1);
		p.addRating(2335, (int)(Math.random()*5)+1);
		p.addRating(3977, (int)(Math.random()*5)+1);
		p.addRating(48385, (int)(Math.random()*5)+1);
		p.addRating(762, (int)(Math.random()*5)+1);
		p.addRating(2424, (int)(Math.random()*5)+1);
		p.addRating(1884, (int)(Math.random()*5)+1);
		p.addRating(2116, (int)(Math.random()*5)+1);
		p.addRating(509, (int)(Math.random()*5)+1);
		p.addRating(2617, (int)(Math.random()*5)+1);
		p.addRating(1982, (int)(Math.random()*5)+1);
		p.addRating(3988, (int)(Math.random()*5)+1);
		p.addRating(8957, (int)(Math.random()*5)+1);
		p.addRating(1923, (int)(Math.random()*5)+1);
		p.addRating(1372, (int)(Math.random()*5)+1);
		p.addRating(25, (int)(Math.random()*5)+1);
		p.addRating(6365, (int)(Math.random()*5)+1);
		p.addRating(1717, (int)(Math.random()*5)+1);
		p.addRating(33493, (int)(Math.random()*5)+1);
		p.addRating(333, (int)(Math.random()*5)+1);
		p.addRating(3273, (int)(Math.random()*5)+1);
		p.addRating(1587, (int)(Math.random()*5)+1);

		System.out.println(r.getRecommendation(p));
	}
	
}
	