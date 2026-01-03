package andreydem0505.remoteconfig;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class RemoteConfigApplication {

    public static void main(String[] args) {
        SpringApplication.run(RemoteConfigApplication.class, args);
    }

}
