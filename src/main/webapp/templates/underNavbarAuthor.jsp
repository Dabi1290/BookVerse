<%--
  Created by IntelliJ IDEA.
  User: davideamoruso
  Date: 04/01/24
  Time: 16:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Pagine</title>
    <link rel="stylesheet" href="<%= request.getContextPath()%>/assets/css/underNavbar.css">

</head>
<body>
<div class="red-line">
    <a href="<%=request.getContextPath()%>/Publications">
        <div class="page-name">
            Publications
        </div>
    </a>
    <a href="<%=request.getContextPath()%>/MyJobs">
        <div class="page-name">
            My jobs
        </div>
    </a>
    <a href="<%=request.getContextPath()%>/requisiti.jsp">
        <div class="page-name">
            Requirements
        </div>
    </a>
</div>
</body>
</html>
