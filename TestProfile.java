import java.util.ArrayList;

//	Darragh O'Keeffe
//	14702321
//	9 Jan 2018

public class TestProfile {
	private int ID;
	ArrayList<Preference> preferences;
	
	public TestProfile(int userID){
		this.ID = userID;
		preferences = new ArrayList<Preference>();
	}
	
	public int getID(){
		return ID;
	}
	
	public void addPreference(Preference p){
		preferences.add(p);
	}
	
	public ArrayList<Integer> getCommonItems(Profile p){
		// TODO
		return null;
	}
}
