package andreydem0505.remoteconfig.exceptions;

public class DynPropertyAlreadyExistsException extends RuntimeException {
    public DynPropertyAlreadyExistsException() {
        super("Dynamic property with this name already exists");
    }
}
