import java.util.ArrayList;
import java.util.HashMap;

public abstract class RecommenderSystem {
	protected ArrayList<Profile> profiles;
	protected float[][] similarities;
	
	protected static final int NEIGHBOURHOOD_SIZE = 50;
	
	
	public float computeSimilarity(Profile a, Profile b){
		return 0;
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
	
	public ArrayList<Float> getSimilarities(Profile p){
		ArrayList<Float> similarities = new ArrayList<Float>();
		for (Profile q: profiles){
			similarities.add(computeSimilarity(p,q));
		}
		return similarities;
	}
	
	// TODO change return type to AL<float> similarities for p->profiles
	public HashMap<Float,Profile> getNeighbours(Profile p, int k){
		HashMap<Float, Profile> map = new HashMap<Float, Profile>();
		
		ArrayList<Profile> neighbours = new ArrayList<Profile>();
		ArrayList<Float> similarities = new ArrayList<Float>();
		for (int index=0;index<profiles.size();index++){
			Profile q = profiles.get(index);
			similarities.add(index,computeSimilarity(p, q));
			
		}
		for (int index=0;index<similarities.size();index++){
			map.put(similarities.get(index), neighbours.get(index));
		}
		return map;
	}
	
	public float getPredictedRating(Profile p, int movie, ArrayList<Float> similarities, ArrayList<Profile> neighbours){
		return 0;
	}
	
	protected <S,T extends Comparable<T>> ArrayList<S> getTopN(ArrayList<T> values, ArrayList<S> objects, int N){
		if (objects.size()<=1){
			return objects;
		}
		ArrayList<S> o = new ArrayList<S>();
		ArrayList<T> v = new ArrayList<T>();
		if (values.get(0).compareTo(values.get(1))>0){
			o.add(objects.get(0));
			o.add(objects.get(1));
			v.add(values.get(0));
			v.add(values.get(1));
		}else{
			o.add(objects.get(1));
			o.add(objects.get(0));
			v.add(values.get(1));
			v.add(values.get(0));
		}
		for (int i=2;i<objects.size();i++){
			int size = v.size();
			T value = values.get(i);
			if (value.compareTo(v.get(size-1))>0){
				int k=0;
				boolean added = false;
				while (k<size && !added){
					if (value.compareTo(v.get(k))>0){
						v.add(k,value);
						o.add(k,objects.get(i));
						if (v.size()>N){
							v.remove(N);
							o.remove(N);
						}
						added = true;
					}
					k++;
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
	
	public ArrayList<Integer> getRecommendations(Profile p, int N){
		ArrayList<Float> similarities = getSimilarities(p);
		
		ArrayList<Profile> neighbours = getTopN(similarities, profiles, NEIGHBOURHOOD_SIZE);
		similarities = getTopN(similarities, similarities, NEIGHBOURHOOD_SIZE);
		
		ArrayList<Float> predictedRatings = new ArrayList<Float>();
		ArrayList<Integer> neighbourhoodMovies = new ArrayList<Integer>();
		
		for (Profile n: neighbours){
			ArrayList<Integer> movies = n.getItemsRated();
			for (Integer m: movies){
				if (!p.hasRated(m) && !neighbourhoodMovies.contains(m)){
					neighbourhoodMovies.add(m);
					predictedRatings.add(getPredictedRating(p,m,similarities,neighbours));
				}
			}
		}
		return getTopN(predictedRatings,neighbourhoodMovies,N);
	}
}
