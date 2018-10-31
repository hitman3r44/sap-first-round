package com.h2rd.refactoring.usermanagement.dao;

import com.h2rd.refactoring.usermanagement.models.User;
import lombok.Getter;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * Data access Object Class, it handles the CRUD operation on user object.
 *
 * @author Sumit Sarkar
 * @version 1.0
 */

@Repository("userDao")
public class UserDaoImpl implements UserDao {

    @Getter
    private Set<User> users = new ConcurrentSkipListSet<User>();

    /**
     * Save User
     *
     * @param user User
     */
    public void saveUser(User user) {
        users.add(user);
    }

    /**
     * Delete a user
     *
     * @param userToDelete User
     */
    public void deleteUser(User userToDelete) {
        for (User user : users) {
            if (user.equals(userToDelete)) {
                users.remove(user);
                break;
            }
        }
    }

    /**
     * Update a user
     *
     * @param userToUpdate User
     */
    public void updateUser(User userToUpdate) {
        for (User user : users) {
            if (user.equals(userToUpdate)) {
                user.setName(userToUpdate.getName());
                user.setRoles(userToUpdate.getRoles());
            }
        }
    }

    /**
     * Find a user
     *
     * @param email String
     * @return User
     */
    public User findUser(String email) {
        for (User user : users) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Find a user by email
     *
     * @param name String
     * @return User
     */
    public User findUserByName(String name) {
        for (User user : users) {
            if (user.getName().equals(name)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Find a users by name
     *
     * @param name String
     * @return Set<User>
     */
    public Set<User> findUsersByName(String name) {
        Set<User> foundUsers = new HashSet<User>();
        for (User user : users) {
            if (user.getName().equals(name)) {
                foundUsers.add(user);
            }
        }
        return foundUsers;
    }
}
