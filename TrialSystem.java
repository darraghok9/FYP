import java.util.ArrayList;

public class TrialSystem extends RecommenderSystem {
	
	public TrialSystem(ArrayList<Profile> profiles){
		this.profiles = profiles;
		similarities = new float[profiles.size()][profiles.size()];
	}
	
	public float computeSimilarity(TrialProfile a, Profile b){
		ArrayList<Integer> commonItems = a.getCommonItems(b);
		
		ArrayList<Float> bRatings = b.getRatingsFor(commonItems);
						
		double numerator = 0;
		ArrayList<Preference> preferences = a.getPreferences();
		for (Preference p: preferences){
			int itemA = p.getItemA().getID();
			int itemB = p.getItemB().getID();
			if (commonItems.contains(itemA) && commonItems.contains(itemB)){
				float itemARating = bRatings.get(commonItems.indexOf(itemA));
				float itemBRating = bRatings.get(commonItems.indexOf(itemB));
				if (Float.compare(itemARating, itemBRating) == p.getPreference()){
					numerator++;
				}
			}
		}
		float aSize = a.getSize();
		float bSize = (b.size()*(b.size()-1))/2;
		float similarity = (float) (numerator/(Math.sqrt(aSize*bSize)));
		return similarity;		
	}

	public float getPredictedRating(TrialProfile p, int movie, ArrayList<Float> similarities, ArrayList<Profile> neighbours){
		float numerator = 0, denominator=0;
		for (int index=0;index<similarities.size();index++){
			Profile q = neighbours.get(index);
			float similarity = similarities.get(index);
			if (q.hasRated(movie)){
				float rating = q.getRating(movie);
				numerator += (similarity*rating);
				denominator += similarity;
			}
		}
		return (numerator/denominator);
	}
	
	public ArrayList<Float> getSimilarities(TrialProfile p){
		ArrayList<Float> similarities = new ArrayList<Float>();
		for (Profile q: profiles){
			similarities.add(computeSimilarity(p,q));
		}
		return similarities;
	}
	
	public ArrayList<Integer> getRecommendations(TrialProfile p, int N){
		ArrayList<Float> similarities = getSimilarities(p);
		
		ArrayList<Profile> neighbours = getTopN(similarities, profiles, NEIGHBOURHOOD_SIZE);
		similarities = getTopN(similarities, similarities, NEIGHBOURHOOD_SIZE);
		
		ArrayList<Float> predictedRatings = new ArrayList<Float>();
		ArrayList<Integer> neighbourhoodMovies = new ArrayList<Integer>();
		
		for (Profile n: neighbours){
			ArrayList<Integer> movies = n.getItemsRated();
			for (Integer m: movies){
				if (!p.contains(m) && !neighbourhoodMovies.contains(m)){
					neighbourhoodMovies.add(m);
					predictedRatings.add(getPredictedRating(p,m,similarities,neighbours));
				}
			}
		}
		return getTopN(predictedRatings,neighbourhoodMovies,N);
	}
	
}
