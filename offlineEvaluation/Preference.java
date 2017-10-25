//	Darragh O'Keeffe
//	14702321
//	15 Oct 2017

package offlineEvaluation;

public class Preference {
	private int itemA;
	private int itemB;
	private byte preference;
	
	public Preference(int firstItem, int secondItem, byte p){
		itemA = firstItem;
		itemB = secondItem;
		if (-1>=p && p<=1){
			preference = p;
		}else{
			preference = 0;
		}
	}
	
	public boolean equals(Preference p){
		if (this.itemA==p.itemA && this.itemB==p.itemB && this.preference==p.preference){
			return true;
		}
		return false;
	}
	
	public boolean lessThan(Preference p){
		if (this.itemA<p.itemA){
			return true;
		}
		if (this.itemA==p.itemA && this.itemB<p.itemB){
			return true;
		}
		return false;
	}
	
}
