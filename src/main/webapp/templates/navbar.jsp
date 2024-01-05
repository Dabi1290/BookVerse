<%--
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
<div class="navbar">
    <a href="<%=request.getContextPath()%>/home">
        <img
                loading="lazy"
                src="<%=request.getContextPath()%>/assets/images/logo.png"
                class="img"
        />
    </a>
    <div class="search-bar">
        <div class="genre">Horror</div>
        <div class="search">Search in BookVerse</div>
        <div class="search-img">
            <img
                    loading="lazy"
                    src="https://cdn.builder.io/api/v1/image/assets/TEMP/1425ed4792740a08159922acf78c8e184bb8a0f780ca7cb61106f93d548b4406?"
                    class="img-2"
            />
        </div>
    </div>
    <div class="navbar-right">
       <a href="<%=request.getContextPath()%>/login.jsp"> <div class="sign-in">Sign in</div></a>
        <a href="<%=request.getContextPath()%>/register.jsp"><div class="sign-up">Sign up</div></a>
    </div>
</div>
</body>
</html>
