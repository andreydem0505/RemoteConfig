package andreydem0505.remoteconfig.data.documents;

import andreydem0505.remoteconfig.mappers.UserRoleMapper;
import andreydem0505.remoteconfig.security.UserRole;
import lombok.Value;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Document(collection = "users")
@Value
public class User implements UserDetails {
    String username;
    String password;
    UserRole role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(UserRoleMapper.mapToGrantedAuthority(role));
    }

    @Override
    public String getUsername() {
        return username;
    }
}
