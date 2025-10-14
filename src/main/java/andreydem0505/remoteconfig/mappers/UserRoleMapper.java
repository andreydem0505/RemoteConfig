package andreydem0505.remoteconfig.mappers;

import andreydem0505.remoteconfig.security.UserRole;
import org.springframework.security.core.GrantedAuthority;

public class UserRoleMapper {
    public static GrantedAuthority mapToGrantedAuthority(UserRole role) {
        return role::name;
    }
}
