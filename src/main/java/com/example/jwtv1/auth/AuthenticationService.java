package com.example.jwtv1.auth;

import com.example.jwtv1.JwtService;
import com.example.jwtv1.logoutToken.Token;
import com.example.jwtv1.logoutToken.TokenRepository;
import com.example.jwtv1.logoutToken.TokenType;
import com.example.jwtv1.user.Role;
import com.example.jwtv1.user.User;
import com.example.jwtv1.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthenticationService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;
    public ResponseEntity<String> register(RegisterDTO body){

        //Checks if user with the same email already exists in the database
        if (userRepository.findByEmail(body.getEmail()).isPresent()){
            return new ResponseEntity<>("Email taken",HttpStatus.BAD_REQUEST);
        }else {

            //Creates user
            User user = new User(
                    body.getFirstname(),
                    body.getLastname(),
                    body.getEmail(),
                    passwordEncoder.encode(body.getPassword()),
                    Role.USER);

            //Adds user to the database
            userRepository.save(user);

            //Generates jwt that is going to be returned to the user
            String jwt = jwtService.generateTokenWithoutExtraClaims(user);

            //Generates this token in order to save the jwt token to the database
            Token token = new Token(jwt, TokenType.BEARER,false,false,user);

            //Saves the token to the database
            tokenRepository.save(token);

            //Returns Jwt and http status 200
            return new ResponseEntity<>(jwt, HttpStatus.OK);
        }
    }

    public ResponseEntity<String> login(LoginDTO body){

        //If user exists in the database
        if (userRepository.findByEmail(body.getEmail()).isPresent()){

            //Then generate an authentication token with this user credentials
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(body.getEmail(),body.getPassword());

            //Authenticate the user
            authenticationManager.authenticate(authenticationToken);

            //Generate jwt token
            String jwt = jwtService.generateTokenWithoutExtraClaims(userRepository.findByEmail(body.getEmail()).get());

            //Generates this token in order to save the jwt token to the database
            Token token = new Token(jwt, TokenType.BEARER,false,false,userRepository.findByEmail(body.getEmail()).get());

            //Revoke all existing tokens before saving the new one to the database
            revokeAllUserTokens(userRepository.findByEmail(body.getEmail()).get());

            //Saves the token to the database
            tokenRepository.save(token);

            //Return jwt token and status code 200
            return new ResponseEntity<>(jwt,HttpStatus.OK);

        }else {
            return new ResponseEntity<>("Authentication has failed", HttpStatus.BAD_REQUEST);
        }

    }

    private void revokeAllUserTokens(User user){
        //Gets all tokens associated with the user
        var validUserTokens = tokenRepository.findAllValidTokensByUser(user.getId());

        //Checks if there are valid tokens
        if (validUserTokens.isEmpty()) {
            return;
        }else {

            //Set expire and revoked to true for all tokens
            validUserTokens.forEach(t -> {
                t.setExpired(true);
                t.setRevoked(true);
            });

            //Save them to the database
            tokenRepository.saveAll(validUserTokens);
        }
    }
}
