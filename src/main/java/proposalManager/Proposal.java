package proposalManager;

import userManager.Author;
import userManager.Validator;

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

    public boolean isValidParameter(String title, Author author, Collection<String> genres, File ebookFile, Collection<Author> coAuthors, File coverImage, float price, String description){
        return true;
    }

    public void addVersion(Version version){
        versions.add(version);
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

    public Set<Author> getCollaborators() {
        return collaborators;
    }

    public Version lastVersion() {
        return this.versions.get(versions.size() - 1);
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
