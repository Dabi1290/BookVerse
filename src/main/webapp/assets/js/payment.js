let cvvre = /\b\d{3}\b/;
let intsre = /^[a-zA-Z\s]{1,30}$/;
let numre = /^\d{4}-\d{4}-\d{4}-\d{4}$/;


window.onload = function() {
    let error = document.getElementById("error");
}





function checkData(){
    let year = document.getElementById("y");
    let month = document.getElementById("m");
    let currentDate = new Date();
    if((month.value<=12 && month.value>=0) || (year.value>9999)){
    let inputDate = new Date(year.value, month.value - 1);
    if (inputDate < currentDate) {
        year.style.borderColor = "red";
        month.style.borderColor = "red";
        error.innerText = "Expiration date cannot be earlier than today and the format must be MM/YY";
        return false;
    }

    else {
        year.style.borderColor = "green";
        month.style.borderColor = "green";
        error.innerText = "";
        return true;
    }
    }
    else{
        year.style.borderColor = "red";
        month.style.borderColor = "red";
        error.innerText = "Expiration date cannot be earlier than today and the format must be MM/YY";
        return false;
    }
}
function checkCvv(){
    let cvv = document.getElementById("cvv");
    if(!cvv.value.match(cvvre)){
        cvv.style.borderColor = "red";
        error.innerText = "The Cvv must have the format ***, and consists of only digits.";
        return false;
    }
    else{
        cvv.style.borderColor = "green";
        error.innerText = "";
        return true;
    }
}
function  checkInte(){
    let ints=document.getElementById("ints");
    if(!ints.value.match(intsre)){
        ints.style.borderColor = "red";
        error.innerText = "The holder can contain a maximum of 30 alphabetic characters (no symbols and numbers).";
        return false;
    }
    else{
        ints.style.borderColor = "green";
        error.innerText = "";
        return true;
    }
}
function  checkNum(){
    let num=document.getElementById("num");
    if(!num.value.match(numre)){
        num.style.borderColor = "red";
        error.innerText = "The card number must have the format ****-****-****, and consist of only digits.";
        return false;
    }
    else {
        num.style.borderColor = "green";
        error.innerText = "";
        return true;
    }
}




function send(propId) {

        if(checkCvv() && checkData() && checkInte() && checkNum()) {
            let form = document.getElementById("pippo");
            form.action = "/PayProposal?proposalId=" + propId;
            form.submit();
        }


}

