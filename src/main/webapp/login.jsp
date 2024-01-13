<%--
  Created by IntelliJ IDEA.
  User: davideamoruso
  Date: 23/12/23
  Time: 10:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
    <link rel="stylesheet" href="assets/css/login.css">
    <script src="assets/js/login.js"></script>
</head>
<body>
<%@include file="templates/navbar.jsp" %>
<div class="div">
  <div class="div-10">
    <form action="/login" method="POST" class="div-11" id="form">
      <div class="div-12">Sign in to BookVerse</div>
      <input type="text" class="input-email" placeholder="Email" name="email">
      <input type="password" class="input-password" placeholder="Password" name="password">
      <% if(request.getAttribute("error") != null){%>
          <h2> <%=request.getAttribute("error")%></h2>
      <%}%>
      <input type="hidden" value="" id="role" name="role">
        <div class="div-15">

          <div class="button" id="r">Reader</div>
          <div class="button" id="v">Validator</div>


          <div class="button" id="a">Author</div>
          <div class="button" id="c">Catalogue Manager</div>

      </div>
      <div class="div-22">
        No account?
        <a href="register.jsp">Register now!</a>
      </div>
    </form>
  </div>
</div>

</body>
</html>
