import java.util.ArrayList;

//	Darragh O'Keeffe
//	14702321
//	9 Jan 2018

public class TrialProfile {
	private int ID;
	ArrayList<Integer> items;
	ArrayList<Preference> preferences;
	
	public TrialProfile(int userID){
		this.ID = userID;
		preferences = new ArrayList<Preference>();
		items = new ArrayList<Integer>();
	}
	
	public int getID(){
		return ID;
	}
	
	public void addPreference(Preference p){
		preferences.add(p);
		int itemsP[] = new int[2];
		itemsP[0] = p.getItemA().getID();
		itemsP[1] = p.getItemB().getID();
		if (items.isEmpty()){
			items.add(itemsP[0]);
			items.add(itemsP[1]);
		}else{
			for (int i=1;i<2;i++){
				if (!items.contains(itemsP[i])){
					int index=0;
					boolean added = false;
					while (index<items.size() && !added){
						if (itemsP[i]<items.get(index)){
							items.add(index, itemsP[i]);
							added = true;
						}
						index++;
					}
				}
			}
		}
	}
	
	public ArrayList<Integer> getCommonItems(Profile p){
		ArrayList<Integer> pItems = p.getItemsRated();
		ArrayList<Integer> commonItems = new ArrayList<Integer>();
		int i=0, j=0;
		float aSize = this.items.size();
		float bSize = pItems.size();
		
		while (i<aSize && j<bSize){
			if (this.items.get(i)>pItems.get(j)){
				j++;
			} else {
				if (this.items.get(i)<pItems.get(j)){
					i++;
				} else {
					commonItems.add(this.items.get(i));
					i++;
					j++;
				}
			}
		}
		return commonItems;
	}
	
	public ArrayList<Preference> getPreferences(){
		return preferences;
	}
	
	public int getSize(){
		return preferences.size();
	}
	
	public boolean contains(int movie){
		return items.contains(movie);
	}
	
}
