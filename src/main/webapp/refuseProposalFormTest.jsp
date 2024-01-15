<%--
  Created by IntelliJ IDEA.
  User: Tonaion
  Date: 13/01/2024
  Time: 00:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <form action="/RefuseProposal" method="POST" enctype="multipart/form-data">
        <input type="text" name="proposalId">
        <br>
        <input type="file" name="report">
        <br>
        <input type="submit">

    </form>
</body>
</html>
