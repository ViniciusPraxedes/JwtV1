package com.example.jwtv1.user;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserRepositoryTest {
     @Autowired
     private UserRepository testRepo;

     @AfterEach
     void deleteAll(){
         testRepo.deleteAll();
     }

    @Test
    void findByEmail_FindUserByEmail_ReturnUser() {

        //Given
        String email = "test@hotmail.com";
        User user = new User("Test","Test",email,"Test",Role.USER);
        testRepo.save(user);

        //When
        User savedUser = testRepo.findByEmail(email).get();

        //Then
        Assertions.assertThat(savedUser).isEqualTo(user);

    }
    @Test
    void findByEmail_FindUserByEmail_ReturnNull(){

        //Given
        String email = "test@hotmail.com";
        User user = new User("Test","Test",email,"Test",Role.USER);

        //When
        boolean exists = testRepo.findByEmail(email).isPresent();

        //Then
        Assertions.assertThat(exists).isFalse();
    }
}