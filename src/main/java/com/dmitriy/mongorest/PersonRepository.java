package com.dmitriy.mongorest;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


/**
*
*This repository is an interface and will allow you to perform various 
*operations involving Person objects. It gets these operations by extending 
*MongoRepository, which in turn extends the PagingAndSortingRepositry interface 
*defined in Spring Data Commons.
*
*At runtime, Spring Data REST will create an implementation of this 
*interface automatically. Then it will use the @RepositoryRestResource 
*annotation to direct Spring MVC to create RESTful endpoints at /people.
*/

@RepositoryRestResource(collectionResourceRel = "people", path = "people")
public interface PersonRepository extends MongoRepository<Person, String> {

	List<Person> findByLastName(@Param("name") String name);

}