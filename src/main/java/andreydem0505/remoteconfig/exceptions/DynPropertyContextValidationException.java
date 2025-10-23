package andreydem0505.remoteconfig.exceptions;

public class DynPropertyContextValidationException extends RuntimeException {
    public DynPropertyContextValidationException(String name) {
        super("Invalid context for property " + name);
    }
}
