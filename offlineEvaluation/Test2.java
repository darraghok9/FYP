package offlineEvaluation;

public class Test2 {
	public static void main(String[] args){
		Profile p = new Profile(123);
		Profile q = new Profile(456);
		Preference a = new Preference(1,2,(byte)1);
		Preference b = new Preference(3,4,(byte)0);
		Preference c = new Preference(2,3,(byte)-1);
		Preference d = new Preference(1,4,(byte)0);
		p.addPreference(a);
		p.addPreference(b);
		p.addPreference(c);
		q.addPreference(a);
		q.addPreference(b);
		q.addPreference(d);
		System.out.println(p.computeSimilarity(q));
	}
}
