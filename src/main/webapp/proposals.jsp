<%@ page import="com.bookverse.bookverse.sessionConstants.SessionCostants" %>
<%@ page import="userManager.User" %>
<%@ page import="userManager.Author" %>
<%@ page import="proposalManager.Proposal" %>
<%@ page import="java.util.Set" %>
<%@ page import="proposalManager.Version" %>
<%@ page import="userManager.Validator" %>
<%@ page import="com.bookverse.bookverse.ServletUtils" %><%--
  Created by IntelliJ IDEA.
  User: Tonaion
  Date: 15/01/2024
  Time: 16:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Proposals</title>
    <link rel="stylesheet" href="assets/css/proposal.css">
    <script src="assets/js/proposals.js"></script>
    <script src="assets/js/fileUpload.js"></script>
</head>
<body>
<%@include file="templates/navbarLogged.jsp" %>
<%@include file="templates/underNavbarValidator.jsp" %>
<div class="proposals">
    <%
        String p;

        User user = (User) session.getAttribute(SessionCostants.USER);
        Validator validator = user.getRoleValidator();

        Set<Proposal> assignedProposals =  validator.getAssignedProposals();

        for(Proposal proposal : assignedProposals) {
            if(proposal.getStatus().equals("Pending")){
            Version lv = proposal.lastVersion();
    %>
<div class="proposal-author">
    <div class="title-status">
        <p><%=lv.getTitle() %></p>
    </div>
    <div class="genre-authors">
        <div class="raggruppa">
            <p>Genres</p>
            <div class="proposal-genres">
                <%=ServletUtils.generateGeneri(lv.getGenres())%>
            </div>
        </div>
    </div>
    <div class="versions-buttons">
        <%=ServletUtils.validatorButton(proposal.getStatus(),proposal.getId(),lv)%>
    </div>
</div>

    <%
            }
        }
    %>
    <div id="myOverlay" class="overlay">
        <form id="form-report" class="overlay-container" method="post" action="" enctype="multipart/form-data">
            <img src="assets/images/exit-cross.png" onclick="hideReport()">
        <div class="report-box">
            <label for="e-book" class="label">
                Report
                <div class="input-container" >
                    <img src="assets/images/upload.png" class="icon" id="e-book-icon">
                    <input type="file" id="e-book" class="input-file" name="report"> <!-- input pdf -->
                    <div class="labeling" id="upload-phrase-file">Click to upload</div>
                    <button class="button" id="e-book-button">PDF</button>
                    <span id="upload-check-file">
                        <img src="assets/images/greenCheck.png">
                        <span id="upload-text-file"></span>
                    </span>
                </div>
            </label>
            <input type="submit" class="orange-button" value="Refuse">

        </div>
        </form>


    </div>
</div>
</body>
</html>
