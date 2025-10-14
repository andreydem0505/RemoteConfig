package andreydem0505.remoteconfig.data.repositories;

import andreydem0505.remoteconfig.data.documents.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
    User findByUsername(String name);
}
