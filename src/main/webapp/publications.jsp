<%@ page import="com.bookverse.bookverse.sessionConstants.SessionCostants" %>
<%@ page import="userManager.User" %>
<%@ page import="userManager.Author" %>
<%@ page import="proposalManager.Proposal" %>
<%@ page import="java.util.Set" %>
<%@ page import="proposalManager.Version" %><%--
  Created by IntelliJ IDEA.
  User: Tonaion
  Date: 15/01/2024
  Time: 16:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Publications</title>
    <link rel="stylesheet" href="assets/css/proposal.css">
</head>
<body>
<%@include file="templates/navbarLogged.jsp" %>
<%@include file="templates/underNavbarAuthor.jsp" %>
<%
    String p;

    User user = (User) session.getAttribute(SessionCostants.USER);
    Author author = user.getRoleAuthor();

    Set<Proposal> proposalWhereIsMainAuthor =  author.getProposed();

    for(Proposal proposal : proposalWhereIsMainAuthor) {
        Version lv= proposal.lastVersion();
%>
<div class="proposals">
<div class="proposal-author">
    <div class="title-status">
        <p><%=lv.getTitle() %></p>
        <div class="status"><span class="dot"></span><p class="<%=proposal.getStatus()%>"><%=proposal.getStatus()%></p></div>
    </div>
    <div class="genre-authors">
        <div class="raggruppa">
            <p>Genres</p>
        <div class="proposal-genres">
            <%
                for(String g:lv.getGenres()){
            %>
            <p><%=g%></p>
            <%
                }%>
        </div>
        </div>
        <div class="raggruppa">
            <p>Authors</p>
        <div class="proposal-authors">
            <%
                //for(Author a:proposal.getCollaborators()){
            %>
            <p>pippo</p>
            <%
               // }%>
        </div>
        </div>
    </div>
    <div class="proposal-buttons">
        <%
            String stato= proposal.getStatus();
            if(!stato.equals("PermanentlyRefused")){
                if(stato.equals("Approved")){
                    %>
        <div>Pay now</div>
        <%
                }
                if(stato.equals("Pending")||stato.equals("Refuse")){
                %>
        <div class="orange-button">History</div>
        <%
                    if(stato.equals("Pending")){
                    %>
        <div class="orange-button">Try now</div>
        <%
                    }
                }
            }
        %>
    </div>
</div>

<%
    }

    Set<Proposal> proposalWhereIsCoAuthor = author.getCollaboratedTo();
    for(Proposal proposal : proposalWhereIsCoAuthor) {
        p = "id: " + proposal.getId() + " status: " + proposal.getStatus();
%>
<p><%= p%> </p>
<%
    for(Version version : proposal.getVersions()) {
%>
<p><%=version.getDate()%></p>
<%
    }
%>
<%
    }
%>
</div>
</body>
</html>
