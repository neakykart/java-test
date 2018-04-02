package com.emav.javatest;

import com.emav.javatest.entities.Account;
import com.emav.javatest.entities.User;
import com.emav.javatest.exceptions.BankException;
import com.emav.javatest.ressources.TestDataSource;
import com.emav.javatest.services.BankService;
import com.emav.javatest.services.UserService;
import org.junit.After;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static com.emav.javatest.exceptions.BankErrorCode.ACCOUNT_IS_NULL_ERROR;
import static com.emav.javatest.exceptions.BankErrorCode.NEGATIVE_MONEY_VALUE_ERROR;
import static com.emav.javatest.exceptions.BankErrorCode.USER_IS_NULL_ERROR;
import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;

/**
 * Created by Elinam on 02/04/2018.
 */
public class AppTest  {
    private UserService userService = new UserService();
    private BankService bankService = new BankService();
    private List<User> testUsers = new LinkedList<>();

    public void init() {
        testUsers = TestDataSource.getTestUsers(5);
        testUsers.forEach((u) -> {
            try {
                userService.insertUser(u);
            } catch (BankException e) {
                System.err.println(e.getMessage());
            }
        });
    }

    @Test
    public void testUserCRUD() throws BankException {
        System.out.println("");
        System.out.println("User creation");
        System.out.println("");

        try {
            userService.insertUser(null);
            fail("BankException expected.");
        } catch (BankException expected) {
            assertEquals(USER_IS_NULL_ERROR, expected.getErrorCode());
        }

        User userA = new User(UUID.randomUUID().toString());
        User userB = new User(UUID.randomUUID().toString());
        userService.insertUser(userA);
        userService.insertUser(userB);

        assertNotNull(userService.findUserById(userA.getId()));
        assertNotNull(userService.findUserById(userB.getId()));

        System.out.println("");
        System.out.println("User updates");
        System.out.println("");

        User _updatedUser = new User("");
        _updatedUser.setFirstName("Moulal");
        _updatedUser.setLastName("Bany");

        try {
            userService.updateUser("-1", null);
            fail("BankException expected.");
        } catch (BankException expected) {
            assertEquals(USER_IS_NULL_ERROR, expected.getErrorCode());
        }

        userService.updateUser(userA.getId(), _updatedUser);

        _updatedUser.setLastName("Patatra");
        _updatedUser.setAge(25);
        userService.updateUser(userB.getId(), _updatedUser);

        assertEquals("Moulal", userService.findUserById(userA.getId()).getFirstName());
        assertEquals("Patatra", userService.findUserById(userB.getId()).getLastName());
        assertEquals(25, userService.findUserById(userB.getId()).getAge());

        System.out.println("");
        System.out.println("User deletion");
        System.out.println("");

        userService.deleteUser(userA.getId());
        userService.deleteUser(userB.getId());

        assertNull(userService.findUserById(userA.getId()));
    }



    @Test
    public void testAccountCRUD() throws BankException {
        this.init();

        User user = testUsers.get(0);
        System.out.println("");
        System.out.println("Create user account u : " + user);
        System.out.println("");
        assertNotNull(userService.findUserById(user.getId()));

        // Test exception
        try {
            bankService.createUserAccount(null);
            fail("BankException expected.");
        } catch (BankException expected) {
            assertEquals(USER_IS_NULL_ERROR, expected.getErrorCode());
        }

        Account account = bankService.createUserAccount(user);

        Account storedAccount = bankService.findUserAccountById(account.getId());
        assertNotNull(storedAccount);

        try {
            bankService.findUserAccounts(null);
            fail("BankException expected.");
        } catch (BankException expected) {
            assertEquals(USER_IS_NULL_ERROR, expected.getErrorCode());
        }

        // Test account storage
        assertTrue(bankService.findUserAccounts(user).size() == 1);

        // Test balance initial value
        assertTrue(storedAccount.getBalance() == 0);

        // Test exception
        try {
            bankService.depositMoneyInAccount(null, 10000);
            fail("BankException expected.");
        } catch (BankException expected) {
            assertEquals(ACCOUNT_IS_NULL_ERROR, expected.getErrorCode());
        }

        try {
            bankService.depositMoneyInAccount(account, -1000);
            fail("BankException expected.");
        } catch (BankException expected) {
            assertEquals(NEGATIVE_MONEY_VALUE_ERROR, expected.getErrorCode());
        }

        System.out.println("");
        System.out.println("Deposit money account : " + account);
        System.out.println("");

        bankService.depositMoneyInAccount(account, 1000);
        storedAccount = bankService.findUserAccountById(account.getId());
        assertTrue(storedAccount.getBalance() == 1000);

        // Test exception
        try {
            bankService.withdrawFromAccount(null, 10000);
            fail("BankException expected.");
        } catch (BankException expected) {
            assertEquals(ACCOUNT_IS_NULL_ERROR, expected.getErrorCode());
        }

        try {
            bankService.withdrawFromAccount(account, -1000);
            fail("BankException expected.");
        } catch (BankException expected) {
            assertEquals(NEGATIVE_MONEY_VALUE_ERROR, expected.getErrorCode());
        }

        System.out.println("");
        System.out.println("Withdraw money account : " + account);
        System.out.println("");

        bankService.withdrawFromAccount(account, 1000);
        storedAccount = bankService.findUserAccountById(account.getId());
        assertTrue(storedAccount.getBalance() == 0);

        // Test account deletion
        System.out.println("");
        System.out.println("Account deletion");
        System.out.println("");

        // Test exception
        try {
            bankService.deleteUserAccount(null, account.getId());
            fail("BankException expected.");
        } catch (BankException expected) {
            assertEquals(USER_IS_NULL_ERROR, expected.getErrorCode());
        }

        bankService.deleteUserAccount(user, account.getId());
        assertNull(bankService.findUserAccountById(account.getId()));
        assertTrue(user.getAccounts().size() == 0);
    }

    @Test
    public void testManageSeveralAccounts() throws BankException {
        this.init();

        List<Account> accounts = new LinkedList<>();
        User user = testUsers.get(2);

        System.out.println("");

        accounts.add(bankService.createUserAccount(user));
        accounts.add(bankService.createUserAccount(user));
        accounts.add(bankService.createUserAccount(user));

        List<Account> storedAccounts = bankService.findUserAccounts(user);
        assertTrue(storedAccounts.size() == 3);

        bankService.depositMoneyInAccount(accounts.get(0), 1000);
        bankService.depositMoneyInAccount(accounts.get(1), 500);
        bankService.depositMoneyInAccount(accounts.get(2), 600);

        System.out.println("");
        System.out.println("Get user wealth u : "+ user);
        System.out.println("");

        // Test exception
        try {
            bankService.getUserWealth(null);
            fail("BankException expected.");
        } catch (BankException expected) {
            assertEquals(USER_IS_NULL_ERROR, expected.getErrorCode());
        }

        assertEquals(2100, bankService.getUserWealth(user));
    }

    @After
    public void clean() {
        System.out.println("");
        System.out.println("Clean tests datas");
        System.out.println("");

        testUsers.forEach((u) -> userService.deleteUser(u.getId()));
        assertTrue(TestDataSource.users.size() == 0);
        assertTrue(TestDataSource.accounts.size() == 0);
        testUsers.clear();
    }
}
