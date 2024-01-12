package ebookManager;

import proposalManager.Proposal;
import proposalManager.Version;
import userManager.Author;

import java.util.Collection;
import java.io.File;
import java.util.Set;

public class EBook {

    private int id;
    private String title;
    private String description;
    private File ebookFile;
    private Set<String> genres;
    private boolean proposalAlreadyLoaded;
    private Proposal proposedThrough;
    private int price;
    private boolean mainAuthorAlreadyLoaded;
    private Author mainAuthor;
    private Collection<Author> coAuthors;
    private boolean inCatalog;

    public EBook() {
        this.genres = null;
        this.proposalAlreadyLoaded = false;
        this.mainAuthorAlreadyLoaded = false;
    }

    public EBook makeEbook(Proposal proposal) {
        EBook book = new EBook();

        book.mainAuthorAlreadyLoaded = true;
        book.mainAuthor = proposal.getProposedBy();
        book.proposalAlreadyLoaded = true;
        book.proposedThrough = proposal;

        book.coAuthors = proposal.getCollaborators();

        //Retrieve last version info and add to ebook
        Version lastVersion = proposal.lastVersion();
        book.id = id;
        book.description = lastVersion.getDescription();
        book.genres = lastVersion.getGenres();
        book.ebookFile = lastVersion.getEbookFile();
        book.inCatalog = true; //CHECK
        book.title = lastVersion.getTitle();
        book.price = lastVersion.getPrice();
        //Retrieve last version info and add to ebook

        return book;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public File getEbookFile() {
        return ebookFile;
    }

    public void setEbookFile(File ebookFile) {
        this.ebookFile = ebookFile;
    }

    public Set<String> getGenres() {
        return genres;
    }

    public void setGenres(Set<String> genres) {
        this.genres = genres;
    }

    public boolean isProposalAlreadyLoaded() {
        return proposalAlreadyLoaded;
    }

    public void setProposalAlreadyLoaded(boolean proposalAlreadyLoaded) {
        this.proposalAlreadyLoaded = proposalAlreadyLoaded;
    }

    public Proposal getProposedThrough() {
        return proposedThrough;
    }

    public void setProposedThrough(Proposal proposedThrough) {
        this.proposedThrough = proposedThrough;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean isMainAuthorAlreadyLoaded() {
        return mainAuthorAlreadyLoaded;
    }

    public void setMainAuthorAlreadyLoaded(boolean mainAuthorAlreadyLoaded) {
        this.mainAuthorAlreadyLoaded = mainAuthorAlreadyLoaded;
    }

    public Author getMainAuthor() {
        return mainAuthor;
    }

    public void setMainAuthor(Author mainAuthor) {
        this.mainAuthor = mainAuthor;
    }

    public Collection<Author> getCoAuthors() {
        return coAuthors;
    }

    public void setCoAuthors(Collection<Author> coAuthors) {
        this.coAuthors = coAuthors;
    }

    public boolean isInCatalog() {
        return inCatalog;
    }

    public void setInCatalog(boolean inCatalog) {
        this.inCatalog = inCatalog;
    }
}
