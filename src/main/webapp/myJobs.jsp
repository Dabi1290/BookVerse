<%@ page import="com.bookverse.bookverse.sessionConstants.SessionCostants" %>
<%@ page import="userManager.User" %>
<%@ page import="userManager.Author" %>
<%@ page import="proposalManager.Proposal" %>
<%@ page import="java.util.Set" %>
<%@ page import="proposalManager.Version" %>
<%@ page import="com.bookverse.bookverse.ServletUtils" %>
<%@ page import="ebookManager.EBook" %><%--
  Created by IntelliJ IDEA.
  User: Tonaion
  Date: 15/01/2024
  Time: 16:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>My Jobs</title>
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

        Set<EBook> proposalWhereIsMainAuthor =  author.getWritten();
        String status;
        for(EBook proposal : proposalWhereIsMainAuthor) {

            if(proposal.isInCatalog()) status="Completed";
            else status = "Removed";
    %>

    <div class="proposal-author">
        <div class="title-status">
            <p><%=proposal.getTitle() %></p>
            <div class="status"><span class="<%=status%>dot"></span><p class="<%=status%>"><%=status%></p></div>
        </div>
        <div class="proposal-buttons">
            <%=ServletUtils.ebookButton(status,proposal.getId())%>
        </div>
    </div>

    <%
        } // fine mainAuthor

        Set<EBook> proposalWhereIsCoAuthor = author.getCoWritten();
        for(EBook proposal : proposalWhereIsCoAuthor) {
            if(proposal.isInCatalog()) status="Completed";
            else status = "Removed";
    %>
    <div class="proposal-author">
        <div class="title-status">
            <p><%=proposal.getTitle() %></p>
            <div class="status"><span class="<%=status%>dot"></span><p class="<%=status%>"><%=status%></p></div>
        </div>
        <div class="proposal-buttons">
            <%=ServletUtils.ebookButton(status,proposal.getId())%>
        </div>
    </div>
    <%
        }
    %>
</div>
</body>
</html>
