package andreydem0505.remoteconfig.services.feature_flags;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UnitInListFeatureFlagChecker implements FeatureFlagChecker{
    @Override
    public boolean checkHit(Object context, Object data) {
        List<Object> list = (List<Object>) data;
        return list.contains(context);
    }
}
