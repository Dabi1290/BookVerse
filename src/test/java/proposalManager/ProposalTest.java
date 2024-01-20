package proposalManager;

import org.junit.jupiter.api.Test;
import userManager.Validator;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProposalTest {

    @Test
    void makeProposal() {
    }

    @Test
    void isValidParameter() {
    }

    @Test
    void approve(){
        Proposal p = new Proposal();
        p.setStatus("Pending");
        try {
            p.approve();
        } catch (WrongStatusException e) {
            throw new RuntimeException(e);
        }
        assertEquals("Approved",p.getStatus());
        p.setStatus("Pluto");
        assertThrows(WrongStatusException.class, p::approve);
    }

    @Test
    void refuse() {
        Proposal p = new Proposal();
        p.setStatus("Pending");
        try {
            p.refuse();
        } catch (WrongStatusException e) {
            throw new RuntimeException(e);
        }
        assertEquals("Refused",p.getStatus());
        p.setStatus("Pluto");
        assertThrows(WrongStatusException.class, p::refuse);
    }

    @Test
    void permanentlyRefuse() {
        Proposal p = new Proposal();
        p.setStatus("Pending");
        try {
            p.permanentlyRefuse();
        } catch (WrongStatusException e) {
            throw new RuntimeException(e);
        }
        assertEquals("PermanentlyRefused",p.getStatus());
        p.setStatus("Pluto");
        assertThrows(WrongStatusException.class, p::permanentlyRefuse);
    }

    @Test
    void pay() {
        Proposal p = new Proposal();
        p.setStatus("Approved");
        try {
            p.pay();
        } catch (WrongStatusException e) {
            throw new RuntimeException(e);
        }
        assertEquals("Completed",p.getStatus());
        p.setStatus("Pluto");
        assertThrows(WrongStatusException.class, p::pay);
    }

    @Test
    void correct() {
        Proposal p = new Proposal();
        p.setStatus("Refused");
        try {
            p.correct();
        } catch (WrongStatusException e) {
            throw new RuntimeException(e);
        }
        assertEquals("Pending",p.getStatus());
        p.setStatus("Pluto");
        assertThrows(WrongStatusException.class, p::correct);
    }

    @Test
    void assignValidator() {
        Validator v= new Validator();
        Validator v1= new Validator();
        Proposal p= new Proposal();
        p.assignValidator(v);
        assertEquals(v,p.getAssignedValidator());
        assertNotEquals(v1,p.getAssignedValidator());
    }

    @Test
    void addVersion() {
        Proposal p = new Proposal();
        p.setStatus("Refused");
    }

    @Test
    void lastVersion() {
        Proposal p = new Proposal();
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