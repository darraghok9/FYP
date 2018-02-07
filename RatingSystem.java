import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

//	Darragh O'Keeffe
//	14702321
//	16 Jan 2018

public class RatingSystem {
	private ArrayList<Profile> profiles;
	private float[][] similarities;
	
	public RatingSystem(ArrayList<Profile> profiles){
		this.profiles = profiles;
		similarities = new float[profiles.size()][profiles.size()];
	}
	
	public float computeSimilarity(Profile a, Profile b){
		ArrayList<Integer> commonItems = a.getCommonItems(b);
		ArrayList<Float> aRatings = a.getRatingsFor(commonItems);
		ArrayList<Float> bRatings = b.getRatingsFor(commonItems);
		double numerator=0;
		
		for (int i=0;i<commonItems.size();i++){
			numerator += (aRatings.get(i)*bRatings.get(i));
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
	
	public float getPredictedRating(Profile p, int movie, Map<Float,Profile> neighbours){
		Set<Float> keys = neighbours.keySet();
		float numerator = 0, denominator=0;
		for (Float key: keys){
			Profile q = neighbours.get(key);
			if (q.hasRated(movie)){
				float position = q.getPosition(movie);
				numerator += (key*position);
				denominator += key;
			}
		}
		
		return (numerator/denominator);
	}
	
	private HashMap<Integer,Float> getPredictedRatings(Profile p, HashMap<Float,Profile> neighbours){
		HashMap<Integer, Float> predictedRatings = new HashMap<Integer,Float>();
		for(Float key: neighbours.keySet()){
			ArrayList<Integer> itemsRated = neighbours.get(key).getItemsRated();
			for (Integer movie: itemsRated){
				predictedRatings.put(movie, getPredictedRating(p,movie,neighbours));
			}
		}
		return predictedRatings;
	}
		
	public ArrayList<Integer> getTopNRecommendations(Profile p, int N){
		if (N<=0){
			return null;
		}
		HashMap<Float,Profile> neighbours = getNeighbours(p,50);
		HashMap<Integer,Float> predictedRatings = getPredictedRatings(p,neighbours);
		ArrayList<Integer> recommendations = new ArrayList<Integer>();
		ArrayList<Float> ratings = new ArrayList<Float>();
		float rating;
		float minRating = 100;
		for (Integer movie: predictedRatings.keySet()){
			rating = predictedRatings.get(movie);
			if (ratings.size()<N){
				if (rating<minRating){
					minRating = rating;
				}
				ratings.add(rating);
				recommendations.add(movie);
			}else {
				
			}
		}
		return recommendations;
	}
	
	
	public static void main(String[] args){
		DataReader dr = new DataReader();
		RatingSystem r = new RatingSystem(dr.getProfiles(new File("ratingsSample.csv")));
		
		Profile p = new Profile(0);
		
		p.addRating(86332,1);
		p.addRating(106072,1);
		
	}
	
}
	