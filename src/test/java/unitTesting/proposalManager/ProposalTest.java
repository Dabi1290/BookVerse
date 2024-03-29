package unitTesting.proposalManager;

import com.sun.source.tree.Tree;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import proposalManager.Proposal;
import proposalManager.Version;
import proposalManager.WrongStatusException;
import userManager.Author;
import userManager.Validator;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ProposalTest {

    private Proposal p;

    @BeforeEach
    void setUp() {
        p = new Proposal();
    }

    @Test
    void makeProposal_A1_B1_D2() {
        Author ma = null;
        Set<Author> cA = new HashSet<>();
        try {
            assertThrows(Exception.class,()->Proposal.makeProposal(ma,cA));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    void makeProposal_A1_B2_D2() {
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
    void makeProposal_A1_B3_D2() {
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
    void makeProposal_A2_B2_D1() {
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
    void makeProposalA2_B3_D1() {
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
    void makeProposal_A2_B1_D2() {
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
    void makeProposal_A2_B2_D2() {
        Author ma = new Author();
        ma.setId(1);
        Author ma1 = new Author();
        ma1.setId(2);
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
    void makeProposal_A2_B3_D2() {
        Author ma = new Author();
        ma.setId(1);
        Author ma1 = new Author();
        ma1.setId(2);
        Author ma2 = new Author();
        ma2.setId(3);
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
    void approve_A2(){
        p.setStatus("Completed");
        assertThrows(WrongStatusException.class, p::approve);
    }
    @Test
    void approve_A1(){
        p.setStatus("Pending");
        try {
            p.approve();
        } catch (WrongStatusException e) {
            throw new RuntimeException(e);
        }
        assertEquals("Approved",p.getStatus());
    }

    @Test
    void refuse_A2() {
        p.setStatus("Pluto");
        assertThrows(WrongStatusException.class, p::refuse);
    }
    @Test
    void refuse_A1(){
        p.setStatus("Pending");

        try {
            p.refuse();
        } catch (WrongStatusException e) {
            throw new RuntimeException(e);
        }

        assertEquals("Refused",p.getStatus());
    }

    @Test
    void permanentlyRefuse_A2() {

        p.setStatus("Pluto");
        assertThrows(WrongStatusException.class, p::permanentlyRefuse);
    }
    @Test
    void PermanentlyRefuse_A1(){
        p.setStatus("Pending");

        try {
            p.permanentlyRefuse();
        } catch (WrongStatusException e) {
            throw new RuntimeException(e);
        }
        assertEquals("PermanentlyRefused",p.getStatus());
    }

    @Test
    void pay_A2() {

        p.setStatus("Pluto");
        assertThrows(WrongStatusException.class, p::pay);
    }
    @Test
    void pay_A1() {
        p.setStatus("Approved");
        try {
            p.pay();
        } catch (WrongStatusException e) {
            throw new RuntimeException(e);
        }
        assertEquals("Completed",p.getStatus());
    }

    @Test
    void correct_A2() {

        p.setStatus("Pluto");
        assertThrows(WrongStatusException.class, p::correct);
    }
    @Test
    void correct_A1() {
        p.setStatus("Refused");
        try {
            p.correct();
        } catch (WrongStatusException e) {
            throw new RuntimeException(e);
        }
        assertEquals("Pending",p.getStatus());
    }

    @Test
    void assignValidator_A1_B1_C1() {
        p.setStatus("Pending");
        Validator v= null;
        Validator v1= null;
        p.setAssignedValidator(v1);
        assertThrows(Exception.class,()->p.assignValidator(v));
    }
    @Test
    void assignValidator_A1_B2_C2() {
        p.setStatus("Pending");
        Validator v= new Validator();
        Validator v1= new Validator();
        p.setAssignedValidator(v1);
        assertThrows(Exception.class,()->p.assignValidator(v));
    }
    @Test
    void assignValidator_A2_B2_C1() {
        p.setStatus("Pippo");
        Validator v= new Validator();
        Validator v1= null;
        p.setAssignedValidator(v1);
        assertThrows(Exception.class,()->p.assignValidator(v));
    }
    @Test
    void assignValidator_A1_B2_C1() {
        p.setStatus("Pending");
        Validator v= new Validator();
        Validator v1= null;
        p.setAssignedValidator(v1);
        try {
            p.assignValidator(v);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        assertEquals(p.getAssignedValidator(),v);
    }
    @Test
    void addVersion_S1_V1_D2() {
        p.setStatus("Pending");
        Version v1 = new Version();
        v1.setDate(LocalDate.of(2030,1,1));
        List<Version> lv= new ArrayList<>();
        lv.add(v1);
        p.setVersions(lv);
        Version v = new Version();
        v.setDate(LocalDate.of(2020,1,1));
        assertThrows(Exception.class,()->p.addVersion(v));

    }
    @Test
    void addVersionS1_V2_D1() {
        p.setStatus("Pending");
        Version v1 = new Version();
        v1.setDate(LocalDate.of(2030,1,1));
        List<Version> lv= new ArrayList<>();
        lv.add(v1);
        p.setVersions(lv);
        Version v = null;
        assertThrows(Exception.class,()->p.addVersion(v));
    }
    @Test
    void addVersion_S2_V1_D1() {
        p.setStatus("Pippo");
        Version v = new Version();
        v.setDate(LocalDate.of(2030,1,1));
        assertThrows(Exception.class,()->p.addVersion(v));

    }
    @Test
    void addVersionS1_V1_D1() {
        p.setStatus("Pending");
        Version v = new Version();
        v.setDate(LocalDate.of(2030,1,1));
        int pVsize=p.getVersions().size();
        try {
            p.addVersion(v);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        assertEquals(pVsize+1,p.getVersions().size());
        assertTrue(p.getVersions().contains(v));
        assertEquals(0,p.getVersions().indexOf(v));

    }

    @Test
    void lastVersion_V1() {
        List<Version> lv= new ArrayList<>();
        p.setVersions(lv);
        assertThrows(Exception.class,()->p.lastVersion());

    }
    @Test
    void lastVersion_V2() {
        Version v1 = new Version();
        v1.setDate(LocalDate.of(2030,1,1));
        List<Version> lv= new ArrayList<>();
        lv.add(v1);
        p.setVersions(lv);
        try {
            assertEquals(v1,p.lastVersion());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    void lastVersion_V3() {
        Version v1 = new Version();
        v1.setDate(LocalDate.of(2030,1,1));
        Version v2 = new Version();
        v2.setDate(LocalDate.of(2029,1,1));
        List<Version> lv= new ArrayList<>();
        lv.add(v1);
        lv.add(v2);
        p.setVersions(lv);
        try {
            assertEquals(v1,p.lastVersion());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}