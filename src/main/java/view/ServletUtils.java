package view;


import proposalManager.Version;
import userManager.Author;

import java.util.Set;

public class ServletUtils {
    public static String generateButton(String stato,int idProp) {
        StringBuilder sb = new StringBuilder();
        if(!stato.equals("PermanentlyRefused")){
            if(stato.equals("Approved")){
                sb.append("<a href=\"/payment.jsp?proposalId="+idProp+"\"><div class=\"green-button\">Pay now</div></a>");
            }
            if(stato.equals("Pending")||stato.equals("Refused")){
                sb.append("<a href=\"/history.jsp?idProp="+idProp+"\" ><div class=\"orange-button\">History</div></a>");
                if(stato.equals("Refused")){
                    sb.append("<a href=\"/correctProposalForm.jsp?idProp="+idProp+"\"><div class=\"orange-button\">Try now</div></a>");
                }
            }
        }
        return sb.toString();
    }
    public static String generateCoAuthorsButton(String stato, int idProp) {
        StringBuilder sb = new StringBuilder();

            if(stato.equals("Pending")||stato.equals("Refused")){
                sb.append("<a href=\"/history.jsp?idProp="+idProp+"\" ><div class=\"orange-button\">History</div></a>");
            }

        return sb.toString();
    }
    public static String generateGeneri(Set<String> genres) {
        StringBuilder sb = new StringBuilder();
        for(String g : genres){
            sb.append("<p>" + g + "</p>");
        }
        return sb.toString();
    }

    public static String generateAuthorNames(Set<Author> collaborators) {
        StringBuilder sb = new StringBuilder();
        for(Author a : collaborators){
            sb.append("<p>" + a.getUser().getName() + " " + a.getUser().getSurname() + "</p>");
        }
        return sb.toString();
    }

    public static String versionButton(String stato, int idProp, Version v) {
        StringBuilder sb = new StringBuilder();
        sb.append("<a href=\"/FileDownload?fileName="+idProp+"/ebookFile_"+v.getId()+".pdf\"><div class=\"orange-button\">Ebook</div></a>");
        if(v.getReport()!=null) sb.append("<a href=\"/FileDownload?fileName="+idProp+"/reportFile_"+v.getId()+".pdf\"><div class=\"orange-button\">Report</div></a>");
        return sb.toString();
    }
    public static String validatorButton(String stato, int idProp, Version v) {
        StringBuilder sb = new StringBuilder();
        sb.append("<a href=\"/ApproveProposal?proposalId="+idProp+"\"><div class=\"green-button\">Approve</div></a>");
        sb.append("<div class=\"orange-button\" onclick=\"showReport("+idProp+")\">Refuse</div>");
        sb.append("<a href=\"/PermanentlyRefuse?proposalId="+idProp+"\"><div class=\"red-button\">PermanentlyRefuse</div></a>");
        return sb.toString();
    }

    public static String ebookButton(String stato,int idProp) {
        StringBuilder sb = new StringBuilder();
        sb.append("<a href=\"/pippo.jsp?proposalId="+idProp+"\"><div class=\"orange-button\">Proposal</div></a>");
        return sb.toString();
    }


}
