<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Ibt Java Web Task</title>
</head>
<body>
<form action='WelcomeServlet'>
  <button>Welcome</button>
</form>
<br>
<form action="XmlParserServlet" method="post" enctype="multipart/form-data">
    <input type="submit" value="Parse XML">
</form>
</body>
</html>