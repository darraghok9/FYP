//	Darragh O'Keeffe
//	14702321
//	19 Oct 2017
package offlineEvaluation;


import java.util.ArrayList;

public class SparseMatrix {
	private ArrayList<Entry> entries;

	public SparseMatrix(){
		entries = new ArrayList<Entry>();
	}
	
	public class Entry{
		private int row;
		private int column;
		private byte value;
		
		public Entry(int r, int c, byte v){
			row = r;
			column = c;
			value = v;
		}
		
		public String toString(){
			return row+":"+column+":"+value;
		}
	}
	
	public void add(Entry e){
		int position = 0;
		if (entries.isEmpty()){
			entries.add(e);
		}else{
			boolean added = false;
			for (Entry i: entries){
				if (e.row<i.row || (e.row==i.row && e.column<i.column)){
					entries.add(position, e);
					added = true;
					break;
				}
				position++;
			}
			if (!added){
				entries.add(e);
			}
		}
	}
	
	public void add(int r, int c, byte v){
		add(new Entry(r, c, v));
	}
	
	public byte getValueAt(int r, int c){
		for (Entry e: entries){
			if(e.row==r && e.column==c){
				return e.value;
			}
		}
		return 0;
	}
	
	public String toString(){
		String s = "";
		for (Entry e: entries){
			s+=e.toString()+"\n";
		}
		return s;
	}
	
	public int size(){
		return entries.size();
	}
	
	public boolean isEmpty(){
		return entries.isEmpty();
	}
	
	public ArrayList<Entry> getEntries(){
		return entries;
	}
	
	public static void main(String[] args){
		SparseMatrix M = new SparseMatrix();
		M.add(3,2,(byte)1);
		M.add(0,0,(byte)1);
		M.add(0,7,(byte)-1);
		M.add(0,9,(byte)-1);
		M.add(1,2,(byte)0);
		M.add(1,6,(byte)0);
		M.add(1,9,(byte)1);
		M.add(3,4,(byte)-1);
		M.add(2,1,(byte)1);
		M.add(2,2,(byte)-1);
		M.add(2,4,(byte)0);
		M.add(0,4,(byte)0);
		M.add(2,8,(byte)1);
		M.add(3,0,(byte)0);
		M.add(3,6,(byte)1);
		System.out.println(M.toString());
		
	}
}	
