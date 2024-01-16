let cvvre= /\d{3}/;
let intsre = /[a-zA-Z\s]+/;
let numre = /\d{4}-\d{4}-\d{4}-\d{4}/;








function checkData(){
    let year = document.getElementById("y");
    let month = document.getElementById("m");
    let currentDate = new Date();
    let inputDate = new Date(year.value, month.value - 1);
    if (inputDate < currentDate) {
        year.style.borderColor = "red";
        month.style.borderColor = "red";
        return false;
    }

    else {
        year.style.borderColor = "green";
        month.style.borderColor = "green";
        return true;
    }
}
function checkCvv(){
    let cvv = document.getElementById("cvv");
    if(!cvv.value.match(cvvre)){
        cvv.style.borderColor = "red";
        return false;
    }
    else{
        cvv.style.borderColor = "green";
        return true;
    }
}
function  checkInte(){
    let ints=document.getElementById("ints");
    if(!ints.value.match(intsre)){
        ints.style.borderColor = "red";
        return false;
    }
    else{
        ints.style.borderColor = "green";
        return true;
    }
}
function  checkNum(){
    let num=document.getElementById("num");
    if(!num.value.match(numre)){
        num.style.borderColor = "red";
        return false;
    }
    else {
        num.style.borderColor = "green";
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

