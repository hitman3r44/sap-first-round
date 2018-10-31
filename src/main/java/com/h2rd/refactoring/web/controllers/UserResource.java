package com.h2rd.refactoring.web.controllers;

import com.h2rd.refactoring.usermanagement.dao.UserDao;
import com.h2rd.refactoring.usermanagement.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.Set;

/**
 * This a Contorller class of User. It contains all the end points of the REST.
 *
 * @author Sumit Sarkar
 * @version 1.0
 */

@Path("users")
public class UserResource {
    static Logger LOGGER = LoggerFactory.getLogger(UserResource.class);

    @Context
    UriInfo uriInfo;

    @Autowired
    UserDao userDao;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response addUser(@Valid final User user) {
        User foundUser = userDao.findUser(user.getEmail());
        if (foundUser != null) {
            LOGGER.debug("A User with email " + foundUser.getEmail() + " already exist");
            return Response.status(Response.Status.CONFLICT).build();
        }
        userDao.saveUser(user);
        URI uri = uriInfo.getAbsolutePathBuilder().path(user.getEmail()).build();
        return Response.created(uri).entity(user).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{email}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response updateUser(@PathParam("email") String email,
                               @Valid final User user) {
        LOGGER.debug("Updating User " + email);
        User existingUser = userDao.findUser(email);
        if (existingUser == null) {
            LOGGER.debug("User with email " + email + " not found");
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        user.setEmail(email);
        userDao.updateUser(user);
        return Response.ok().entity(user).build();
    }

    @DELETE
    @Path("{email}")
    public Response deleteUser(@PathParam("email") String email) {
        LOGGER.debug("Deleting User " + email);
        User existingUser = userDao.findUser(email);
        if (existingUser == null) {
            LOGGER.debug("User with email " + email + " not found");
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        userDao.deleteUser(existingUser);
        return Response.noContent().build();
    }

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getUsers() {
        Set<User> users = userDao.getUsers();
        if (users.isEmpty()) {
            return Response.noContent().build();
        }
        GenericEntity<Set<User>> usersEntity = new GenericEntity<Set<User>>(users) {
        };
        return Response.ok().entity(usersEntity).build();
    }

    @GET
    @Path("{email}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response findUser(@PathParam("email") String email) {
        LOGGER.debug("findUserByEmail=" + email);
        User user = userDao.findUser(email);
        if (user == null) {
            LOGGER.debug("User with email " + email + " not found");
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok().entity(user).build();
    }
}
