package andreydem0505.remoteconfig.services.feature_flags;

import org.springframework.stereotype.Component;

@Component
public class EqualityFeatureFlagService implements FeatureFlagChecker {
    @Override
    public boolean checkHit(Object context, Object data) {
        if (context == null) {
            return data == null;
        }
        return context.equals(data);
    }
}
