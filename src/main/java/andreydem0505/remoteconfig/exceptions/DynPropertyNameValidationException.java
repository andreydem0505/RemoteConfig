package andreydem0505.remoteconfig.exceptions;

public class DynPropertyNameValidationException extends RuntimeException {
    public DynPropertyNameValidationException(String propertyName) {
        super("Invalid property name: " + propertyName);
    }
}
