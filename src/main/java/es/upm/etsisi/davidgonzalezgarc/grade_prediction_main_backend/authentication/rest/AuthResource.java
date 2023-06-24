package es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.authentication.rest;

import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.authentication.AuthService;
import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.authentication.rest.dto.AuthRequestDto;
import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.authentication.rest.dto.AuthResponseDto;
import es.upm.etsisi.davidgonzalezgarc.grade_prediction_main_backend.authentication.rest.dto.RegisterRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthResource {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(@RequestBody RegisterRequestDto request) {
        return ResponseEntity.ok().body(authService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponseDto> authenticate(@RequestBody AuthRequestDto request) {
        return ResponseEntity.ok().body(authService.authenticate(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        final String jwt;
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().build();
        }
        jwt = authorization.substring(7);
        authService.logout(jwt);
        return ResponseEntity.ok().build();
    }

}
