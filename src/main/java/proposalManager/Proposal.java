package proposalManager;

import userManager.Author;
import userManager.Validator;

import java.time.LocalDate;
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

    public static Proposal makeProposal(Author author, Set<Author> coAuthors){
        Proposal p = new Proposal();
        p.proposedBy = author;
        p.collaborators = coAuthors;
        p.status = "Pending";
        return p;
    }

    public static boolean isValidParameter(String title, Collection<String> genres, float price, String description){

        if(title == null || title.isEmpty())
            return false;

        if(title.length() > 30)
            return false;

        if(price < 0.0f || price > 500.0f)
            return false;

        if(description == null || description.isEmpty())
            return false;

        if(description.length() > 500)
            return false;

        if(genres == null || genres.isEmpty())
            return false;

        return true;
    }

    public void approve() throws WrongStatusException {
        if(! status.equals("Pending"))
            throw new WrongStatusException(status, "Approved");

        this.status="Approved";
    }

    public void refuse() throws WrongStatusException {
        if(! status.equals("Pending"))
            throw new WrongStatusException(status, "Refused");

        this.status="Refused";
    }

    public void permanentlyRefuse() throws WrongStatusException {
        if(! status.equals("Pending"))
            throw new WrongStatusException(status, "PermanentlyRefused");

        this.status="PermanentlyRefused";
    }

    public void pay() throws WrongStatusException {
        if(! status.equals("Approved"))
            throw new WrongStatusException(status, "Completed");

        this.status = "Completed";
    }

    public void correct() throws WrongStatusException {
        if(! status.equals("Refused"))
            throw new WrongStatusException(status, "Pending");

        this.status = "Pending";
    }
    public void assignValidator(Validator validator){
        this.assignedValidator = validator;
    }
    public void addVersion(Version version) throws Exception {
        if(! status.equals("Pending") && ! status.equals("Refused"))
            throw new Exception("Proposal is not on the correct status to add a version");

        if(version == null)
            throw new Exception("Version can't be null");

        Version lastVersion = null;
        if(! versions.isEmpty())
            lastVersion = versions.get(0);

        if(lastVersion != null && lastVersion.getDate().isAfter(version.getDate()))
            throw new Exception("Version's date not valid");

        versions.add(0, version);
    }
    public Version lastVersion() throws Exception {

        if(versions.isEmpty())
            throw new Exception("Versions collections is empty");
        return versions.get(0);
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
