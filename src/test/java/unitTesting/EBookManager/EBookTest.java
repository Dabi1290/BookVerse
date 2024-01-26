package unitTesting.EBookManager;

import ebookManager.EBook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proposalManager.Proposal;
import proposalManager.Version;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EBookTest {

    private EBook ebook;
    private Proposal p;
    private Version v;
    private String title;
    private String description;
    private int price;

    @BeforeEach
    public void setUp() {
        p = new Proposal();
    }

    public void buildProposal() {
        v = new Version();
        v.setTitle(title);
        v.setDescription(description);
        v.setPrice(price);
        p.setVersions(Arrays.asList(v));
    }

    @Test
    public void makeEbook_VP1_VV1_VT1_VD1_VPR1() throws Exception {
        title = "Title1";
        price = 10;
        description = "Description1";

        buildProposal();

        ebook = EBook.makeEbook(p);

        assertTrue(ebook.getDescription().equals(description)
                && ebook.getTitle().equals(title)
                && ebook.getPrice() == price);
    }

    @Test
    public void makeEbook_VP1_VV1_VT1_VD1_VPR2() {
        title = "Title1";
        price = -1;
        description = "Description1";

        buildProposal();

        Exception e = assertThrows(Exception.class, () -> EBook.makeEbook(p));
        assertEquals(e.getMessage(), "value of price is not valid");
    }

    @Test
    public void makeEBook_VP1_VV1_VT1_VD2_VPR1() {
        title = "Giorno Dello Sciacallo";
        price = 10;
        description = "";

        buildProposal();

        Exception e = assertThrows(Exception.class, () -> EBook.makeEbook(p));
        assertEquals(e.getMessage(), "value of description is not valid");
    }

    @Test
    public void makeEBook_VP1_VV1_VT2_VD1_VPR1() {
        title = null;
        price = 10;
        description = "description_un_po_diversa";

        buildProposal();

        Exception e = assertThrows(Exception.class, () -> EBook.makeEbook(p));
        assertEquals(e.getMessage(), "value of title is not valid");
    }

    @Test
    public void makeEBook_VP1_VV2_VT1_VD1_VPR1() {
        title = "IT";
        price = 10;
        description = "horror generico";

        v = null;
        List<Version> versions = new ArrayList<>();
        versions.add(v);
        p.setVersions(versions);

        Exception e = assertThrows(Exception.class, () -> EBook.makeEbook(p));
        assertEquals(e.getMessage(), "there isn't a version in proposal");
    }

    @Test
    public void makeEBook_VP2_VV1_VT1_VD1_VPR1() {
        p = null;

        Exception e = assertThrows(Exception.class, () -> EBook.makeEbook(p));
        assertEquals(e.getMessage(), "value of proposal is not valid");
    }
}
