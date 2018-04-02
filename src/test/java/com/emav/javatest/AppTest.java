package com.emav.javatest;

import com.emav.javatest.entities.User;
import com.emav.javatest.ressources.TestDataSource;
import com.emav.javatest.services.DataService;
import com.emav.javatest.services.UserService;
import junit.framework.TestCase;

import java.util.UUID;

/**
 * Created by Elinam on 02/04/2018.
 */
public class AppTest extends TestCase {
    private DataService dataService = new DataService();
    private UserService userService = new UserService();

    public void testUserCRUD() {

        User userA = new User(UUID.randomUUID().toString());
        User userB = new User(UUID.randomUUID().toString());
        userService.insertUser(userA);
        userService.insertUser(userB);

        assertNotNull(userService.findUserById(userA.getId()));
        assertNotNull(userService.findUserById(userB.getId()));

        User _updatedUser = new User("");
        _updatedUser.setFirstName("Moulal");
        _updatedUser.setLastName("Bany");

       userService.updateUser(userA.getId(), _updatedUser);

       _updatedUser.setLastName("Patatra");
       userService.updateUser(userB.getId(), _updatedUser);

        assertEquals("Moulal",  userService.findUserById(userA.getId()).getFirstName());
        assertEquals("Patatra",  userService.findUserById(userB.getId()).getLastName());

        userService.deleteUser(userA.getId());
        userService.deleteUser(userB.getId());

        assertNull(userService.findUserById(userA.getId()));

        assertTrue(TestDataSource.users.size() == 0);
        assertTrue(TestDataSource.accounts.size() == 0);
    }
}
