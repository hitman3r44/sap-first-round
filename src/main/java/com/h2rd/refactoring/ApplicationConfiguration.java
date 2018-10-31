package com.h2rd.refactoring;

import com.h2rd.refactoring.usermanagement.dao.UserDao;
import com.h2rd.refactoring.usermanagement.dao.UserDaoImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * This a Resource Config class of this application. It contains the properties of this application.
 *
 * @author Sumit Sarkar
 * @version 1.0
 */

@Configuration
@ComponentScan(basePackages = {"com.h2rd.refactoring.usermanagement"})
public class ApplicationConfiguration {
    @Bean
    Object testService() {
        return new Object();
    }

    @Bean
    UserDao testUserDao() {
        return new UserDaoImpl();
    }
}
