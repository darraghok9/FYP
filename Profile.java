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
	
	public double getPosition(int itemID){
		double higher=0, lower=0, equal=-1;
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
	
	public double computeSimilarity(Profile q){
		double similarity = 0;
		for (int index=0;index<items.size();index++){
			int item = items.get(0);
			if (q.hasRated(item)){
				float qRating = q.getRating(item);
				if (qRating==ratings.get(index)){
					similarity++;
				}
			}
		}
		return similarity;
	}
	
	public ArrayList<Integer> getCommonItems(Profile q){
		// TODO
		return null;
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
		System.out.println(p.computeMeanRating());
	}
}

