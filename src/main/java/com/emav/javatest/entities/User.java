package com.emav.javatest.entities;

import java.util.List;

/**
 * Created by Elinam on 02/04/2018.
 */
public class User {
    private String id;
    private String firstName;
    private String lastName;

    private List<Account> accounts;

    public User(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public User copy(User user)  {
        this.setLastName(user.getLastName());
        this.setFirstName(user.getFirstName());

        return this;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", accounts=" + accounts +
                '}';
    }
}
