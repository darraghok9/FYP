import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

//	Darragh O'Keeffe
//	14702321
//	16 Jan 2018

public class PreferenceSystem {
	private ArrayList<Profile> profiles;
	private float[][] similarities;
	
	public PreferenceSystem(ArrayList<Profile> profiles){
		this.profiles = profiles;
		similarities = new float[profiles.size()][profiles.size()];
	}
	
	public float computeSimilarity(Profile a, Profile b){
		ArrayList<Integer> commonItems = a.getCommonItems(b);
		ArrayList<Float> aRatings = a.getRatingsFor(commonItems);
		ArrayList<Float> bRatings = b.getRatingsFor(commonItems);
						
		double numerator = 0;
		for (int i=0;i<aRatings.size();i++){
			for (int j=i+1;j<bRatings.size();j++){
				if (Float.compare(aRatings.get(i), aRatings.get(j)) == 
						Float.compare(bRatings.get(i), bRatings.get(j))){
					numerator++;
				}
			}
		}
		float aSize = (a.size()*(a.size()-1))/2;
		float bSize = (b.size()*(b.size()-1))/2;
		float similarity = (float) (numerator/(Math.sqrt(aSize*bSize)));
		return similarity;
		
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
		for (int index=0;index<similarities.size();index++){
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
				float rating = q.getRating(movie);
				numerator += (key*rating);
				denominator += key;
			}
		}
		
		return (numerator/denominator);
	}
	
	public int getRecommendation(Profile p){
		Map<Float,Profile> neighbours = getNeighbours(p,50);
		Map<Integer,Float> predictedPositions = new HashMap<Integer,Float>();
		
		for(Float key: neighbours.keySet()){
			ArrayList<Integer> itemsRated = neighbours.get(key).getItemsRated();
			for (Integer movie: itemsRated){
				predictedPositions.put(movie, getPredictedRating(p,movie,neighbours));
			}
		}
		float maxPosition = -1;
		int maxID = 0;
		float position;
		for (Integer movie: predictedPositions.keySet()){
			position = predictedPositions.get(movie);
			if (position>maxPosition && !p.hasRated(movie)){
				maxPosition = position;
				maxID = movie;
			}
		}
		return maxID;
	}
	
	public static void main(String[] args){
		DataReader dr = new DataReader();
		PreferenceSystem r = new PreferenceSystem(dr.getProfiles(new File("ratingSample.csv")));
		
		Profile p = new Profile(0);
		
		
		p.addRating(86332,5);
		p.addRating(106072,5);
		p.addRating(88140,5);
		p.addRating(110102,5);
		p.addRating(59315,5);
		p.addRating(77516,5);
		p.addRating(102125,5);
		p.addRating(33794,5);
		p.addRating(103042,5);
	
		
		
		


		System.out.println(r.getRecommendation(p));
	}
}