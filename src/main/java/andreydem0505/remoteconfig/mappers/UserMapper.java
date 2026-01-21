package andreydem0505.remoteconfig.mappers;

import andreydem0505.remoteconfig.cache.UserCache;
import andreydem0505.remoteconfig.data.documents.User;
import org.springframework.stereotype.Service;

@Service
public class UserMapper {
    public UserCache toCache(User user) {
        return new UserCache(user.getPassword(), user.getRole());
    }

    public User fromCache(String username, UserCache cache) {
        return new User(username, cache.getPassword(), cache.getRole());
    }
}
