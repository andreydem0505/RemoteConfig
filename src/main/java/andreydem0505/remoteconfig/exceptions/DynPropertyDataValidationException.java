package andreydem0505.remoteconfig.exceptions;

import andreydem0505.remoteconfig.data.documents.PropertyType;

public class DynPropertyDataValidationException extends RuntimeException {
    public DynPropertyDataValidationException(PropertyType type) {
        super("Invalid data for property type: " + type);
    }
}
