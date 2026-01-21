package andreydem0505.remoteconfig.cache;

import andreydem0505.remoteconfig.security.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserCache implements Serializable {
    private String password;
    private UserRole role;
}
