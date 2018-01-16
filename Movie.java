//	Darragh O'Keeffe
//	14702321
//	9 Jan 2018

import java.util.ArrayList;

public class Movie {
	
	private int ID;
	private ArrayList<Integer> users;
	private ArrayList<Float> ratings;
	private String name;
	private int IMDBid;
	private int TMDBid;
	
	private float averageRating;
	private boolean validValue;
	
	public Movie(int ID){
		this.ID = ID;
		users = new ArrayList<Integer>();
		ratings = new ArrayList<Float>();
		averageRating=0;
		validValue=false;
	}
	
	public Movie(int id, int IMDBid, int TMDBid, String name){
		this.ID = id;
		this.name = name;
		this.IMDBid = IMDBid;
		this.TMDBid = TMDBid;
		users = new ArrayList<Integer>();
		ratings = new ArrayList<Float>();
		averageRating=0;
		validValue=false;
	}
	
	public int getID(){
		return ID;
	}
	
	public boolean wasRatedBy(int userID){
		return users.contains(userID);
	}
	
	public float getRating(int userID){
		int index = users.indexOf(userID);
		return ratings.get(index);
	}
	
	public void addRating(int userID, float rating){
		users.add(userID);
		ratings.add(rating);
		validValue = false;
	}
	
	public ArrayList<Integer> getUsersWhoRated(){
		return users;
	}
	
	public ArrayList<Float> getRatings(){
		return ratings;
	}
	
	public float getAverageRating(){
		if (validValue){
			return averageRating;
		}
		float size = (float) ratings.size();
		float sum = 0;
		for (int index=0;index<size;index++){
			sum += ratings.get(index);
		}
		validValue = true;
		averageRating = sum/size;
		return averageRating;
	}
	
	public int getSize(){
		return ratings.size();
	}
	
	public String getName(){
		return name;
	}
	
	public int getIMDBid(){
		return IMDBid;
	}
	
	public int getTMDBid(){
		return TMDBid;
	}
	
	public String toString(){
		return (ID+" "+name);
	}
		
}


