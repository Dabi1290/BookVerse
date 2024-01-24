package userManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proposalManager.Proposal;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

public class ValidatorTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = new Validator();
    }

    @Test
    void assignProposal_VP2() {
        Exception e = assertThrows(Exception.class, () -> validator.assignProposal(null));
        assertEquals("Proposal is not valid", e.getMessage());
    }

    @Test
    void assignProposal_VP1() throws Exception {
        Proposal p = new Proposal();
        validator.setAssignedProposals(new HashSet<>());
        validator.assignProposal(p);

        assertTrue(validator.getAssignedProposals().contains(p));
    }
}
