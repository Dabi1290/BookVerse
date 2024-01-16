window.onload = function() {

    h= document.getElementById("history");
    var inputElement = h.querySelector('input');
    var inputValue = inputElement.value;

    h.addEventListener("click", function () {

        window.location.href = "/history.jsp?idProp="+inputValue;
    });
}