package andreydem0505.remoteconfig.services;

import andreydem0505.remoteconfig.data.documents.User;
import andreydem0505.remoteconfig.exceptions.UserNotFoundException;
import andreydem0505.remoteconfig.security.JwtService;
import andreydem0505.remoteconfig.security.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public void register(String username, String password) {
        userService.saveUser(username, passwordEncoder.encode(password), UserRole.COMMON);
    }

    public String login(String username, String password) {
        User user = userService.getUser(username);
        if (user == null) {
            throw new UserNotFoundException();
        }
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        return jwtService.generateToken(user);
    }
}
