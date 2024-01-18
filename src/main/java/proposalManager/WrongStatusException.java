package proposalManager;

public class WrongStatusException extends Exception {
    public WrongStatusException(String previous, String now) {
        super("You can't pass from status: " + previous + " to: " + now);
    }

}
