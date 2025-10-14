package andreydem0505.remoteconfig.exceptions;

public class NoUserWithUsernameException extends RuntimeException {
    public NoUserWithUsernameException() {
        super("No user with this username was found");
    }
}
