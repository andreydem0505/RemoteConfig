package andreydem0505.remoteconfig.exceptions;

import andreydem0505.remoteconfig.data.documents.PropertyType;

public class DynPropertyTypeValidationException extends RuntimeException {
    public DynPropertyTypeValidationException(PropertyType type) {
        super("Dyn property of unexpected type: " + type);
    }
}
