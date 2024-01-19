<%@ page import="com.bookverse.bookverse.sessionConstants.SessionCostants" %>
<%@ page import="userManager.User" %>
<%@ page import="userManager.Author" %>
<%@ page import="proposalManager.Proposal" %>
<%@ page import="java.util.Set" %>
<%@ page import="proposalManager.Version" %>
<%@ page import="view.ServletUtils" %><%--
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
<div class="proposals">
<%
    String p;

    User user = (User) session.getAttribute(SessionCostants.USER);
    Author author = user.getRoleAuthor();

    Set<Proposal> proposalWhereIsMainAuthor =  author.getProposed();

    for(Proposal proposal : proposalWhereIsMainAuthor) {
        Version lv= proposal.lastVersion();
        if(proposal.getStatus().equals("Pending") || proposal.getStatus().equals("Refused") || proposal.getStatus().equals("PermanentlyRefused") || proposal.getStatus().equals("Approved")){
%>

<div class="proposal-author">
    <div class="title-status">
        <p><%=lv.getTitle() %></p>
        <div class="status"><span class="<%=proposal.getStatus()%>dot"></span><p class="<%=proposal.getStatus()%>"><%=proposal.getStatus()%></p></div>
    </div>
    <div class="genre-authors">
        <div class="raggruppa">
            <p>Genres</p>
        <div class="proposal-genres">
            <%=ServletUtils.generateGeneri(lv.getGenres())%>
        </div>
        </div>
        <div class="raggruppa">
            <p>Authors</p>
        <div class="proposal-authors">
            <%=ServletUtils.generateAuthorNames(proposal.getCollaborators())%>
        </div>
        </div>
    </div>
    <div class="proposal-buttons">
        <%=ServletUtils.generateButton(proposal.getStatus(),proposal.getId())%>
    </div>
</div>

<%
    }} // fine mainAuthor

    Set<Proposal> proposalWhereIsCoAuthor = author.getCollaboratedTo();
    for(Proposal proposal : proposalWhereIsCoAuthor) {
        if(proposal.getStatus().equals("Pending") || proposal.getStatus().equals("Refused") || proposal.getStatus().equals("PermanentlyRefused") || proposal.getStatus().equals("Approved")){
        Version lv= proposal.lastVersion();
%>
    <div class="proposal-author">
        <div class="title-status">
            <p><%=lv.getTitle() %></p>
            <div class="status"><span class="<%=proposal.getStatus()%>dot"></span><p class="<%=proposal.getStatus()%>"><%=proposal.getStatus()%></p></div>
        </div>
        <div class="genre-authors">
            <div class="raggruppa">
                <p>Genres</p>
                <div class="proposal-genres">
                    <%=ServletUtils.generateGeneri(lv.getGenres())%>
                </div>
            </div>
            <div class="raggruppa">
                <p>Authors</p>
                <div class="proposal-authors">
                    <%=ServletUtils.generateAuthorNames(proposal.getCollaborators())%>
                </div>
            </div>
        </div>
        <div class="proposal-buttons">
            <%=ServletUtils.generateCoAuthorsButton(proposal.getStatus(),proposal.getId())%>
        </div>
    </div>
<%
    }}

%>
    <a href="proposalForm.jsp"><div class="publish-button">Publish</div></a>
</div>
</body>
</html>
