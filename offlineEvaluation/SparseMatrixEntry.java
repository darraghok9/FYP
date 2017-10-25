//	Darragh O'Keeffe
//	14702321
//	19 Oct 2017
package offlineEvaluation;

public class SparseMatrixEntry {
	private int row;
	private int column;
	private byte value;

	public SparseMatrixEntry(int r, int c, byte v){
		row = r;
		column = c;
		value = v;
	}
	
	public int getRow(){
		return row;
	}
	
	public int getColumn(){
		return column;
	}
	
	public byte getValue(){
		return value;
	}
	
	public int compareTo(SparseMatrixEntry e){
		if (row==e.getRow()){
			if (column<e.getColumn()){
				return -1;
			}
			if (column>e.getColumn()){
				return 1;
			}
		}
		if (row<e.getRow()){
			return -1;
		}
		if (row>e.getRow()){
			return 1;
		}
		return 0;
	}
}