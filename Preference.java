//	Darragh O'Keeffe
//	14702321
//	9 Jan 2018

public class Preference {
	private Movie itemA;
	private Movie itemB;
	private byte preference;
	
	public Preference(Movie itemA, Movie itemB, byte preference){
		this.itemA = itemA;
		this.itemB = itemB;
		this.preference = preference;
	}
	
	public Movie getItemA(){
		return itemA;
	}
	
	public Movie getItemB(){
		return itemB;
	}
	
	public byte getPreference(){
		return preference;
	}
	
	public String toString(){
		return itemA.toString()+" "+itemB.toString()+" "+preference;
	}
}
