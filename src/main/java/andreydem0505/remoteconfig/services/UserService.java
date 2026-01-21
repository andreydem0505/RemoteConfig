package andreydem0505.remoteconfig.services;

import andreydem0505.remoteconfig.cache.RedisConfig;
import andreydem0505.remoteconfig.cache.UserCache;
import andreydem0505.remoteconfig.data.documents.User;
import andreydem0505.remoteconfig.data.repositories.UserRepository;
import andreydem0505.remoteconfig.exceptions.UserAlreadyExistsException;
import andreydem0505.remoteconfig.mappers.UserMapper;
import andreydem0505.remoteconfig.security.UserRole;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final Cache cache;

    public UserService(UserRepository userRepository,
                       UserMapper userMapper,
                       @Qualifier(RedisConfig.USER_CACHE_QUALIFIER) Cache cache) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.cache = cache;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = getUserCacheable(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }

    public User getUser(String username) {
        return getUserCacheable(username);
    }

    public void saveUser(String username, String password, UserRole role) {
        if (getUserCacheable(username) != null) {
            throw new UserAlreadyExistsException();
        }
        User user = new User(username, password, role);
        userRepository.save(user);
        cache.put(username, userMapper.toCache(user));
    }

    private User getUserCacheable(String username) {
        Cache.ValueWrapper wrapper = cache.get(username);
        if (wrapper != null) {
            UserCache userCache = (UserCache) wrapper.get();
            return userMapper.fromCache(username, userCache);
        }
        return userRepository.findByUsername(username);
    }
}
