<%--
  Created by IntelliJ IDEA.
  User: Tonaion
  Date: 12/01/2024
  Time: 21:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<form action="/ProposalCreation" method="post" enctype="multipart/form-data">
    <input type="text" name="title">
    <br>
    <input type="number" name="price">
    <br>
    <input type="description" name="description">
    <br>
    <input id="ebookFile" name="ebookFile" type="file">
    <br>
    <input id="coverImage" name="coverImage" type="file">
    <br>

    <select name="authors" multiple>
        <option name="author1" value="1">author1</option>
        <option name="author2" value="2">author2</option>
    </select>
    <br>

    <input type="checkbox" name="genres" value="genere1">
    <br>
    <input type="checkbox" name="genres" value="genere2">
    <br>

    <input type="submit" value="submit">
</form>
</body>
</html>
