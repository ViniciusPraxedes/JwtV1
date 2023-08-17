package com.example.jwtv1.logoutToken;

import com.example.jwtv1.user.Role;
import com.example.jwtv1.user.User;
import com.example.jwtv1.user.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class TokenRepositoryTest {

    @Autowired
    private TokenRepository testTokenRepo;
    @Autowired
    private UserRepository testUserRepo;

    @AfterEach
    void deleteAll(){
        testTokenRepo.deleteAll();
        testUserRepo.deleteAll();
    }

    @Test
    void findAllValidTokensByUser_FindValidTokens_ReturnTokens() {
        //Given
        User user = new User("Test","Test","test@hotmail.com","Test", Role.USER);
        testUserRepo.save(user);

        Token token1 = new Token("test1",TokenType.BEARER,false,false,user);
        Token token2 = new Token("test2",TokenType.BEARER,false,false,user);
        Token token3 = new Token("test3",TokenType.BEARER,false,false,user);
        testTokenRepo.save(token1);
        testTokenRepo.save(token2);
        testTokenRepo.save(token3);

        //When
        List<Token> tokens = testTokenRepo.findAllValidTokensByUserId(user.getId());

        //Then
        Assertions.assertThat(tokens.size()).isEqualTo(3);
        Assertions.assertThat(testTokenRepo.findAllValidTokensByUserId(user.getId())).isNotNull();
        Assertions.assertThat(testTokenRepo.findAllValidTokensByUserId(user.getId())).isEqualTo(tokens);
    }

    @Test
    void findByToken_FindToken_ReturnToken() {

        //Given
        User user = new User("Test","Test","test@hotmail.com","Test", Role.USER);
        testUserRepo.save(user);

        Token token1 = new Token("test1",TokenType.BEARER,false,false,user);
        testTokenRepo.save(token1);

        //When
        Token savedToken = testTokenRepo.findByToken(token1.getToken()).get();

        //Then
        Assertions.assertThat(savedToken).isEqualTo(token1);

    }
}