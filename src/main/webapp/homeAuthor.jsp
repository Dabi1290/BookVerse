<%--
  Created by IntelliJ IDEA.
  User: davideamoruso
  Date: 05/01/24
  Time: 15:41
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
<%@include file="templates/underNavbarAuthor.jsp" %>

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
      Bring Your Story to Life!
      <br />
      <br />


    </span>
        <span class="description-text">
      Have you always dreamt of writing a book? Now you can do it easily. With
      our e-book publishing service, turn your words into a professional e-book.
      Share your creativity with the world. We are here to guide you every step
      of the way, from writing to publishing.
    </span>
    </div>
    <div class="publish-button">Start Publishing</div>
</div>
</body>
</html>
