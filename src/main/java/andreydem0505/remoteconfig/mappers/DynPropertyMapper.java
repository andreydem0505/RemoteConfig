package andreydem0505.remoteconfig.mappers;

import andreydem0505.remoteconfig.cache.DynPropertyCache;
import andreydem0505.remoteconfig.data.documents.DynProperty;
import org.springframework.stereotype.Service;

@Service
public class DynPropertyMapper {
    public DynPropertyCache toCache(DynProperty dynProperty) {
        return new DynPropertyCache(dynProperty.getType(), dynProperty.getData());
    }

    public DynProperty fromCache(String username, String propertyName, DynPropertyCache cache) {
        return new DynProperty(username, propertyName, cache.getType(), cache.getData());
    }
}
