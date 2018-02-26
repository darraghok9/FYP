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
<form name="ratingForm" action="trial" method="post">
	<input type="hidden" name="htmlFormName" value="ratingForm"/>
	<fieldset>
		<legend><%= request.getAttribute("NAME") %></legend>
		<p><img src=<%= request.getAttribute("POSTER") %> alt="Movie Poster"></p>
		<p><a href=<%= request.getAttribute("IMDB") %> target="_blank">View on IMDB</a></p>
		<p><button name="returnType" type="submit" value="reset">I haven't seen this movie</button></p>
		<p><select name="rating">
				<option value=0>Choose Rating</option>
				<option value=0.5>0.5</option>
				<option value=1.0>1</option>
				<option value=1.5>1.5</option>
				<option value=2.0>2</option>
				<option value=2.5>2.5</option>
				<option value=3.0>3</option>
				<option value=3.5>3.5</option>
				<option value=4.0>4</option>
				<option value=4.5>4.5</option>
				<option value=5.0>5</option>
		   </select>
		   <button name="returnType" type="submit" value="submit">Submit</button>
		</p>
	</fieldset>
</form>

</body>
</html>