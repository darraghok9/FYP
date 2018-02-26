import java.io.File;
import java.util.ArrayList;

//	Darragh O'Keeffe
//	14702321
//	16 Jan 2018

public class PreferenceSystem extends RecommenderSystem{
	
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

	public float getPredictedRating(Profile p, int movie, ArrayList<Float> similarities, ArrayList<Profile> neighbours){
		float numerator = 0, denominator=0;
		for (int index=0;index<similarities.size();index++){
			Profile q = neighbours.get(index);
			float similarity = similarities.get(index);
			if (q.hasRated(movie)){
				float rating = q.getPosition(movie);
				numerator += (similarity*rating);
				denominator += similarity;
			}
		}
		return (numerator/denominator);
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
	
		ArrayList<Integer> recommendations = r.getRecommendations(p, 10);
		for (Integer i:recommendations){
			System.out.println(i);
		}
	}
}