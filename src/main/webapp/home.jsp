<%--
  Created by IntelliJ IDEA.
  User: davideamoruso
  Date: 10/12/23
  Time: 16:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="assets/css/home.css">
</head>
<body>
<div class="div">
    <%@include file="templates/navbar.jsp" %>
    <div class="buy-preview">
        <img
                loading="lazy"
                src="<%= request.getContextPath()%>/assets/images/buy.png"
                class="img-4"
        />
        <div class="buy-text">
      <span>
        Welcome to BookVerse!
        <br />
        <br />
      </span>
            <span class="description-text"
            >
        Explore a vast library of e-books in every genre imaginable.
        <br />
        From bestsellers to obscure titles, you'll find your next favorite read
        here. Choose from a variety of formats and read anywhere, on any device.
        Discover the future of reading with our selection of high-quality
        e-books.
      </span>
        </div>
        <div class="buy-button">Explore Now</div>
    </div>
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
</div>



</body>
</html>
