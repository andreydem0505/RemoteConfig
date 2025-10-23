package andreydem0505.remoteconfig.services.feature_flags;

public interface FeatureFlagChecker {
    boolean checkHit(Object context, Object data);
}
