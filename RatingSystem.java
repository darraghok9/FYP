import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

//	Darragh O'Keeffe
//	14702321
//	16 Jan 2018

public class RatingSystem {
	private DataReader dataReader;
	private ArrayList<Profile> profiles;
	private float[][] similarities;
	
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
	
	
	public int getRecommendation(Profile p){
		Map<Float,Profile> neighbours = getNeighbours(p,50);
		Map<Integer,Float> predictedRatings = new HashMap<Integer,Float>();
		
		for(Float key: neighbours.keySet()){
			ArrayList<Integer> itemsRated = neighbours.get(key).getItemsRated();
			for (Integer movie: itemsRated){
				predictedRatings.put(movie, getPredictedRating(p,movie,neighbours));
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
		return maxID;
	}
	
	
	public static void main(String[] args){
		DataReader dr = new DataReader(new File("ratingsSample.csv"));
		RatingSystem r = new RatingSystem(dr);
		
		Profile p = new Profile(0);
		
		p.addRating(2,3);
		p.addRating(29,3);
		p.addRating(32,3);
		p.addRating(47,3);
		p.addRating(50,3);
		p.addRating(112,3);
		p.addRating(151,4);
		p.addRating(223,4);
		p.addRating(253,4);
		p.addRating(260,4);
		p.addRating(293,4);
		p.addRating(296,4);
		p.addRating(318,4);
		p.addRating(337,3);
		p.addRating(367,3);
		p.addRating(541,4);
		p.addRating(589,3);
		p.addRating(593,3);
		p.addRating(653,3);
		p.addRating(919,3);
		p.addRating(924,3);
		p.addRating(1009,3);
		p.addRating(1036,4);
		p.addRating(1079,4);
		p.addRating(1080,3);
		p.addRating(1089,3);
		p.addRating(1090,4);
		p.addRating(1097,4);
		p.addRating(1136,3);
		p.addRating(1193,3);
		p.addRating(1196,4);
		p.addRating(1198,4);
		p.addRating(1200,4);
		p.addRating(1201,3);
		p.addRating(1208,3);
		p.addRating(1214,4);
		p.addRating(1215,4);
		p.addRating(1217,3);
		p.addRating(1219,4);
		p.addRating(1222,3);

		System.out.println(r.getRecommendation(p));
	}
	
}
	