package andreydem0505.remoteconfig.services;

import andreydem0505.remoteconfig.cache.DynPropertyCache;
import andreydem0505.remoteconfig.cache.RedisConfig;
import andreydem0505.remoteconfig.data.documents.DynProperty;
import andreydem0505.remoteconfig.data.documents.PropertyType;
import andreydem0505.remoteconfig.data.repositories.DynPropertyRepository;
import andreydem0505.remoteconfig.exceptions.*;
import andreydem0505.remoteconfig.mappers.DynPropertyMapper;
import andreydem0505.remoteconfig.services.feature_flags.FeatureFlagService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.interceptor.SimpleKey;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DynPropertyService {
    private final DynPropertyRepository dynPropertyRepository;
    private final DynPropertyValidationService validationService;
    private final FeatureFlagService featureFlagService;
    private final Cache cache;
    private final DynPropertyMapper dynPropertyMapper;

    @CachePut(
            value = RedisConfig.CACHE_NAME,
            key = "new org.springframework.cache.interceptor.SimpleKey(#username, #propertyName)"
    )
    public DynPropertyCache createDynProperty(String username, String propertyName, PropertyType type, Object data) {
        validationService
                .validateName(propertyName)
                .validateData(type, data);

        if (dynPropertyRepository.findByUsernameAndPropertyName(username, propertyName) != null) {
            throw new DynPropertyAlreadyExistsException();
        }

        DynProperty dynProperty = new DynProperty(username, propertyName, type, data);
        dynPropertyRepository.save(dynProperty);
        return dynPropertyMapper.toCache(dynProperty);
    }

    public Object getDynPropertyData(String username, String propertyName) {
        DynProperty dynProperty = getDynPropertyOrThrow(username, propertyName);
        return dynProperty.getData();
    }

    public boolean checkHit(String username, String propertyName, Object context) {
        DynProperty dynProperty = getDynPropertyOrThrow(username, propertyName);
        PropertyType type = dynProperty.getType();

        validationService
                .validateIsFeatureFlag(type)
                .validateContext(propertyName, type, context, dynProperty.getData());

        return featureFlagService.checkHit(type, context, dynProperty.getData());
    }

    private DynProperty getDynPropertyOrThrow(String username, String propertyName) {
        Cache.ValueWrapper wrapper = cache.get(new SimpleKey(username, propertyName));
        if (wrapper != null) {
            DynPropertyCache propertyCache = (DynPropertyCache) wrapper.get();
            return dynPropertyMapper.fromCache(username, propertyName, propertyCache);
        }
        DynProperty dynProperty = dynPropertyRepository.findByUsernameAndPropertyName(username, propertyName);
        if (dynProperty == null) {
            throw new DynPropertyNotFoundException();
        }
        return dynProperty;
    }
}
