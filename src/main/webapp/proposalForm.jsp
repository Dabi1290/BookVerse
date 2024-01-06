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
</head>
<body>
<%@include file="templates/navbarLogged.jsp" %>
<%@include file="templates/underNavbarAuthor.jsp" %>
    <div class="proposal-form">
        <label for="title" class="label">
            Title
            <input type="text" placeholder="Title" id="title">
        </label>

        <div class="ebook-inf" >
            <label for="price" class="label">
                Price
                <input type="text" placeholder="25" id="price">
            </label>

            <label for="e-book" class="label">
                E-Book

                <div class="input-container">
                    <img src="assets/images/upload.png" class="icon">
                    <input type="file" id="e-book" class="input-file">
                    <div class="labeling">Click to upload</div>
                    <button class="button">PDF</button>
                </div>
            </label>
            <label for="cover" class="label">
                Cover
                <div class="input-container">
                    <img src="assets/images/upload.png" class="icon">
                    <input type="file" id="cover" class="input-file">
                    <div class="labeling">Click to upload</div>
                    <button class="button">PNG</button>
                </div>
            </label>
        </div>

        <label class="label">
            Author
            <div class="author-row">
                <input type="text" id="author" placeholder="Find">
                <ul>
                    <li>Pippow</li>
                    <li>Pippow</li>
                    <li>Pippow</li>
                    <li>Pippow</li>
                    <li>Pippow</li>
                    <li>Pippow</li>
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
                            <input type="checkbox" value="<%=s%>" id="<%=s%>" name="<%=s%>" >
                            <%=s%>
                        </label>

                <%
                }}
                %>

            </div>
        </label>
    </div>
</body>
</html>
