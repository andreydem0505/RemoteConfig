package andreydem0505.remoteconfig.services.feature_flags;

import andreydem0505.remoteconfig.services.RandomGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PercentageFeatureFlagChecker implements FeatureFlagChecker {
    private final RandomGeneratorService randomGeneratorService;

    @Override
    public boolean checkHit(Object context, Object data) {
        int percentage = (int) data;
        return percentage / 100.0f > randomGeneratorService.generate();
    }
}
