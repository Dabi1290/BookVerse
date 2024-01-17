<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: davideamoruso
  Date: 06/01/24
  Time: 15:24
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>ProposalForm</title>
    <link rel="stylesheet" href="assets/css/proposal.css">
    <script src="assets/js/jquery-3.7.1.min.js"></script>
    <script src="assets/js/fileUpload.js"></script>
    <script src="assets/js/searchAuthor.js"></script>


</head>
<body>
<%@include file="templates/navbarLogged.jsp" %>
<%@include file="templates/underNavbarAuthor.jsp" %>
<% User user = (User) request.getSession().getAttribute("user");%>
    <form class="proposal-form" action="/ProposalCreation" method="post" enctype="multipart/form-data">    <!-- Start form -->
        <label for="title" class="label">
            Title
            <input type="text" placeholder="Title" id="title" name="title">  <!-- input titolo -->
        </label>

        <div class="ebook-inf" >
            <label for="price" class="label">
                Price
                <input type="text" placeholder="25" id="price" name="price"> <!-- input prezzo -->
            </label>

            <label for="e-book" class="label">
                E-Book

                <div class="input-container" >
                    <img src="assets/images/upload.png" class="icon" id="e-book-icon">
                    <input type="file" id="e-book" class="input-file" name="ebookFile"> <!-- input pdf -->
                    <div class="labeling" id="upload-phrase-file">Click to upload</div>
                    <button class="button" id="e-book-button">PDF</button>
                    <span id="upload-check-file">
                        <img src="assets/images/greenCheck.png">
                        <span id="upload-text-file"></span>
                    </span>
                </div>
            </label>
            <label for="cover" class="label">
                Cover
                <div class="input-container">
                    <img src="assets/images/upload.png" class="icon" id="cover-icon">
                    <input type="file" id="cover" class="input-file" name="coverImage"> <!-- input copertina -->
                    <div class="labeling" id="upload-phrase-cover"> Click to upload</div>
                    <button class="button" id="cover-button">PNG</button>
                    <span id="upload-check-cover">
                        <img src="assets/images/greenCheck.png">
                        <span id="upload-text-cover"></span>
                    </span>
                </div>
            </label>
        </div>

        <label class="label">
            Author
            <div class="author-row">
                <select name="authors" multiple id="authors">

                </select><!-- input per mantenere la lista di auth -->
                <div class="author-insert">
                <input type="text" id="searchBox" placeholder="Search..." onkeyup="searchProducts('<%=user.getEmail()%>')"> <!-- ricerca per gli auth -->
                <!--  <input type="text" id="author" placeholder="Find">-->
                <ul id="suggestions">

                </ul>
                </div>
                <ul id="effective-authors">

                </ul>
            </div>


        </label>
        <label class="label">
            Genres
            <div class="genre-grid">

                <%
                    ServletContext context=request.getServletContext();
                    List<String> genres= (List<String>) context.getAttribute("Genres");
                    if(genres!=null){
                        for(String s: genres){%>
                        <label for=<%=s%>>
                            <input type="checkbox" value="<%=s%>" id="<%=s%>" name="genres" >
                            <%=s%>
                        </label>

                <%
                }}
                %>

            </div>
        </label>
        <input type="hidden" value="ops" name="description" >
        <input type="submit" value="Send" class="proposal-send">

    </form>
</body>
</html>
