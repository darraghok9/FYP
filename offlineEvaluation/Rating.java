//	Darragh O'Keeffe
//	14702321
//	17 Oct 2017
package offlineEvaluation;

public class Rating {
	private int userID;
	private int itemID;
	private double rating;
	
	public Rating(int userID, int itemID, double rating){
		this.userID = userID;
		this.itemID = itemID;
		this.rating = rating;
	}
	
	public int getUserID(){
		return userID;
	}
	
	public int getItemID(){
		return itemID;
	}
	
	public double getRating(){
		return rating;
	}
	
	public String toString(){
		return userID+":"+itemID+":"+rating;
	}
}
