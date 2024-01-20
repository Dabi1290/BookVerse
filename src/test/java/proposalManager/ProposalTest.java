package proposalManager;

import com.sun.source.tree.Tree;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import userManager.Author;
import userManager.Validator;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ProposalTest {

    private Proposal p;

    @BeforeEach
    void setUp() {
        p = new Proposal();
    }

    @Test
    void makeProposalErrorMA1() {
        Author ma = null;
        Set<Author> cA = new HashSet<>();
        try {
            assertThrows(Exception.class,()->Proposal.makeProposal(ma,cA));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    void makeProposalErrorMA2() {
        Author ma = null;
        Author ma1 = new Author();
        Set<Author> cA = new HashSet<>();
        cA.add(ma1);
        try {
            assertThrows(Exception.class,()->Proposal.makeProposal(ma,cA));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    void makeProposalErrorMA3() {
        Author ma = null;
        Author ma1 = new Author();
        Author ma2 = new Author();
        Set<Author> cA =new HashSet<>();
        cA.add(ma1);
        cA.add(ma2);
        try {
            assertThrows(Exception.class,()->Proposal.makeProposal(ma,cA));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    void makeProposalErrorCA1() {
        Author ma = new Author();
        Set<Author> cA = new HashSet<>();
        cA.add(ma);
        try {
            assertThrows(Exception.class,()->Proposal.makeProposal(ma,cA));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    void makeProposalErrorCA2() {
        Author ma = new Author();
        Author ma1 = new Author();
        Set<Author> cA = new HashSet<>();
        cA.add(ma);
        cA.add(ma1);
        try {
            assertThrows(Exception.class,()->Proposal.makeProposal(ma,cA));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void makeProposalOk1() {
        Author ma = new Author();
        Set<Author> cA = new HashSet<>();
        Proposal p;
        try {
            p=Proposal.makeProposal(ma,cA);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        assertEquals(ma,p.getProposedBy());
        assertEquals(cA,p.getCollaborators());
        assertEquals("Pending",p.getStatus());
    }
    @Test
    void makeProposalOk2() {
        Author ma = new Author();
        Author ma1 = new Author();
        Set<Author> cA = new HashSet<>();
        cA.add(ma1);
        Proposal p;
        try {
            p=Proposal.makeProposal(ma,cA);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        assertEquals(ma,p.getProposedBy());
        assertEquals(cA,p.getCollaborators());
        assertEquals("Pending",p.getStatus());
    }
    @Test
    void makeProposalOk3() {
        Author ma = new Author();
        Author ma1 = new Author();
        Author ma2 = new Author();
        Set<Author> cA = new HashSet<>();
        cA.add(ma1);
        cA.add(ma2);
        Proposal p;
        try {
            p=Proposal.makeProposal(ma,cA);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        assertEquals(ma,p.getProposedBy());
        assertEquals(cA,p.getCollaborators());
        assertEquals("Pending",p.getStatus());
    }

    @Test
    void isValidParameter() {

    }

    @Test
    void approveErr(){
        p.setStatus("Pluto");
        assertThrows(WrongStatusException.class, p::approve);
    }
    @Test
    void approveOk(){
        p.setStatus("Pending");
        try {
            p.approve();
        } catch (WrongStatusException e) {
            throw new RuntimeException(e);
        }
        assertEquals("Approved",p.getStatus());
    }

    @Test
    void refuseErr() {
        p.setStatus("Pluto");
        assertThrows(WrongStatusException.class, p::refuse);
    }
    @Test
    void refuseOk(){
        p.setStatus("Pending");

        try {
            p.refuse();
        } catch (WrongStatusException e) {
            throw new RuntimeException(e);
        }

        assertEquals("Refused",p.getStatus());
    }

    @Test
    void permanentlyRefuseErr() {

        p.setStatus("Pluto");
        assertThrows(WrongStatusException.class, p::permanentlyRefuse);
    }
    @Test
    void PermanentlyRefuseOk(){
        p.setStatus("Pending");

        try {
            p.permanentlyRefuse();
        } catch (WrongStatusException e) {
            throw new RuntimeException(e);
        }
        assertEquals("PermanentlyRefused",p.getStatus());
    }

    @Test
    void payErr() {

        p.setStatus("Pluto");
        assertThrows(WrongStatusException.class, p::pay);
    }
    @Test
    void payOk() {
        p.setStatus("Approved");
        try {
            p.pay();
        } catch (WrongStatusException e) {
            throw new RuntimeException(e);
        }
        assertEquals("Completed",p.getStatus());
    }

    @Test
    void correctErr() {

        p.setStatus("Pluto");
        assertThrows(WrongStatusException.class, p::correct);
    }
    @Test
    void correctOK() {
        p.setStatus("Refused");
        try {
            p.correct();
        } catch (WrongStatusException e) {
            throw new RuntimeException(e);
        }
        assertEquals("Pending",p.getStatus());
    }

    @Test
    void assignValidator() {
        Validator v= new Validator();
        Validator v1= new Validator();
        p.assignValidator(v);
        assertEquals(v,p.getAssignedValidator());
        assertNotEquals(v1,p.getAssignedValidator());
    }

    @Test
    void addVersion() {
        //TO-DO
        p.setStatus("Refused");
    }

    @Test
    void lastVersion() {
        assertThrows(Exception.class, p::lastVersion);
        Version v = new Version();
        List<Version> lv= new ArrayList<>();
        lv.add(v);
        try {
            p.setVersions(lv);
            assertEquals(v,p.lastVersion());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}