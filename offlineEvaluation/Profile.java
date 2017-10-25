//	Darragh O'Keeffe
//	14702321
//	15 Oct 2017

package offlineEvaluation;

import java.util.ArrayList;

public class Profile {
	private int userID;
	private ArrayList<Preference> preferences;
	
	public Profile(int id){
		userID = id;
		preferences = new ArrayList<Preference>();
	}
	
	
	public void addPreference(Rating r, Rating t){
		double a = r.getRating();
		double b = t.getRating();
		if (a>b){
			addPreference(new Preference(r.getItemID(), t.getItemID(), (byte) 1));
		}
		if (a==b){
			addPreference(new Preference(r.getItemID(), t.getItemID(), (byte) 0));
		}
		if (a<b){
			addPreference(new Preference(r.getItemID(), t.getItemID(), (byte) -1));
		}
	}
	
	public void addPreference(Preference p){
		if (preferences.isEmpty()){
			preferences.add(p);
			return;
		}
		if (preferences.get(preferences.size()-1).lessThan(p)){
			preferences.add(p);
		}else{
			int index = 0;
			for (Preference q: preferences){
				if(p.lessThan(q)){
					preferences.add(index, p);
					break;
				}
				index++;
			}
		}
	}
	
	/*
	private ArrayList<Preference> getCommonPreferences(Profile p){
		ArrayList<Preference> commonPreferences = new ArrayList<Preference>();
		ArrayList<Preference> prefP = p.getPreferences();
		for (Preference q: preferences){
			if (prefP.contains(q)){
				commonPreferences.add(q);
			}
		}
		return commonPreferences;
	}
	*/
	private int getCommonPreferencesSize(Profile p){
		ArrayList<Preference> prefP = p.getPreferences();
		int commonPreferences = 0;
		for (Preference q: preferences){
			if (prefP.contains(q)){
				commonPreferences++;
			}
		}
		return commonPreferences;
	}
	
	
	private ArrayList<Preference> getPreferences(){
		return preferences;
	}
	
	public double computeSimilarity(Profile p){
		double commonPreferences = (double) this.getCommonPreferencesSize(p);
		double normalization = Math.sqrt((double) p.size() * (double) this.size()); 
		return commonPreferences/normalization;
	}
	/*
	public boolean hasRated(int itemA, int itemB){
		if ()
	}
	*/
	public int size(){
		return preferences.size();
	}
	
	/*
	private ArrayList<Preference> getNeighbours(int k, double t){
		return
	}
	*/

	
	public String toString(){
		String s = userID+" has "+preferences.size()+" preferences";
		
		return s;
	}
}
