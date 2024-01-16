<%@ page import="com.bookverse.bookverse.sessionConstants.SessionCostants" %>
<%@ page import="userManager.Author" %>
<%@ page import="proposalManager.Proposal" %>
<%@ page import="proposalManager.Version" %>
<%@ page import="java.util.Set" %>
<%@ page import="java.util.List" %>
<%@ page import="com.bookverse.bookverse.ServletUtils" %><%--
  Created by IntelliJ IDEA.
  User: davideamoruso
  Date: 16/01/24
  Time: 00:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>History</title>
    <link rel="stylesheet" href="assets/css/proposal.css">
</head>
<body>
<%@include file="templates/navbarLogged.jsp" %>
<%@include file="templates/underNavbarAuthor.jsp" %>
<div class="versions">
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
        List<Version> vv= proposal.getVersions();
        for(Version v : vv) {
    %>

    <div class="proposal-author">
        <div class="title-status">
            <p><%=v.getTitle() %></p>
            <div class="status">versions of <%=v.getDate()%></div>
        </div>
        <div class="genre-authors">
            <div class="raggruppa">
                <p>Genres</p>
                <div class="proposal-genres">
                    <%=ServletUtils.generateGeneri(v.getGenres())%>
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
            <%=ServletUtils.versionButton(proposal.getStatus(),proposal.getId())%>
        </div>
    </div>

    <%}%>
</div>
</body>
</html>
