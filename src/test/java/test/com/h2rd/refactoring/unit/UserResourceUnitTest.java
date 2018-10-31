package test.com.h2rd.refactoring.unit;

import com.h2rd.refactoring.usermanagement.dao.UserDao;
import com.h2rd.refactoring.usermanagement.models.User;
import com.h2rd.refactoring.web.controllers.UserResource;
import org.glassfish.jersey.uri.internal.JerseyUriBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * UNIT test case for UserResource.java Class
 *
 * @author Sumit Sarkar
 */

@RunWith(MockitoJUnitRunner.class)
public class UserResourceUnitTest {

    @Mock
    UserDao userDao;

    @Mock
    UriInfo uriInfo;

    @InjectMocks
    UserResource userResource;

    @Test
    public void getUsers() {
        User user = new User("sumit@sarkar.com", "Sumit", Arrays.asList("admin"));

        Set<User> users = new HashSet<User>(Arrays.asList(user));
        when(userDao.getUsers()).thenReturn(users);

        Response response = userResource.getUsers();
        assertEquals(HttpStatus.OK.value(), response.getStatus());

        List<User> outputUsers = new ArrayList((Set<User>) response.getEntity());

        assertEquals(1, outputUsers.size());
        assertEquals(user.getName(), outputUsers.get(0).getName());
        assertEquals(user.getEmail(), outputUsers.get(0).getEmail());
        assertEquals(user.getRoles(), outputUsers.get(0).getRoles());
    }

    @Test
    public void getUsersNoUsersFound() {

        when(userDao.getUsers()).thenReturn(new HashSet<User>());
        Response response = userResource.getUsers();
        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());
        assertNull("expecting null", response.getEntity());
    }

    @Test
    public void deleteUserNoUserFound() {

        String email = "sumit@sarkar.com";
        when(userDao.findUser(email)).thenReturn(null);
        Response response = userResource.deleteUser(email);
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertNull("expecting null", response.getEntity());
    }

    @Test
    public void deleteUser() {

        String email = "sumit@sarkar.com";

        User user = new User("test@test.com", "test", Arrays.asList("role1"));
        when(userDao.findUser(email)).thenReturn(user);

        Response response = userResource.deleteUser(email);
        verify(userDao).deleteUser(user);

        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());
        assertNull("expecting null", response.getEntity());
    }

    @Test
    public void findUser() {

        String email = "sumit@sarkar.com";

        User user = new User("test@test.com", "test", Arrays.asList("role1"));
        when(userDao.findUser(email)).thenReturn(user);

        Response response = userResource.findUser(email);
        assertEquals(HttpStatus.OK.value(), response.getStatus());

        User outputUser = (User) response.getEntity();

        assertEquals(user.getName(), outputUser.getName());
        assertEquals(user.getEmail(), outputUser.getEmail());
        assertEquals(user.getRoles(), outputUser.getRoles());
    }

    @Test
    public void findUserNotFound() {

        String email = "sumit@sarkar.com";
        when(userDao.findUser(email)).thenReturn(null);

        Response response = userResource.findUser(email);
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertNull("expecting null", response.getEntity());
    }

    @Test
    public void updateUserNotFound() {

        String email = "sumit@sarkar.com";
        User user = new User("test@test.com", "test", Arrays.asList("role1"));
        when(userDao.findUser(email)).thenReturn(null);

        Response response = userResource.updateUser(email, user);
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertNull("expecting null", response.getEntity());
    }

    @Test
    public void updateUser() {
        String email = "sumit@sarkar.com";
        User user = new User("test2@test.com", "new name", Arrays.asList("role1", "roles2", "roles3"));
        when(userDao.findUser(email)).thenReturn(user);
        Response response = userResource.updateUser(email, user);

        ArgumentCaptor<User> argument = ArgumentCaptor.forClass(User.class);
        verify(userDao).updateUser(argument.capture());

        User updatedUser = argument.getValue();
        assertEquals(user.getName(), updatedUser.getName());
        assertEquals(user.getEmail(), updatedUser.getEmail());
        assertEquals(user.getRoles(), updatedUser.getRoles());

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        User outputUser = (User) response.getEntity();
        assertSame(updatedUser, outputUser);
    }

    @Test
    public void addUserFound() {
        User user = new User("sumit@sarkar.com", "new name", Arrays.asList("role1", "roles2", "roles3"));

        when(userDao.findUser(user.getEmail())).thenReturn(user);

        Response response = userResource.addUser(user);

        assertEquals(HttpStatus.CONFLICT.value(), response.getStatus());
        assertNull("expecting null", response.getEntity());
    }

    @Test
    public void addUser() {
        User user = new User("sumit@sarkar.com", "new name", Arrays.asList("role1", "roles2", "roles3"));
        when(userDao.findUser(user.getEmail())).thenReturn(null);

        UriBuilder uriBuilder = new JerseyUriBuilder();
        when(uriInfo.getAbsolutePathBuilder()).thenReturn(uriBuilder);
        Response response = userResource.addUser(user);

        ArgumentCaptor<User> argument = ArgumentCaptor.forClass(User.class);
        verify(userDao).saveUser(argument.capture());

        User addedUser = argument.getValue();
        assertEquals(user.getName(), addedUser.getName());
        assertEquals(user.getEmail(), addedUser.getEmail());
        assertEquals(user.getRoles(), addedUser.getRoles());

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertEquals(1, response.getHeaders().size());
        assertEquals(uriBuilder.toString(), response.getHeaderString("Location"));
        User outputUser = argument.getValue();
        assertSame(addedUser, outputUser);
    }
}