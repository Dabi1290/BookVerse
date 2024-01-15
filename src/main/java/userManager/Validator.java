package userManager;

import proposalManager.Proposal;

import java.util.Collection;
import java.util.Set;

public class Validator {

    private int id;

    private Set<Proposal> assignedProposals;

    public Validator() {

    }

    public static Validator makeValidator(int id, Set<Proposal> assignedProposals) {
        Validator validator = new Validator();

        validator.id = id;
        validator.assignedProposals = assignedProposals;

        return validator;
    }

    public void assignProposal(Proposal proposal) {
        assignedProposals.add(proposal);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Set<Proposal> getAssignedProposals() {
        return assignedProposals;
    }

    public void setAssignedProposals(Set<Proposal> assignedProposals) {
        this.assignedProposals = assignedProposals;
    }
}
