package andreydem0505.remoteconfig.services.feature_flags;

import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class AllInListFeatureFlagChecker implements FeatureFlagChecker {
    @Override
    public boolean checkHit(Object context, Object data) {
        Collection<?> allowedValues = (Collection<?>) data;
        Collection<?> requestedValues = (Collection<?>) context;
        return allowedValues.containsAll(requestedValues);
    }
}
