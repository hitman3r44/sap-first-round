package com.h2rd.refactoring.web;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;

/**
 * This a Resource Config class of this application. It contains the properties of this application.
 *
 * @author Sumit Sarkar
 * @version 1.0
 */

public class MyApplication extends ResourceConfig {

    public MyApplication() {
        register(RequestContextFilter.class);
        register(JacksonFeature.class);
        packages(true, "com.h2rd.refactoring.web.controllers");
    }
}
