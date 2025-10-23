package andreydem0505.remoteconfig.services;

import andreydem0505.remoteconfig.data.documents.DynProperty;
import andreydem0505.remoteconfig.data.documents.PropertyType;
import andreydem0505.remoteconfig.data.repositories.DynPropertyRepository;
import andreydem0505.remoteconfig.exceptions.*;
import andreydem0505.remoteconfig.services.feature_flags.FeatureFlagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DynPropertyService {
    private final DynPropertyRepository dynPropertyRepository;
    private final DynPropertyValidationService validationService;
    private final FeatureFlagService featureFlagService;

    public void createDynProperty(String username, String propertyName, PropertyType type, Object data) {
        validationService
                .validateName(propertyName)
                .validateData(type, data);

        if (dynPropertyRepository.findByUsernameAndPropertyName(username, propertyName) != null) {
            throw new DynPropertyAlreadyExistsException();
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

    public boolean checkHit(String username, String propertyName, Object context) {
        DynProperty dynProperty = dynPropertyRepository.findByUsernameAndPropertyName(username, propertyName);
        if (dynProperty == null) {
            throw new DynPropertyNotFoundException();
        }

        PropertyType type = dynProperty.getType();

        validationService
                .validateIsFeatureFlag(type)
                .validateContext(propertyName, type, context, dynProperty.getData());

        return featureFlagService.checkHit(type, context, dynProperty.getData());
    }
}
