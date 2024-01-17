package proposalManager;

import userManager.Author;
import userManager.Validator;

import java.util.regex.Pattern;

import java.io.File;
import java.util.*;

public class Proposal {

    private int id;
    private String status;
    private boolean alreadyLoadedAuthor;
    private Author proposedBy;
    private Set<Author> collaborators;
    private List<Version> versions;
    private boolean alreadyLoadedValidator;
    private Validator assignedValidator;

    public Proposal() {
        this.versions = new ArrayList<>();
    }

    public static Proposal makeProposal(Author author, Set<Author> coAuthors, String status){
        Proposal p = new Proposal();
        p.proposedBy = author;
        p.collaborators = coAuthors;
        p.status = status;
        return p;
    }

    public static boolean isValidParameter(String title, Collection<String> genres, float price, String description){

        if(title == null || title.isEmpty())
            return false;

        if(! Pattern.matches("^[a-zA-Z0-9]+$", title))
            return false;

        if(price < 0)
            return false;

        if(description == null || description.isEmpty())
            return false;

        if(! Pattern.matches("^[a-zA-Z0-9\\s]+$", description))
            return false;

        if(genres == null || genres.isEmpty())
            return false;

        return true;
    }

    public void approve(){
        this.status="Approved";
    }

    public void refuse(){
        this.status="Refused";
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

    public void correct() {
        this.status = "Pending";
    }

    public void addVersion(Version version){
        versions.add(0, version);
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

    public void setCollaborators(Set<Author> collaborators) {this.collaborators = collaborators;}

    public Set<Author> getCollaborators() {
        return collaborators;
    }

    public Version lastVersion() {
        return versions.get(0);
    }

    public void setVersions(List<Version> versions) { this.versions = versions; }

    public List<Version> getVersions() {return versions;}

    public boolean isAlreadyLoadedAuthor() {
        return alreadyLoadedAuthor;
    }

    public void setAlreadyLoadedAuthor(boolean alreadyLoadedAuthor) {
        this.alreadyLoadedAuthor = alreadyLoadedAuthor;
    }

    public boolean isAlreadyLoadedValidator() {
        return alreadyLoadedValidator;
    }

    public void setAlreadyLoadedValidator(boolean alreadyLoadedValidator) {
        this.alreadyLoadedValidator = alreadyLoadedValidator;
    }

    public Validator getAssignedValidator() {
        return assignedValidator;
    }

    public void setAssignedValidator(Validator assignedValidator) {
        this.assignedValidator = assignedValidator;
    }
}
