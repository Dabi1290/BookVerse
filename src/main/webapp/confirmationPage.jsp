<%--
  Created by IntelliJ IDEA.
  User: davideamoruso
  Date: 05/01/24
  Time: 16:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Confirmation Page</title>
    <link rel="stylesheet" href="<%= request.getContextPath()%>/assets/css/home.css">
</head>
<body>
<%@include file="templates/navbarLogged.jsp" %>
<%@include file="templates/underNavbarAuthor.jsp" %>
<div class="confirm-body">
    <img src="<%= request.getContextPath()%>/assets/images/bigCheck.png">
    <div class="confirm-msg">Transaction failed! Something went wrong</div>
    <div class="confirm-button">Torna alla Home</div>
</div>
</body>
</html>
