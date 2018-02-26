

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


/**
 * Servlet implementation class TrialServlet
 */
@WebServlet("/TrialServlet")
public class TrialServlet extends HttpServlet {
	private static final String TMDB_API_KEY = "ae7bb1214130c1f573bd48bfd4660d0d";
	private static final long serialVersionUID = 1L;
	private static final int MIN_PROFILE_SIZE = 2;
	private static final int MIN_PROFILE_ID = 138494;
	private ArrayList<Movie> testMovies;
	private ArrayList<Preference> testPreferences;
	private ArrayList<Movie> movieDetails;
	
	private Profile p;
	private TrialProfile tp;
	private Movie lastMovie;
	private Preference lastPreference;
	private boolean ratingsFirst;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TrialServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int id = (int) (Math.random()*100000) + MIN_PROFILE_ID;
        p = new Profile(id);
        tp = new TrialProfile(id);
        System.out.println(p.toRatingString());
		String tomcatRoot = getServletContext().getRealPath("/");
		
		String sourceFile = tomcatRoot + "/WEB-INF/testMovies.csv";
		File trialMovies = new File(sourceFile);
		sourceFile = tomcatRoot + "/WEB-INF/movies.csv";
		File details = new File(sourceFile);
		sourceFile = tomcatRoot + "/WEB-INF/testPairs.csv";
		File trialPreferences = new File(sourceFile);
		
		DataReader dr = new DataReader();
		
		testPreferences = dr.getTestPreferences(trialPreferences, details);
		
		ArrayList<Integer> ids = dr.getTestMovieIDs(trialMovies);
		movieDetails = dr.getMovieDetails(details);
		testMovies = new ArrayList<Movie>();
				
		for (Movie m:movieDetails) {
			if (ids.contains(m.getID())) {
				testMovies.add(m);
			}
		}
		
		HttpSession ssn = request.getSession();
		System.out.println(ssn.getId());
		
		ratingsFirst = (Math.random() < 0.5);
		
