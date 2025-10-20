package andreydem0505.remoteconfig.services;

import andreydem0505.remoteconfig.data.documents.DynProperty;
import andreydem0505.remoteconfig.data.documents.PropertyType;
import andreydem0505.remoteconfig.data.repositories.DynPropertyRepository;
import andreydem0505.remoteconfig.exceptions.DynPropertyAlreadyExistsException;
import andreydem0505.remoteconfig.exceptions.DynPropertyDataValidationException;
import andreydem0505.remoteconfig.exceptions.DynPropertyNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DynPropertyService {
    private final DynPropertyRepository dynPropertyRepository;

    public void createDynProperty(String username, String propertyName, PropertyType type, Object data) {
        if (!validateName(propertyName)) {
            throw new DynPropertyDataValidationException("Invalid property name: " + propertyName);
        }
        if (dynPropertyRepository.findByUsernameAndPropertyName(username, propertyName) != null) {
            throw new DynPropertyAlreadyExistsException();
        }
        if (!validateData(type, data)) {
            throw new DynPropertyDataValidationException("Invalid data for property type: " + type);
        }
        DynProperty dynProperty = new DynProperty(username, propertyName, type, data);
        dynPropertyRepository.save(dynProperty);
    }

    public Object getDynPropertyData(String username, String propertyName) {
        DynProperty dynProperty = dynPropertyRepository.findByUsernameAndPropertyName(username, propertyName);
        if (dynProperty == null) {
            throw new DynPropertyNotFoundException();
        }
        return dynProperty.getData();
    }

    private boolean validateName(String propertyName) {
        if (propertyName == null || propertyName.isEmpty()) {
            return false;
        }
        return propertyName.matches("^[a-zA-Z0-9_]+(\\.[a-zA-Z0-9_]+)*$");
    }

    private boolean validateData(PropertyType type, Object data) {
        return switch (type) {
            case CUSTOM_PROPERTY -> true;
            case BOOLEAN_FEATURE_FLAG -> data instanceof Boolean;
            case PERCENTAGE_FEATURE_FLAG -> data instanceof Integer i && i >= 0 && i <= 100;
        };
    }
}
