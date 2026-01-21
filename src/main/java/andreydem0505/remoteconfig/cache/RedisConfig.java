package andreydem0505.remoteconfig.cache;

import org.springframework.cache.Cache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.Duration;

@Configuration
public class RedisConfig {
    public static final String DYN_PROPERTY_CACHE_NAME = "DYNAMIC_PROPERTIES";
    public static final String DYN_PROPERTY_CACHE_QUALIFIER = "DYN_PROPERTY_CACHE";
    public static final String USER_CACHE_NAME = "USERS";
    public static final String USER_CACHE_QUALIFIER = "USER_CACHE";

    @Bean
    public RedisSerializer<String> keyKryoSerializer() {
        return new KeyKryoSerializer();
    }

    @Bean
    public RedisSerializer<Object> valueKryoSerializer() {
        return new ValueKryoSerializer();
    }

    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory,
                                          RedisSerializer<String> keyKryoSerializer,
                                          RedisSerializer<Object> valueKryoSerializer) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofDays(30))
                .disableCachingNullValues()
                .serializeKeysWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(keyKryoSerializer))
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(valueKryoSerializer));

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(config)
                .build();
    }

    @Bean(name = DYN_PROPERTY_CACHE_QUALIFIER)
    public Cache dynPropertyCache(RedisCacheManager cacheManager) {
        return cacheManager.getCache(DYN_PROPERTY_CACHE_NAME);
    }

    @Bean(name = USER_CACHE_QUALIFIER)
    public Cache userCache(RedisCacheManager cacheManager) {
        return cacheManager.getCache(USER_CACHE_NAME);
    }
}
