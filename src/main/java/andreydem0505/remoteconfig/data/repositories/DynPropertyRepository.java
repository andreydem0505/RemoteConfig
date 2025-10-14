package andreydem0505.remoteconfig.data.repositories;

import andreydem0505.remoteconfig.data.documents.DynProperty;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DynPropertyRepository extends MongoRepository<DynProperty, String> {
    DynProperty findByUsernameAndPropertyName(String username, String propertyName);
}
