package andreydem0505.remoteconfig.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class RandomGeneratorService {
    private final SecureRandom random;

    private RandomGeneratorService(
            @Value("${RANDOM_GENERATOR_SEED}") String seed
    ) {
        random = new SecureRandom(seed.getBytes());
    }

    public float generate() {
        return random.nextFloat();
    }
}
