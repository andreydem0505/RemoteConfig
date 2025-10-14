package andreydem0505.remoteconfig.services;

import andreydem0505.remoteconfig.data.documents.DynProperty;
import andreydem0505.remoteconfig.data.repositories.DynPropertyRepository;
import andreydem0505.remoteconfig.exceptions.DynPropertyAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DynPropertiesService {
    private final DynPropertyRepository dynPropertyRepository;

    public void createDynProperty(String username, String propertyName, Object data) {
        if (dynPropertyRepository.findByUsernameAndPropertyName(username, propertyName) != null) {
            throw new DynPropertyAlreadyExistsException();
        }
        DynProperty dynProperty = new DynProperty(username, propertyName, data);
        dynPropertyRepository.save(dynProperty);
    }
}
