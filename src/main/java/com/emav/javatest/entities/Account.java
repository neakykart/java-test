package com.emav.javatest.entities;

import java.util.Date;

/**
 * Created by Elinam on 02/04/2018.
 */
public class Account {
    private String id;
    private int balance;
    private Date createdOn;

    User user;

    public Account(String id, User user) {
        this.id = id;
        this.createdOn = new Date();
        this.balance = 0;
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id='" + id + '\'' +
                ", balance=" + balance +
                ", createdOn=" + createdOn +
                '}';
    }
}
