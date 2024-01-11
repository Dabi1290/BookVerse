package proposalManager;

import userManager.Author;
import userManager.Validator;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Proposal {

    private int id;
    private String status;
    private Author proposedBy;
    private Collection<Author> collaborators;
    private List<Version> versions;
    private Validator assignedValidator;


    public Proposal() {
        this.versions = new ArrayList<>();
        this.collaborators = new ArrayList<>();
    }

    public static Proposal makeProposal(Author author, Collection<Author> coAuthors, String status){
        Proposal p = new Proposal();
        p.proposedBy = author;
        p.collaborators = coAuthors;
        p.status = status;
        return p;
    }

    public void approve(){
        this.status="Approved";
    }

    public void refuse(){
        this.status="Refuse";
    }

    public void permanentlyRefuse(){
        this.status="PermanentlyRefused";
    }

    public void pay(){
        this.status = "Completed";
    }

    public void assignValidator(Validator validator){
        this.assignedValidator = validator;
    }

    public boolean isValidParameter(String title, Author author, Collection<String> genres, File ebookFile, Collection<Author> coAuthors, File coverImage, float price, String description){
        return true;
    }

    public void addVersion(Version version){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Author getProposedBy() {
        return proposedBy;
    }

    public void setProposedBy(Author proposedBy) {
        this.proposedBy = proposedBy;
    }
}
