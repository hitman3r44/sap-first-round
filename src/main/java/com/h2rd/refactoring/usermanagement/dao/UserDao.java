package com.h2rd.refactoring.usermanagement.dao;

import com.h2rd.refactoring.usermanagement.models.User;

import java.util.Set;

/**
 * <p>
 * This interface provide the get, save, update and delete user
 * function. It's easy for sub-class to change implementation strategy
 * based on different data source.
 * <p>
 *
 * @author Sumit Sarkar
 * @version 1.0
 */

public interface UserDao {

    void saveUser(User user);

    Set<User> getUsers();

    void deleteUser(User user);

    void updateUser(User user);

    User findUser(String email);

    User findUserByName(String name);

    Set<User> findUsers(String name);
}
