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
</head>
<body>
<%@include file="templates/navbar.jsp" %>
<div class="div">
  <div class="div-10">
    <div class="div-11">
      <div class="div-12">Sign in to BookVerse</div>

      <input type="text" class="input-email" placeholder="Email">
      <input type="password" class="input-password" placeholder="Password">
      <div class="div-15">

          <div class="button">Reader</div>
          <div class="button">Validator</div>


          <div class="button">Author</div>
          <div class="button">Catalogue Manager</div>

      </div>
      <div class="div-22">
        No account?
        <a href="register.jsp">Register now!</a>
      </div>
    </div>
  </div>
</div>

</body>
</html>
