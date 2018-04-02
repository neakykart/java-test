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

    public Account createUserAccount(User user) {
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

    public Account findUserAccountById(String accountId) {
        List<Account> accounts = TestDataSource.accounts.stream()
                .filter(account -> account.getId().equals(accountId))
                .collect(Collectors.toList());
        return accounts.size() > 0 ? accounts.get(0) : null;
    }

    public List<Account> findUserAccounts(User user) {
        return TestDataSource.accounts.stream()
                .filter(account -> {
                    if (account.getUser() == null)
                        return false;

                    return account.getUser().getId().equals(user.getId());
                })
                .collect(Collectors.toList());
    }

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

    public void depositMoneyInAccount(Account account, int value) throws BankException {
        if (value < 0)
            throw new BankException("Negative money", BankErrorCode.NEGATIVE_MONEY_VALUE_ERROR);

        updateAccountBalance(account.getId(), account.getBalance() + value);
    }

    public void withdrawFromAccount(Account account, int value) throws BankException {
        if (value < 0)
            throw new BankException("Negative money", BankErrorCode.NEGATIVE_MONEY_VALUE_ERROR);

        updateAccountBalance(account.getId(), account.getBalance() - value);
    }

    public void deleteUserAccount(User user, String accountId) {
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

    public int getUserWealth(User user) {
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
