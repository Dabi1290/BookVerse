<%--
  Created by IntelliJ IDEA.
  User: davideamoruso
  Date: 05/01/24
  Time: 16:34
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Home</title>
    <link rel="stylesheet" href="<%= request.getContextPath()%>/assets/css/home.css">
</head>
<body>
<%@include file="templates/navbarLogged.jsp" %>
<%@include file="templates/underNavbarValidator.jsp" %>

<div class="publish-preview">
    <img
            loading="lazy"
            srcset="<%= request.getContextPath()%>/assets/images/publish.jpg"
            class="img-5"
    />
    <div class="publish-text">
        <span
                style="
        font-family: Comfortaa, sans-serif;
        font-weight: 700;
        z-index: 1;
      "
        >
      Empowering Knowledge: One Page at a Time
      <br />
      <br />


    </span>
        <span class="description-text">
      “Every page you validate today, brings the world one book closer to universal access to knowledge. Keep going!” 📚💪
    </span>
    </div>
    <div class="publish-button"><a href="/Proposals">Work now</a></div>
</div>
</body>
</html>
