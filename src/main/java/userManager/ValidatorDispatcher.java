package userManager;

import java.util.Collection;
import java.util.Set;

public interface ValidatorDispatcher {
    public Validator findFreeValidator(Author mainAuthor, Set<Author> coAuthors) throws Exception;
}
