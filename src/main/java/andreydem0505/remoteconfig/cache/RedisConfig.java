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
    public static final String CACHE_NAME = "DYNAMIC_PROPERTIES";

    @Bean
    public RedisSerializer<String> simpleKeyKryoSerializer() {
        return new SimpleKeyKryoSerializer();
    }

    @Bean
    public RedisSerializer<DynPropertyCache> dynPropertyKryoSerializer() {
        return new DynPropertyKryoSerializer();
    }

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory,
                                          RedisSerializer<String> simpleKeyKryoSerializer,
                                          RedisSerializer<DynPropertyCache> dynPropertyKryoSerializer) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofDays(30))
                .disableCachingNullValues()
                .serializeKeysWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(simpleKeyKryoSerializer))
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(dynPropertyKryoSerializer));

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(config)
                .build();
    }

    @Bean
    public Cache cache(RedisCacheManager cacheManager) {
        return cacheManager.getCache(CACHE_NAME);
    }
}
