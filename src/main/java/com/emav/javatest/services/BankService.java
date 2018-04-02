package com.emav.javatest.services;

import com.emav.javatest.entities.Account;
import com.emav.javatest.entities.User;
import com.emav.javatest.exceptions.BankErrorCode;
import com.emav.javatest.exceptions.BankException;
import com.emav.javatest.ressources.TestDataSource;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Elinam on 02/04/2018.
 */
public class BankService {
    private DataService dataService = new DataService();

    /**
     * Create user account
     *
     * @param user user object
     * @return
     */
    public Account createUserAccount(User user) throws BankException {
        if (user == null) {
            throw new BankException("User is null", BankErrorCode.USER_IS_NULL_ERROR);
        }

        return dataService.doAction(
                () -> {
                    if (user.getId() != null) {
                        Account account = new Account(TestDataSource.generateAccountId(), user);
                        user.getAccounts().add(account);
                        TestDataSource.accounts.add(account);
                        return account;
                    }

                    return null;
                },
                o -> System.out.println("Account : " + (o != null ? o.toString() : "no account") + " was created")
                , e -> System.err.println("Error: " + e.getMessage()));
    }

    /**
     * Find the user account using account id
     *
     * @param accountId user account id
     * @return
     */
    public Account findUserAccountById(String accountId) {
        List<Account> accounts = TestDataSource.accounts.stream()
                .filter(account -> account.getId().equals(accountId))
                .collect(Collectors.toList());
        return accounts.size() > 0 ? accounts.get(0) : null;
    }

    /**
     * Find all user accounts
     *
     * @param user user object
     * @return
     */
    public List<Account> findUserAccounts(User user) throws BankException {
        if (user == null) {
            throw new BankException("User is null", BankErrorCode.USER_IS_NULL_ERROR);
        }

        return TestDataSource.accounts.stream()
                .filter(account -> {
                    if (account.getUser() == null)
                        return false;

                    return account.getUser().getId().equals(user.getId());
                })
                .collect(Collectors.toList());
    }

    /**
     * Update account balance value in storage
     *
     * @param accountId account id
     * @param balance new balance value
     */
    public void updateAccountBalance(String accountId, int balance) {
        dataService.doAction(
                () -> {
                    if (accountId != null) {
                        Account storedAccount = findUserAccountById(accountId);
                        if(storedAccount != null) {
                            storedAccount.setBalance(balance);
                            return storedAccount;
                        }

                        return null;
                    }

                    return null;
                },
                a -> System.out.println("Account acc : " + (a != null ? a.toString() : "no account. Null error")+ " was updated"),
                e -> System.err.println("Error: " + e.getMessage()));
    }

    /**
     * Deposit money in the user account
     *
     * @param account user account object
     * @param value value to add to the balance
     *
     * @throws BankException
     */
    public void depositMoneyInAccount(Account account, int value) throws BankException {
        if (account == null) {
            throw new BankException("Account is null", BankErrorCode.ACCOUNT_IS_NULL_ERROR);
        }

        if (value < 0)
            throw new BankException("Negative money", BankErrorCode.NEGATIVE_MONEY_VALUE_ERROR);

        updateAccountBalance(account.getId(), account.getBalance() + value);
    }

     /**
     * Withdraw money in the user account
     *
     * @param account user account object
     * @param value value to withdraw from the balance
     *
     * @throws BankException
     */
    public void withdrawFromAccount(Account account, int value) throws BankException {
        if (account == null) {
            throw new BankException("Account is null", BankErrorCode.ACCOUNT_IS_NULL_ERROR);
        }

        if (value < 0)
            throw new BankException("Negative money", BankErrorCode.NEGATIVE_MONEY_VALUE_ERROR);

        updateAccountBalance(account.getId(), account.getBalance() - value);
    }

    /**
     * Remove user account from storage
     *
     * @param user user object
     * @param accountId user account id
     */
    public void deleteUserAccount(User user, String accountId) throws BankException {
        if (user == null) {
            throw new BankException("User is null", BankErrorCode.USER_IS_NULL_ERROR);
        }

        dataService.doAction(
                () -> {
                    if (accountId != null) {
                        Account storedAccount = findUserAccountById(accountId);
                        if(storedAccount != null && storedAccount.getUser().equals(user)) {
                            TestDataSource.accounts.removeIf(account -> account.getId().equals(accountId));
                            storedAccount.getUser().getAccounts().removeIf(account -> account.getId().equals(accountId));
                            return storedAccount;
                        }

                        return null;
                    }

                    return null;
                },
                a -> System.out.println("Account acc : " + (a != null ? a.toString() : "no account. Null error")+ " was deleted"),
                e -> System.err.println("Error: " + e.getMessage()));
    }

    /**
     * Calculate user wealth
     *
     * @param user user object
     * @return
     */
    public int getUserWealth(User user) throws BankException {
        if (user == null) {
            throw new BankException("User is null", BankErrorCode.USER_IS_NULL_ERROR);
        }

        return TestDataSource.accounts.stream()
                .filter(account -> {
                    if (account.getUser() == null)
                        return false;

                    return account.getUser().getId().equals(user.getId());
                })
                .mapToInt(Account::getBalance)
                .sum();
    }
}
