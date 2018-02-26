//	Darragh O'Keeffe
//	14702321
//	9 Jan 2018

import java.util.ArrayList;

public class Profile {
	
	private int ID;
	private ArrayList<Integer> items;
	private ArrayList<Float> ratings;
	
	private double ratingDistribution;
	private boolean validValue;
	
	public Profile(int ID){
		this.ID = ID;
		items = new ArrayList<Integer>();
		ratings = new ArrayList<Float>();
		ratingDistribution=0;
		validValue=false;
	}
	
	public int getID(){
		return ID;
	}
	
	public boolean hasRated(int itemID){
		return items.contains(itemID);
	}
	
	public float getRating(int itemID){
		int index = items.indexOf(itemID);
		return ratings.get(index);
	}
	
	public float getPosition(int itemID){
		float higher=0, lower=0, equal=-1;
		float itemRating = ratings.get(items.indexOf(itemID));
		for (int index=0;index<ratings.size();index++){
			int comparison = Float.compare(itemRating,ratings.get(index));
			if (comparison>0){
				lower++;
			}else{
				if (comparison<0){
					higher++;
				}else{
					equal++;
				}
			}
		}
		return (-higher+lower)/(higher+lower+equal);
	}
	
	public void addRating(int itemID, float rating){
		items.add(itemID);
		ratings.add(rating);
		validValue = false;
	}
	
	public ArrayList<Integer> getItemsRated(){
		return items;
	}
	
	public ArrayList<Float> getRatings(){
		return ratings;
	}
	
	public double getRatingDistribution(){
		if (validValue){
			return ratingDistribution;
		}
		double sum = 0;
		for (int index=0;index<ratings.size();index++){
			sum += Math.pow(ratings.get(index),2);
		}
		validValue = true;
		ratingDistribution = Math.sqrt(sum);
		return ratingDistribution;
	}
	
	public double computeMeanRating(){
		double sum = 0;
		for (int index=0;index<ratings.size();index++){
			sum += ratings.get(index);
		}
		return (sum/ratings.size());
	}
	
	public ArrayList<Float> getRatingsFor(ArrayList<Integer> movies){
		ArrayList<Float> ratingsFor = new ArrayList<Float>();
		int i=0, j=0;
		while (i<items.size() && j<movies.size()){
			if (items.get(i)>movies.get(j)){
				j++;
			} else {
				if (items.get(i)<movies.get(j)){
					i++;
				} else {
					ratingsFor.add(ratings.get(i));
					i++;
					j++;
				}
			}
		}
		return ratingsFor;
	}
	
	public int size(){
		return items.size();
	}
	
	public int numberOfMoviesRated(ArrayList<Movie> movies){
		int i=0, j=0, count=0;
		while (i<items.size() && j<movies.size()){
			if (items.get(i)>movies.get(j).getID()){
				j++;
			} else {
				if (items.get(i)<movies.get(j).getID()){
					i++;
				} else {
					count++;
					i++;
					j++;
				}
			}
		}
		return count;
	}
	
	public ArrayList<Integer> getCommonItems(Profile b){
		ArrayList<Integer> bItems = b.getItemsRated();
		ArrayList<Integer> commonItems = new ArrayList<Integer>();
		int i=0, j=0;
		float aSize = this.items.size();
		float bSize = bItems.size();
		
		while (i<aSize && j<bSize){
			if (this.items.get(i)>bItems.get(j)){
				j++;
			} else {
				if (this.items.get(i)<bItems.get(j)){
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
	
	public String toRatingString(){
		String s = "";
		for (int index=0;index<items.size();index++){
			s += (ID+","+items.get(index)+","+ratings.get(index)+"\n");
		}
		return s;
	}
	
	public static void main(String[] args){
		Profile p = new Profile(1);
		p.addRating(0, 1);
		p.addRating(1, 2);
		p.addRating(2, 5);
		p.addRating(3, 3);
		p.addRating(4, 4);
		p.addRating(5, 4);
		p.addRating(6, 3);
		p.addRating(7, 3);
		p.addRating(8, 5);
		p.addRating(9, 2);
		p.addRating(10, 4);
		p.addRating(11, 3);
		p.addRating(12, 5);
		p.addRating(13, 3);
		p.addRating(14, 3);
		p.addRating(15, 2);
		p.addRating(16, 1);
		p.addRating(17, 4);
		p.addRating(18, 4);
		p.addRating(19, 3);
		p.addRating(20, 2);
		System.out.println(p.toString());
	}
}


