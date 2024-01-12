package userManager;

import ebookManager.EBook;
import proposalManager.Proposal;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

public class Author {

    private int id;
    private int userId;
    private Set<Proposal> collaboratedTo;
    private Set<Proposal> proposed;
    private Set<EBook> written;
    private Set<EBook> coWritten;

    public Author() {

    }

    public Author(int id, int uId) {
        this.id=id;
        this.userId=uId;
        collaboratedTo =null;
        proposed = null;
        written = null;
        coWritten = null;
    }
    public Author(int id) {
        this.id=id;
        userId=-1;
        collaboratedTo =null;
        proposed = null;
        written = null;
        coWritten = null;
    }

    public static Author makeAuthor(int id, Set<Proposal> collaboratedTo, Set<Proposal> proposed, Set<EBook> written, Set<EBook> coWritten) {
        Author author = new Author();

        author.id = id;
        author.collaboratedTo = collaboratedTo;
        author.proposed = proposed;
        author.written = written;
        author.coWritten = coWritten;

        return author;
    }

    public void addProposal(Proposal proposal) {
        proposed.add(proposal);
    }

    public void addProposalCollaboration(Proposal proposal) {
        collaboratedTo.add(proposal);
    }

    public void addEbook(EBook ebook) {
        written.add(ebook);
    }

    public void addEbookCollaboration(EBook ebook) {
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

    public Set<Proposal> getCollaboratedTo() {
        return collaboratedTo;
    }

    public void setCollaboratedTo(Set<Proposal> collaboratedTo) {
        this.collaboratedTo = collaboratedTo;
    }

    public Set<Proposal> getProposed() {
        return proposed;
    }

    public void setProposed(Set<Proposal> proposed) {
        this.proposed = proposed;
    }

    public Set<EBook> getWritten() {
        return written;
    }

    public void setWritten(Set<EBook> written) {
        this.written = written;
    }

    public Set<EBook> getCoWritten() {
        return coWritten;
    }

    public void setCoWritten(Set<EBook> coWritten) {
        this.coWritten = coWritten;
    }

}