		if (ratingsFirst) {
			doRating(request,response);
		} else {
			doPreference(request,response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String s = request.getParameter("htmlFormName");
		if (s.equals("ratingForm")){
			doRating(request,response);
		}
		if (s.equals("preferenceForm")){
			doPreference(request,response);
		}
		HttpSession ssn = request.getSession();
		System.out.println(ssn.getId());
	}
	
	protected void doRating(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Map<String,String[]> parameters = request.getParameterMap();
		Set<String> keys = parameters.keySet();
		if (keys.contains("returnType") && request.getParameter("htmlFormName").equals("ratingForm")) {
			if(request.getParameter("returnType").equals("submit")){
				float rating = Float.valueOf(request.getParameter("rating"));
				p.addRating(lastMovie.getID(), rating);
			}
		}
		if (p.size()>=MIN_PROFILE_SIZE) {
			if (ratingsFirst) {
				doPreference(request,response);
			} else {
				doRecommendations(request,response);
			}
		} else {
			int rand = (int) (Math.random()*(testMovies.size()-1));
			lastMovie = testMovies.get(rand);
			testMovies.remove(rand);
			
			request.setAttribute("NAME", lastMovie.getName()); 
			request.setAttribute("IMDB", getImdbLink(lastMovie.getIMDBid()));
			request.setAttribute("POSTER", getMoviePosterLink(lastMovie.getTMDBid()));
			
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/GetRating.jsp"); 
			dispatcher.include(request, response);
		}
		
	}
	
	protected void doPreference(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Map<String,String[]> parameters = request.getParameterMap();
		Set<String> keys = parameters.keySet();
		if (keys.contains("returnType") && request.getParameter("htmlFormName").equals("preferenceForm")) {
			if(request.getParameter("returnType").equals("submit")){
				byte preference = Byte.valueOf(request.getParameter("preference"));
				lastPreference.setPreference(preference);
				tp.addPreference(lastPreference);
			}
		}
		
		if (tp.getSize()>=MIN_PROFILE_SIZE) {
			if (ratingsFirst) {
				doRecommendations(request,response);
			} else {
				doRating(request,response);
			}
		} else {
			int rand = (int) (Math.random()*(testPreferences.size()-1));
			lastPreference = testPreferences.get(rand);
			testPreferences.remove(rand);
			
			Movie m = lastPreference.getItemA();
			Movie n = lastPreference.getItemB();

			
			request.setAttribute("NAME1", m.getName()); 
			request.setAttribute("IMDB1", getImdbLink(m.getIMDBid()));
			request.setAttribute("POSTER1", getMoviePosterLink(m.getTMDBid()));
			
			request.setAttribute("NAME2", n.getName()); 
			request.setAttribute("IMDB2", getImdbLink(n.getIMDBid()));
			request.setAttribute("POSTER2", getMoviePosterLink(n.getTMDBid()));
			
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/GetPreference.jsp"); 
			dispatcher.include(request, response);
		}
	}
	
	protected void doRecommendations(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String tomcatRoot = getServletContext().getRealPath("/");
		
		String sourceFile = tomcatRoot + "/WEB-INF/ratingsSample.csv";
		File ratings = new File(sourceFile);
		DataReader dr = new DataReader();
		ArrayList<Profile> profiles = dr.getProfiles(ratings);
		RatingSystem rs = new RatingSystem(profiles);
		TrialSystem ts = new TrialSystem(profiles);
		ArrayList<Integer> ratingRecommendationIds = rs.getRecommendations(p, 10);
		ArrayList<Integer> preferenceRecommendationIds = ts.getRecommendations(tp, 10);
		ArrayList<Movie> ratingRecommendations = new ArrayList<Movie>();
		ArrayList<Movie> preferenceRecommendations = new ArrayList<Movie>();
		for (Movie m: movieDetails) {
			if (ratingRecommendationIds.contains(m.getID())) {
				ratingRecommendations.add(m);
			}
			if (preferenceRecommendationIds.contains(m.getID())) {
				preferenceRecommendations.add(m);
			}
		}
		ServletOutputStream out = response.getOutputStream();
		out.println("<html><link rel = \"stylesheet\" href=\"/UserTrial/FormStyle.css\" type=\"text/css\">");
	    out.println("<head><title>Pairwise Preferences in Recommender Systems</title></head>");
	    out.println("<body><table class=\"centerTable\"><tr>");
	    out.println("<td><h3>"+p.size()+"</h3></td><td></td><td><h3>"+tp.getSize()+"</h3></td></tr>");
	    out.println("<td><h3>Rating Recommendations</h3></td><td></td><td><h3>Preference Recommendations</h3></td></tr>");
	    for (int i=0;i<10;i++) {
	    	Movie m = ratingRecommendations.get(i);
	    	Movie n = preferenceRecommendations.get(i);
	    	out.println("<tr><td><fieldset><legend>"+m.getName()+"</legend>");
			out.println("<p><img src=\""+getMoviePosterLink(m.getTMDBid())+"\" alt=\"Movie Poster\"></p>");
			out.println("<p><a href=\""+ getImdbLink(m.getIMDBid()) +"\" target=\"_blank\">View on IMDB</a></p>");
			out.println("</fieldset></td>");
			
			out.println("<td></td><td><fieldset><legend>"+n.getName()+"</legend>");
			out.println("<p><img src=\""+getMoviePosterLink(n.getTMDBid())+"\" alt=\"Movie Poster\"></p>");
			out.println("<p><a href=\""+ getImdbLink(n.getIMDBid()) +"\" target=\"_blank\">View on IMDB</a></p>");
			out.println("</fieldset></td></tr>");
	    }
	    out.println("</table><p><a href=\"https://goo.gl/forms/5ikxKIAxYsQf7K3y2\" target=\"_blank\">Continue to Survey</a></p>");
	    out.println("</body></html>");
		
	}
	
	private String getImdbLink(int imdbId) {
		String IMDBid = String.valueOf(imdbId);
		IMDBid = IMDBid.trim();
		while (IMDBid.length()<7) {
			IMDBid = "0"+IMDBid;
		}
		return "https://www.imdb.com/title/tt"+IMDBid+"/";
	}

	private String getMoviePosterLink(int tmdbId) {
		URL url;
		String path = "";
		try {
			url = new URL("http://api.themoviedb.org/3/movie/"+String.valueOf(tmdbId)+"?api_key="+TMDB_API_KEY);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
	        con.setDoOutput(true);
	        con.setRequestMethod("GET");
	        con.setRequestProperty("Content-Type", "application/json");
	           
	        BufferedReader br = new BufferedReader(new InputStreamReader((con.getInputStream())));
	        String output = br.readLine();
	        String[] splits = output.split("poster_path\":\"");
	        String[] splits2 = splits[1].split("\"");
	        path = splits2[0];
		} catch (IOException e) {
			e.printStackTrace();
		}
        return "https://image.tmdb.org/t/p/w185" + path;
	}

}
