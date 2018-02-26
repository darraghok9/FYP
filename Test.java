import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class Test {

	public static void main(String[] args) {
		String TMDB_API_KEY = "ae7bb1214130c1f573bd48bfd4660d0d";
		URL url;
		try {
			url = new URL("http://api.themoviedb.org/3/movie/550?api_key="+TMDB_API_KEY);
			System.out.println(url.toString());
			 HttpURLConnection con = (HttpURLConnection) url.openConnection();
	            con.setDoOutput(true);
	            con.setRequestMethod("GET");
	            con.setRequestProperty("Content-Type", "application/json");
	            

	            BufferedReader br = new BufferedReader(new InputStreamReader((con.getInputStream())));
	            String output = br.readLine();
	            System.out.println(output);
	            String[] splits = output.split("poster_path\":\"");
	            String[] splits2 = splits[1].split("\"");
	            String path = splits2[0];
	            System.out.println(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
