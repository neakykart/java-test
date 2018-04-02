package com.emav.javatest.ressources;

import com.emav.javatest.entities.Account;
import com.emav.javatest.entities.User;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Elinam on 02/04/2018.
 */
public class TestDataSource {
    public static List<User> users = new LinkedList<>();
    public static List<Account> accounts = new LinkedList<>();

    /**
     * Generate unique ID for account creation
     * @return
     */
    public static String generateAccountId() {
        return "ACC"+UUID.randomUUID().toString();
    }

    /**
     * Generate test users
     *
     * @param n number to generate
     * @return
     */
    public static List<User> getTestUsers(int n) {
        List<User> users = new LinkedList<>();

        while(n > 0) {
            User user = new User(UUID.randomUUID()+"user"+n);
            user.setLastName("Neak"+n);
            user.setFirstName("Elinam"+n);

            users.add(user);
            n--;
        }

        return users;
    }
}
