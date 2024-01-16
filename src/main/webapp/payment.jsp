<%--
  Created by IntelliJ IDEA.
  User: davideamoruso
  Date: 16/01/24
  Time: 21:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Pagamento</title>
    <link rel="stylesheet" href="assets/css/payment.css">
    <script src="assets/js/payment.js"></script>
</head>
<body>
<%@include file="templates/navbarLogged.jsp" %>
<%@include file="templates/underNavbarAuthor.jsp" %>
<%String id = request.getParameter("proposalId");%>
<form class="payment-container" action="" method="post" id="pippo">
    <div class="card-informations">
        <div id="payment-title">Credit Card</div>
        <div class="first-line">
            <input type="text" id="ints"name="intestatario" placeholder="Nome Cognome" class="grande" onblur="checkInte()">
            <input type="text" id="num"name="cardNumber" placeholder="1234 1234 1234 1234" class="grande" onblur="checkNum()">
        </div>
        <div class="first-line">
            <div class="second-line">
            <input type="text" id="m"name="month" placeholder="MM" class="piccolo" onblur="checkData()">
            <input type="text" id="y" name="year" placeholder="YYYY" class="piccolo" onblur="checkData()">
            </div>
            <input type="text" id="cvv" name="cvv" placeholder="000" class="grande" onblur="checkCvv()">
        </div>
    </div>
    <div class="riepilogo">
        <div class="summary">
            <div>Order summary</div>
            <div class="summary-row">
                <div>Publishing an ebook</div>
                <div>50 €</div>
            </div>
            <div class="summary-row">
                <div>TOTAL</div>
                <div>50 €</div>
            </div>
        </div>
        <div id="pay-button" onclick="send(<%=id%>)">Pay now</div>
    </div>

</form>
</body>
</html>
