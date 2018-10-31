package test.com.h2rd.refactoring.integration;

import com.h2rd.refactoring.usermanagement.models.User;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Integration test case (end to end test case) to test UserResource
 * functionality.
 *
 * @author Sumit Sarkar
 */

public class UserResourceIntegrationTest extends BaseEndpointIntegrationTest {
    static final String END_POINT = "/users";

    /**
     * Test find user information
     *
     * @throws Exception
     */
    @Test
    public void getUsers() throws Exception {

        User user = new User();

        user.setEmail("sumit@sarkar.com");
        user.setName("Sumit Sarkar");
        user.setRoles(Arrays.asList("admin", "admin1"));

        Entity<User> userEntity = Entity.entity(user, MediaType.APPLICATION_JSON);

        Response response = target(END_POINT).request().accept(MediaType.APPLICATION_JSON).post(userEntity);

        assertEquals(END_POINT + "/" + user.getEmail(), URLDecoder.decode(new URL(response.getHeaderString("Location")).getPath(), "utf-8"));

        User createdUser = response.readEntity(User.class);

        assertEquals(user.getEmail(), createdUser.getEmail());
        assertEquals(user.getName(), createdUser.getName());
        assertEquals(user.getRoles(), createdUser.getRoles());

        response = target("users").request().accept(MediaType.APPLICATION_JSON).get();

        List<User> users = response.readEntity(new GenericType<List<User>>() {});
        assertEquals(1, users.size());

        User foundUser = users.get(0);

        assertEquals(user.getEmail(), foundUser.getEmail());
        assertEquals(user.getName(), foundUser.getName());
        assertEquals(user.getRoles(), foundUser.getRoles());

        response.close();
    }

    /**
     * Test: Trying to add user with invalid email address
     */

    @Test
    public void addUserInvalidEmailInput() {

        User user = new User();

        user.setName("Sumit Sarkar");
        user.setRoles(Arrays.asList("admin", "admin1"));

        Entity<User> userEntity = Entity.entity(user, MediaType.APPLICATION_JSON);

        //put email as null
        user.setEmail(null);
        Response response = target(END_POINT).request().accept(MediaType.APPLICATION_JSON).post(userEntity);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());

        //empty email
        user.setEmail("");
        response = target(END_POINT).request().accept(MediaType.APPLICATION_JSON).post(userEntity);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());


        //invalid email address
        user.setEmail("23@");
        response = target(END_POINT).request().accept(MediaType.APPLICATION_JSON).post(userEntity);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());

        //invalid email address
        user.setEmail("we@2323.");
        response = target(END_POINT).request().accept(MediaType.APPLICATION_JSON).post(userEntity);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    /**
     * Test: trying to add user with invalid input
     */
    @Test
    public void addUserInvalidNameInput() {

        User user = new User();
        user.setEmail("sumit@sarkar.com");
        user.setRoles(Arrays.asList("admin", "admin1"));

        Entity<User> userEntity = Entity.entity(user, MediaType.APPLICATION_JSON);

        //put in name field as null
        user.setName(null);
        Response response = target(END_POINT).request().accept(MediaType.APPLICATION_JSON).post(userEntity);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());

        //empty name
        user.setName("");
        response = target(END_POINT).request().accept(MediaType.APPLICATION_JSON).post(userEntity);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    /**
     * Test: Adding user by addind invalid roles
     */
    @Test
    public void addUserInvalidRolesInput() {

        User user = new User();

        user.setEmail("sumit@sarkar.com");
        user.setName("Sumit Sarkar");

        Entity<User> userEntity = Entity.entity(user, MediaType.APPLICATION_JSON);

        //roles as empty
        user.getRoles().clear();
        Response response = target(END_POINT).request().accept(MediaType.APPLICATION_JSON).post(userEntity);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }
}
