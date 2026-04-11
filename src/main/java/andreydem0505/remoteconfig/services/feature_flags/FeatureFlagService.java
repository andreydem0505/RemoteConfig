package andreydem0505.remoteconfig.services.feature_flags;

import andreydem0505.remoteconfig.data.documents.PropertyType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeatureFlagService {
    private final BooleanFeatureFlagChecker booleanFeatureFlagChecker;
    private final PercentageFeatureFlagChecker percentageFeatureFlagChecker;
    private final EqualityFeatureFlagService equalityFeatureFlagService;
    private final UnitInListFeatureFlagChecker unitInListFeatureFlagChecker;
    private final AllInListFeatureFlagChecker allInListFeatureFlagChecker;
    private final AnyInListFeatureFlagChecker anyInListFeatureFlagChecker;
    private final StringContainsFeatureFlagChecker stringContainsFeatureFlagChecker;

    public boolean checkHit(PropertyType type, Object context, Object data) {
        FeatureFlagChecker checker = switch (type) {
            case BOOLEAN_FEATURE_FLAG -> booleanFeatureFlagChecker;
            case PERCENTAGE_FEATURE_FLAG -> percentageFeatureFlagChecker;
            case EQUALITY_FEATURE_FLAG -> equalityFeatureFlagService;
            case UNIT_IN_LIST_FEATURE_FLAG -> unitInListFeatureFlagChecker;
            case ALL_IN_LIST_FEATURE_FLAG -> allInListFeatureFlagChecker;
            case ANY_IN_LIST_FEATURE_FLAG -> anyInListFeatureFlagChecker;
            case STRING_CONTAINS_FEATURE_FLAG -> stringContainsFeatureFlagChecker;
            default -> null;
        };
        return checker.checkHit(context, data);
    }
}
