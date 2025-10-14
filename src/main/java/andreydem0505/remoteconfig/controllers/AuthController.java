package andreydem0505.remoteconfig.controllers;

import andreydem0505.remoteconfig.dto.AuthDto;
import andreydem0505.remoteconfig.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Urls.AUTH_URL)
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody AuthDto authDto) {
        authService.register(authDto.getUsername(), authDto.getPassword());
        return ResponseEntity.ok("User registered");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthDto authDto) {
        String token = authService.login(authDto.getUsername(), authDto.getPassword());
        return ResponseEntity.ok(token);
    }
}
