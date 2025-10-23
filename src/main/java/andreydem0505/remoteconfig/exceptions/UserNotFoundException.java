package andreydem0505.remoteconfig.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("No user with this username was found");
    }
}
