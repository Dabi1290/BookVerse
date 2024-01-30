<%@ page import="userManager.User" %><%--
  Created by IntelliJ IDEA.
  User: davideamoruso
  Date: 10/12/23
  Time: 18:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>NavBar</title>
    <link rel="stylesheet" href="<%= request.getContextPath()%>/assets/css/navbar.css">
</head>
<body>
<div class="navbar2">
    <a href="<%=request.getContextPath()%>/home">
        <img
                loading="lazy"
                src="<%=request.getContextPath()%>/assets/images/logo.png"
                class="img"
        />
    </a>


    <div class="navbar-right">
        <div class="ruolo-cambio" id="<%=((User)request.getSession().getAttribute("user")).getCurrentRole()%>"><%=((User)request.getSession().getAttribute("user")).getCurrentRole()%> <img src="<%=request.getContextPath()%>/assets/images/downArrow.png"></div>
        <a href="<%=request.getContextPath()%>/logout"><div class="logout-icon"><img src="<%=request.getContextPath()%>/assets/images/logout.png" alt="Pippo"></div></a>
    </div>
</div>



</body>
</html>
