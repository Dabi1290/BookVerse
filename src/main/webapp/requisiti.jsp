<%--
  Created by IntelliJ IDEA.
  User: davideamoruso
  Date: 26/01/24
  Time: 13:15
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Requirements</title>
    <link rel="stylesheet" href="assets/css/requisiti.css">
</head>
<body>
<%@include file="templates/navbarLogged.jsp" %>
<%@include file="templates/underNavbarAuthor.jsp" %>
<div class="requisiti">
<h2>File Format</h2>
<li>Format: Use only PDF format for the ebook.</li>
    <li>Format: Use only PNG format for the ebook cover.</li>

<h2>Metadata</h2>
<li>Title: Insert an appropriate and catchy title.</li>
<li>Author: Indicate the email of any co-authors.</li>
<li>Description: Provide a complete and interesting description of the book.</li>
<li>Cover: Use an image for the cover that is high quality, eye-catching and relevant to the content of the book.</li>

<h2>Content</h2>
<li>Text: Make sure the text is formatted correctly without grammatical or spelling errors.</li>
<li>Formatting: Maintain consistent formatting for chapters, paragraphs and text styles.</li>
<li>Images: If present, ensure that images are of high quality and well integrated into the text.</li>

<h2>Accessibility</h2>
<li>Tables: If using tables, check their readability and accessibility.</li>
<li>Alternative Text: Provide alternative text for images to ensure accessibility.</li>

<h2>Legal Compliance</h2>
<li>Copyrights: Make sure you have copyrights for the content or provide the necessary information.</li>
<li>Illegal Content: Avoid posting illegal or infringing content.</li>

<h2>Price</h2>
<li>Price: Determine an appropriate price for the book.</li>
</div>


</body>
</html>
