package andreydem0505.remoteconfig.services;

import andreydem0505.remoteconfig.data.documents.PropertyType;
import andreydem0505.remoteconfig.exceptions.DynPropertyContextValidationException;
import andreydem0505.remoteconfig.exceptions.DynPropertyDataValidationException;
import andreydem0505.remoteconfig.exceptions.DynPropertyNameValidationException;
import andreydem0505.remoteconfig.exceptions.DynPropertyTypeValidationException;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class DynPropertyValidationService {

    DynPropertyValidationService validateName(String name) {
        if (name == null || name.isEmpty() || !name.matches("^[a-zA-Z0-9_]+(\\.[a-zA-Z0-9_]+)*$")) {
            throw new DynPropertyNameValidationException(name);
        }
        return this;
    }

    DynPropertyValidationService validateData(PropertyType type, Object data) {
        boolean validationResult = switch (type) {
            case CUSTOM_PROPERTY, EQUALITY_FEATURE_FLAG -> true;
            case BOOLEAN_FEATURE_FLAG -> data instanceof Boolean;
            case PERCENTAGE_FEATURE_FLAG -> data instanceof Integer i && i >= 0 && i <= 100;
            case UNIT_IN_LIST_FEATURE_FLAG -> data instanceof Collection<?>;
        };

        if (!validationResult) {
            throw new DynPropertyDataValidationException(type);
        }
        return this;
    }

    DynPropertyValidationService validateIsFeatureFlag(PropertyType type) {
        boolean validationResult = !type.equals(PropertyType.CUSTOM_PROPERTY);
        if (!validationResult) {
            throw new DynPropertyTypeValidationException(type);
        }
        return this;
    }

    DynPropertyValidationService validateContext(String name, PropertyType type, Object context, Object data) {
        boolean validationResult = switch (type) {
            case CUSTOM_PROPERTY -> false;
            case BOOLEAN_FEATURE_FLAG, PERCENTAGE_FEATURE_FLAG -> context == null;
            case EQUALITY_FEATURE_FLAG -> true;
            case UNIT_IN_LIST_FEATURE_FLAG -> data instanceof Collection<?> collection &&
                    (collection.isEmpty() || context.getClass().equals(collection.iterator().next().getClass()));
        };
        if (!validationResult) {
            throw new DynPropertyContextValidationException(name);
        }
        return this;
    }
}
