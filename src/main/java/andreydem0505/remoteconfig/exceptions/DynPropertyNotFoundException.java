package andreydem0505.remoteconfig.exceptions;

public class DynPropertyNotFoundException extends RuntimeException {
    public DynPropertyNotFoundException() {
        super("No dynamic property with this name was found");
    }
}
