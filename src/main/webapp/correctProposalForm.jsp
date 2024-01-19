<%@ page import="java.util.List" %>
<%@ page import="com.bookverse.bookverse.sessionConstants.SessionCostants" %>
<%@ page import="userManager.Author" %>
<%@ page import="proposalManager.Proposal" %>
<%@ page import="java.util.Set" %>
<%@ page import="proposalManager.Version" %><%--
  Created by IntelliJ IDEA.
  User: davideamoruso
  Date: 06/01/24
  Time: 15:24
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Correct Proposal</title>
    <link rel="stylesheet" href="assets/css/proposal.css">
    <script src="assets/js/jquery-3.7.1.min.js"></script>
    <script src="assets/js/fileUpload.js"></script>
    <script src="assets/js/searchAuthor.js"></script>


</head>
<body>
<%@include file="templates/navbarLogged.jsp" %>
<%@include file="templates/underNavbarAuthor.jsp" %>
<%
    Proposal proposal= new Proposal();
    int id= Integer.parseInt(request.getParameter("idProp"));
    User user = (User) session.getAttribute(SessionCostants.USER);
    Set<Proposal> p1= user.getRoleAuthor().getProposed();
    if(p1.stream().anyMatch(p->p.getId()==id)) {
        for(Proposal i : p1){
            if(i.getId()==id)proposal=i;
        }
    }
    else{
        p1= user.getRoleAuthor().getCollaboratedTo();
        if(p1.stream().anyMatch(p->p.getId()==id)){
            for(Proposal i : p1){
                if(i.getId()==id)proposal=i;
            }
        }
    }
    Version lv = proposal.lastVersion();

%>
<form class="proposal-form" action="/ProposalCorrection?proposalId=<%=id%>" method="post" enctype="multipart/form-data" id="form-pippo">    <!-- Start form -->
    <label for="title" class="label">
        Title
        <input type="text" placeholder="Title" id="title" name="title" value="<%= lv.getTitle()%>" onblur="checkTitle()">  <!-- input titolo -->
    </label>

    <div class="ebook-inf" >
        <label for="price" class="label">
            Price
            <input type="text" placeholder="25" id="price" name="price" value="<%= lv.getPrice()%>" onblur="checkPrice()"> <!-- input prezzo -->
        </label>

        <label for="e-book" class="label">
            E-Book

            <div class="input-container" >
                <img src="assets/images/upload.png" class="icon" id="e-book-icon">
                <input type="file" id="e-book" class="input-file" name="ebookFile" onchange="checkEbook()"> <!-- input pdf -->
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
                <input type="file" id="cover" class="input-file" name="coverImage" onchange="checkCover()"> <!-- input copertina -->
                <div class="labeling" id="upload-phrase-cover"> Click to upload</div>
                <button class="button" id="cover-button">PNG</button>
                <span id="upload-check-cover">
                        <img src="assets/images/greenCheck.png">
                        <span id="upload-text-cover"></span>
                    </span>
            </div>
        </label>


    </div>
    <label for="description" class="description">
        Description
        <textarea id="description" name="description" onblur="checkDescription()"><%=lv.getDescription()%></textarea>
    </label>
    <label class="label">
        Genres
        <div class="genre-grid">

            <%
                ServletContext context=request.getServletContext();
                List<String> genres= (List<String>) context.getAttribute("Genres");
                Set<String> generiVersion= lv.getGenres();
                for(String a:generiVersion)System.out.println(a);
                if(genres!=null){
                    for(String s: genres){

            %>
            <label for=<%=s%>>
                <input type="checkbox" value="<%=s%>" id="<%=s%>" name="genres" <%if(generiVersion.contains(s)){%>checked<%}%> >
                <%=s%>
            </label>

            <%
                    }}
            %>

        </div>
    </label>
    <div id="error"></div>
    <div class="proposal-send" onclick="send()" id="send-button">Send</div>

</form>
</body>
</html>
