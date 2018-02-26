<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<link rel ="stylesheet" href="/UserTrial/FormStyle.css" type="text/css">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Pairwise Preferences in Recommender Systems</title>
</head>
<body>
<form name="preferenceForm" action="trial" method="post">
	<input type="hidden" name="htmlFormName" value="preferenceForm"/>
	<div><table class="centerTable">
		<tr>
			<td>
				<fieldset>
					<legend><%= request.getAttribute("NAME1") %></legend>
						<p><img src=<%= request.getAttribute("POSTER1") %> alt="Movie Poster"></p>
						<p><a href=<%= request.getAttribute("IMDB1") %> target="_blank">View on IMDB</a></p>
						<p><button name="returnType" type="submit" value="reset">I haven't seen this movie</button></p>
				</fieldset>
			</td>
			<td>
				<fieldset>
					<legend><%= request.getAttribute("NAME2") %></legend>
						<p><img src=<%= request.getAttribute("POSTER2") %> alt="Movie Poster"></p>
						<p><a href=<%= request.getAttribute("IMDB2") %> target="_blank">View on IMDB</a></p>
						<p><button name="returnType" type="submit" value="reset">I haven't seen this movie</button></p>
				</fieldset>
			</td>		
		</tr>
		<tr>
			<td>
				<p><select name="preference">
						<option value=0>Choose Preference</option>
						<option value=1><%= request.getAttribute("NAME1") %></option>
						<option value=-1><%= request.getAttribute("NAME2") %></option>
						<option value=0>Equal Preference</option>
		   			</select></p>
		   			<button name="returnType" type="submit" value="submit">Submit</button>
			</td>
		</tr>
	</table></div>
</form>
</body>
</html>