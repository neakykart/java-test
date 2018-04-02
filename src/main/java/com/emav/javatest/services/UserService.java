package com.emav.javatest.services;

import com.emav.javatest.entities.User;
import com.emav.javatest.exceptions.BankErrorCode;
import com.emav.javatest.exceptions.BankException;
import com.emav.javatest.ressources.TestDataSource;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Elinam on 02/04/2018.
 */
public class UserService {
    private DataService dataService = new DataService();

    /**
     * Update user data in the storage
     *
     * @param updatedUser user object to update
     * @return
     */
    private User _updateUser(User updatedUser) {
        List<User> users = TestDataSource.users.stream()
                .filter(user -> user.getId().equals(updatedUser.getId()))
                .map(u -> u.copy(updatedUser)).collect(Collectors.toList());
        return users.size() > 0 ? users.get(0) : null;
    }

    /**
     * Remove user from storage
     *
     * @param deletedUser user object to delete
     * @return
     */
    private User _deleteUser(User deletedUser) throws BankException {
        if (deletedUser == null) {
            throw new BankException("User is null", BankErrorCode.USER_IS_NULL_ERROR);
        }

        if (deletedUser.getAccounts() != null)
            TestDataSource.accounts.removeAll(deletedUser.getAccounts());

        TestDataSource.users.removeIf(user -> user.getId().equals(deletedUser.getId()));
        return deletedUser;
    }

    /**
     * Store user
     *
     * @param user user object to store
     * @return
     */
    public User insertUser(User user) throws BankException {
        if (user == null) {
            throw new BankException("User is null", BankErrorCode.USER_IS_NULL_ERROR);
        }

        return dataService.doAction(
                () -> {
                    if (user.getId() != null && findUserById(user.getId()) == null) {
                        TestDataSource.users.add(user);
                        return user;
                    }
                    return null;
                },
                u -> System.out.println("User u : " + (u != null ? u.toString() : "no user. Null error") + " was created")
                , e -> System.err.println("Error: " + e.getMessage()));
    }

    /**
     * Update user in storage with user object
     *
     * @param userId user id
     * @param user   contains user new attribute values
     */
    public void updateUser(String userId, User user) throws BankException {
        if (user == null) {
            throw new BankException("User is null", BankErrorCode.USER_IS_NULL_ERROR);
        }

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

    /**
     * Remove user from storage
     *
     * @param userId user id
     */
    public void deleteUser(String userId) {
        dataService.doAction(
                () -> {
                    if (userId != null) {
                        User userToDelete = findUserById(userId);
                        try {
                            return _deleteUser(userToDelete);
                        } catch (BankException e) {
                            return null;
                        }
                    }

                    return null;
                },
                u -> System.out.println("User u : " + (u != null ? u.toString() : "no user. Null error") + " was deleted"),
                e -> System.err.println("Error: " + e.getMessage()));
    }

    /**
     * Search user in storage by user Id
     *
     * @param userId user id
     * @return
     */
    public User findUserById(String userId) {
        List<User> users = TestDataSource.users.stream()
                .filter(user -> user.getId().equals(userId))
                .collect(Collectors.toList());
        return users.size() > 0 ? users.get(0) : null;
    }


}
