package andreydem0505.remoteconfig.services;

import andreydem0505.remoteconfig.data.documents.User;
import andreydem0505.remoteconfig.data.repositories.UserRepository;
import andreydem0505.remoteconfig.exceptions.NoUserWithUsernameException;
import andreydem0505.remoteconfig.exceptions.UserAlreadyExistsException;
import andreydem0505.remoteconfig.security.JwtService;
import andreydem0505.remoteconfig.security.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public void register(String username, String password) throws UserAlreadyExistsException {
        if (userRepository.findByUsername(username) != null) {
            throw new UserAlreadyExistsException();
        }
        User user = new User(username, passwordEncoder.encode(password), UserRole.COMMON);
        userRepository.save(user);
    }

    public String login(String username, String password) throws NoUserWithUsernameException, BadCredentialsException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new NoUserWithUsernameException();
        }
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        return jwtService.generateToken(user);
    }
}
