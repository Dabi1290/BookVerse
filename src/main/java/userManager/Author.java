package userManager;

import java.util.Collection;
import java.util.TreeSet;

public class Author {

    private int id;
    private int userId;
    private Collection<Object> collaboratedTo;
    private Collection<Object> proposed;
    private Collection<Object> written;
    private Collection<Object> coWritten;

    public Author() {
        collaboratedTo = new TreeSet<>();
        proposed = new TreeSet<>();
        written = new TreeSet<>();
        coWritten = new TreeSet<>();
    }

    public void addProposal(Object proposal) {
        proposed.add(proposal);
    }

    public void addProposalCollaboration(Object proposal) {
        collaboratedTo.add(proposal);
    }

    public void addEbook(Object ebook) {
        written.add(ebook);
    }

    public void addEbookCollaboration(Object ebook) {
        coWritten.add(ebook);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Collection<Object> getCollaboratedTo() {
        return collaboratedTo;
    }

    public void setCollaboratedTo(Collection<Object> collaboratedTo) {
        this.collaboratedTo = collaboratedTo;
    }

    public Collection<Object> getProposed() {
        return proposed;
    }

    public void setProposed(Collection<Object> proposed) {
        this.proposed = proposed;
    }

    public Collection<Object> getWritten() {
        return written;
    }

    public void setWritten(Collection<Object> written) {
        this.written = written;
    }

    public Collection<Object> getCoWritten() {
        return coWritten;
    }

    public void setCoWritten(Collection<Object> coWritten) {
        this.coWritten = coWritten;
    }

}
