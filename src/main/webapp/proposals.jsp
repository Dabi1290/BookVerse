<%@ page import="com.bookverse.bookverse.sessionConstants.SessionCostants" %>
<%@ page import="userManager.User" %>
<%@ page import="userManager.Author" %>
<%@ page import="proposalManager.Proposal" %>
<%@ page import="java.util.Set" %>
<%@ page import="proposalManager.Version" %>
<%@ page import="userManager.Validator" %><%--
  Created by IntelliJ IDEA.
  User: Tonaion
  Date: 15/01/2024
  Time: 16:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>

    <%
        String p;

        User user = (User) session.getAttribute(SessionCostants.USER);
        Validator validator = user.getRoleValidator();

        Set<Proposal> assignedProposals =  validator.getAssignedProposals();

        for(Proposal proposal : assignedProposals) {
            p = "id: " + proposal.getId() + " status: " + proposal.getStatus();
    %>
            <p><%= p%></p>
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
</body>
</html>
