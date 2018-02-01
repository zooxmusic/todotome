package com.todotome.user;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserRepository {

    public static Map<String, User> fakeUserRepository = new HashMap<String, User>();

    UserRepository() {
        User user = new User();
        user.setAddress("Somewhere");
        user.setLastName("Szucs");
        user.setMail("contact@brianszucs.com");
        user.setName("Brian");
        user.setPassword("bszucs");
        fakeUserRepository.put("contact@brianszucs.com", user);
    }
    public User findByLogin(String userid) {
        return fakeUserRepository.get(userid);
    }




}