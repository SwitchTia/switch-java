/*package train.trainproject2.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import train.trainproject2.UTILITIES.configurations.AuthenticationRequest;
import train.trainproject2.UTILITIES.configurations.AuthenticationResponse;
import train.trainproject2.UTILITIES.configurations.RegisterRequest;
import train.trainproject2.entities.Role;
import train.trainproject2.entities.Users;
import train.trainproject2.repositories.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository ur;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {

       Users user = Users.builder()
            .firstname (request.getFirstname())
            .lastname (request.getLastname())
            .email (request.getEmail())
            .password (passwordEncoder.encode(request.getPassword()))
            .role (Role.USER)
            .build();
        ur.save(user);

        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
        .token(jwtToken)
        .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
            request.getEmail(), 
            request.getPassword()));

        var user = ur.findByEmail(request.getEmail());
         
        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
        .token(jwtToken)
        .build();
    }
    
}*/
