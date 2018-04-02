package com.emav.javatest.services;

import com.emav.javatest.entities.User;
import com.emav.javatest.ressources.TestDataSource;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Elinam on 02/04/2018.
 */
public class UserService {
    private DataService dataService = new DataService();

    private User _updateUser(User updatedUser) {
        List<User> users = TestDataSource.users.stream()
                .filter(user -> user.getId().equals(updatedUser.getId()))
                .map(u -> u.copy(updatedUser)).collect(Collectors.toList());
        return users.size() > 0 ? users.get(0) : null;
    }

    private User _deleteUser(User deletedUser) {
        if (deletedUser == null)
            return null;

        if (deletedUser.getAccounts() != null)
            TestDataSource.accounts.removeAll(deletedUser.getAccounts());

        TestDataSource.users.removeIf(user -> user.getId().equals(deletedUser.getId()));
        return deletedUser;
    }

    public User insertUser(User user) {
        return dataService.doAction(
                () -> {
                    if (user.getId() != null && findUserById(user.getId()) == null) {
                        TestDataSource.users.add(user);
                        return user;
                    }
                    return null;
                },
                u -> System.out.println("User u : " + (u != null ? u.toString() : "no user. Null error") + " was created")
                ,e -> System.err.println("Error: " + e.getMessage()));
    }

    public void updateUser(String userId, User user) {
        dataService.doAction(
                () -> {
                    if (userId != null) {
                        user.setId(userId);
                        return _updateUser(user);
                    }

                    return null;
                },
                u -> System.out.println("User u : " + (u != null ? u.toString() : "no user. Null error") + " was updated"),
                e -> System.err.println("Error: " + e.getMessage()));
    }

    public void deleteUser(String userId) {
        dataService.doAction(
                () -> {
                    if (userId != null) {
                        User userToDelete = findUserById(userId);
                        return _deleteUser(userToDelete);
                    }

                    return null;
                },
                u -> System.out.println("User u : " + (u != null ? u.toString() : "no user. Null error") + " was deleted"),
                e -> System.err.println("Error: " + e.getMessage()));
    }

    public User findUserById(String userId) {
        List<User> users = TestDataSource.users.stream()
                .filter(user -> user.getId().equals(userId))
                .collect(Collectors.toList());
        return users.size() > 0 ? users.get(0) : null;
    }


}
