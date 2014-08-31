package com.dmitriy.mongorest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;

@Configuration
@EnableMongoRepositories
@Import(RepositoryRestMvcConfiguration.class)
@EnableAutoConfiguration
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}



/**
*The main() method defers to the SpringApplication helper class, 
*providing Application.class as an argument to its run() method. 
*This tells Spring to read the annotation metadata from Application 
*and to manage it as a component in the Spring application context.
*
*The @EnableMongoRepositories annotation activates Spring Data MongoDB. 
*Spring Data MongoDB will create a concrete implementation of the PersonRepository
*and configure it to talk to a MongoDB database using the Cypher query language.
*
*Spring Data REST is a Spring MVC application. 
*The @Import(RepositoryRestMvcConfiguration.class) annotation imports 
*a collection of Spring MVC controllers, JSON converters, and other beans 
*needed to provide a RESTful front end. These components link up 
*to the Spring Data MongoDB backend.
*
*The @EnableAutoConfiguration annotation switches on reasonable default behaviors
*based on the content of your classpath. For example, because the application 
*depends on the embeddable version of Tomcat (tomcat-embed-core.jar), 
*a Tomcat server is set up and configured with reasonable defaults on your behalf. 
*And because the application also depends on Spring MVC (spring-webmvc.jar), 
*a Spring MVC DispatcherServlet is configured and registered for you — no web.xml necessary!
*Auto-configuration is a powerful, flexible mechanism. 
*/