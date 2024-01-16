window.onload = function() {

}

function  showReport(propId){
    document.getElementById("myOverlay").style.display = "block";
    let report= document.getElementById("form-report");
    report.action = "/RefuseProposal?proposalId="+propId;
}
function  hideReport(){
    document.getElementById("myOverlay").style.display = "none";
}