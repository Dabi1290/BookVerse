package ebookManager;

import proposalManager.Proposal;
import userManager.Author;

import java.util.Collection;
import java.io.File;

public class EBook {

    private int id;
    private String title;
    private String description;
    private File ebookFile;
    private Collection<String> genres;
    private Proposal proposedThrough;
    private Author mainAuthor;
    private Collection<Author> coAuthors;
    private boolean inCatalog;

    public EBook() {
    }

    public EBook makeEbook(Proposal proposal) {
        return null;
    }

}
