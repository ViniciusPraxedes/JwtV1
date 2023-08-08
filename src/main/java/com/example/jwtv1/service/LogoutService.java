package com.example.jwtv1.service;

import com.example.jwtv1.logoutToken.TokenRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Service
public class LogoutService implements LogoutHandler {

    @Autowired
    private TokenRepository tokenRepository;
    private HandlerExceptionResolver exceptionResolver;

    public LogoutService(@Qualifier("handlerExceptionResolver")HandlerExceptionResolver exceptionResolver) {
        this.exceptionResolver = exceptionResolver;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;

        try{
            if(authHeader == null || !authHeader.startsWith("Bearer ")){
                throw new MalformedJwtException("Jwt is non existent");
            }

            jwt = authHeader.substring(7);
            var storedToken = tokenRepository.findByToken(jwt).orElse(null);
            if (storedToken != null && !storedToken.isRevoked() && !storedToken.isExpired()){
                storedToken.setExpired(true);
                storedToken.setRevoked(true);
                tokenRepository.save(storedToken);
            }else {
                throw new MalformedJwtException("Jwt is invalid");
            }
        }catch (ExpiredJwtException | MalformedJwtException e){
        exceptionResolver.resolveException(request, response, null, e);
    }


    }
}
