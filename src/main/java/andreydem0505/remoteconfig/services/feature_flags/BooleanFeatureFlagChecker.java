package andreydem0505.remoteconfig.services.feature_flags;

import org.springframework.stereotype.Component;

@Component
public class BooleanFeatureFlagChecker implements FeatureFlagChecker {
    @Override
    public boolean checkHit(Object context, Object data) {
        return (boolean) data;
    }
}
