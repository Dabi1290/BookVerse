<%--
  Created by IntelliJ IDEA.
  User: samuelerusso
  Date: 13/01/24
  Time: 16:58
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>ErrorPage</title>
  <link rel="stylesheet" href="assets/css/home.css">
</head>
<body>
  <%@include file="navbar.jsp" %>

<div>

  <h2> Oppssss, qualcosa è andato storto. </h2>
   <a href="<%= request.getContextPath()%>/home">  <h2> Clicca </h2> </a> <h2> per ritornare alla home  </h2>

</div>



</body>
</html>
