MongoGradleSpringREST
=====================

REST access to Mongo based on Spring and build with Gradle


This is Spring application that let’s you create and retrieve Person objects stored in a MongoDB NoSQL database using Spring Data REST. Spring Data REST takes the features of Spring HATEOAS and Spring Data MongoDB and combines them together automatically.



INSTALL AND LAUNCH MongoDB

To start mongod:

 sudo service mongod start

To stop mongod:

 sudo service mongod stop

If you use the service command to start and stop, it should be using the configuration file: /etc/mongodb.conf.
Starting mongod from the command line

If you run mongod directly instead of using the service definition, you will also have to specify a configuration file as a command line parameter if you want one to be used:

mongod --config /etc/mongodb.conf

For this guide to work, you must stand up a local MongoDB server.
Installation options could be found at http://docs.mongodb.org/manual/installation/.

After installing it, you need to launch the mongo daemon.
$ mongod
all output going to: /usr/local/var/log/mongodb/mongo.log

The MongoDB client that is also installed can be started from another terminal window by typing:   $ mongo




RUN THE SERVICE

If you are using Gradle, you can run your service at the command line this way:
./gradlew clean build && java -jar build/libs/gs-accessing-mongodb-data-rest-0.1.0.jar

If you are using Maven, you can run your service by typing: 
mvn clean package && java -jar target/gs-accessing-mongodb-data-rest-0.1.0.jar.

You can alternatively run the app directly from Gradle like this:
./gradlew bootRun

With mvn, you can run 
mvn spring-boot:run.

Logging output is displayed. The service should be up and running within a few seconds on
http://localhost:8080.





HOW WE SHOULD TEST THE APPLICATION

Now that the application is running, you can test it. You can use any REST client you wish. The following examples use the *nix tool curl.

First you want to see the top level service.

$ curl http://localhost:8080
{
  "_links" : {
    "people" : {
      "href" : "http://localhost:8080/people{?page,size,sort}",
      "templated" : true
    }
  }
}

Here you get a first glimpse of what this server has to offer. There is a people link located at http://localhost:8080/people. It has some options such as ?page, ?size, and ?sort.
Spring Data REST uses the HAL format for JSON output. It is flexible and offers a convenient way to supply links adjacent to the data that is served.

$ curl http://localhost:8080/people
{
  "_links" : {
    "self" : {
      "href" : "http://localhost:8080/people{?page,size,sort}",
      "templated" : true
    },
    "search" : {
      "href" : "http://localhost:8080/people/search"
    }
  },
  "page" : {
    "size" : 20,
    "totalElements" : 0,
    "totalPages" : 0,
    "number" : 0
  }
}

There are currently no elements and hence no pages. Time to create a new Person!
If you run this guide multiple times, there may be left over data. Refer to the MongoDB shell quick reference to find commands to find and drop your database if you need a fresh start.

$ curl -i -X POST -H "Content-Type:application/json" -d '{  "firstName" : "Frodo",  "lastName" : "Baggins" }' http://localhost:8080/people

HTTP/1.1 201 Created
Server: Apache-Coyote/1.1
Location: http://localhost:8080/people/59847b8e3004990b1af9f229
Content-Length: 0

    -i ensures you can see the response message including the headers. The URI of the newly created Person is shown
    -X POST signals this a POST used to create a new entry
    -H "Content-Type:application/json" sets the content type so the application knows the payload contains a JSON object
    -d '{ "firstName" : "John", "lastName" : "Doe" }' is the data being sent

From this you can query for all people:

$ curl http://localhost:8080/people
{
  "_links" : {
    "self" : {
      "href" : "http://localhost:8080/people{?page,size,sort}",
      "templated" : true
    },
    "search" : {
      "href" : "http://localhost:8080/people/search"
    }
  },
  "_embedded" : {
    "persons" : [ {
      "firstName" : "John",
      "lastName" : "Doe",
      "_links" : {
        "self" : {
          "href" : "http://localhost:8080/people/59847b8e3004990b1af9f229"
        }
      }
    } ]
  },
  "page" : {
    "size" : 20,
    "totalElements" : 1,
    "totalPages" : 1,
    "number" : 0
  }
}

The persons object contains a list with John. Notice how it includes a self link. Spring Data REST also uses Evo Inflector to pluralize the name of the entity for groupings.

You can query directly for the individual record:

$ curl http://localhost:8080/people/59847b8e3004990b1af9f229
{
  "firstName" : "John",
  "lastName" : "Doe",
  "_links" : {
    "self" : {
      "href" : "http://localhost:8080/people/59847b8e3004990b1af9f229"
    }
  }
}

This might appear to be purely web based, but behind the scenes, it is talking to the MongoDB database you started.
In this guide, there is only one domain object. With a more complex system where domain objects are related to each other, Spring Data REST will render additional links to help navigate to connected records.

Find all the custom queries:

$ curl http://localhost:8080/people/search
{
  "_links" : {
    "findByLastName" : {
      "href" : "http://localhost:8080/people/search/findByLastName{?name}",
      "templated" : true
    }
  }
}

You can see the URL for the query including the HTTP query parameter name. If you’ll notice, this matches the @Param("name") annotation embedded in the interface.

To use the findByLastName query, do this:

$ curl http://localhost:8080/people/search/findByLastName?name=Doe
{
  "_embedded" : {
    "persons" : [ {
      "firstName" : "John",
      "lastName" : "Doe",
      "_links" : {
        "self" : {
          "href" : "http://localhost:8080/people/59847b8e3004990b1af9f229"
        }
      }
    } ]
  }
}

Because you defined it to return List<Person> in the code, it will return all of the results. If you had defined it only return Person, it will pick one of the Person objects to return. Since this can be unpredictable, you probably don’t want to do that for queries that can return multiple entries.

You can also issue PUT, PATCH, and DELETE REST calls to either replace, update, or delete existing records.

$ curl -X PUT -H "Content-Type:application/json" -d '{ "firstName": "Bill", "lastName": "Bo" }' http://localhost:8080/people/59847b8e3004990b1af9f229
$ curl http://localhost:8080/people/59847b8e3004990b1af9f229
{
  "firstName" : "Bill",
  "lastName" : "Bo",
  "_links" : {
    "self" : {
      "href" : "http://localhost:8080/people/59847b8e3004990b1af9f229"
    }
  }
}

$ curl -X PATCH -H "Content-Type:application/json" -d '{ "firstName": "Dick" }' http://localhost:8080/people/59847b8e3004990b1af9f229
$ curl http://localhost:8080/people/59847b8e3004990b1af9f229
{
  "firstName" : "Dick",
  "lastName" : "Bo",
  "_links" : {
    "self" : {
      "href" : "http://localhost:8080/people/59847b8e3004990b1af9f229"
    }
  }
}

PUT replaces an entire record. Fields not supplied will be replaced with null. PATCH can be used to update a subset of items.

You can delete records:

$ curl -X DELETE http://localhost:8080/people/59847b8e3004990b1af9f229
$ curl http://localhost:8080/people
{
  "_links" : {
    "self" : {
      "href" : "http://localhost:8080/people{?page,size,sort}",
      "templated" : true
    },
    "search" : {
      "href" : "http://localhost:8080/people/search"
    }
  },
  "page" : {
    "size" : 20,
    "totalElements" : 0,
    "totalPages" : 0,
    "number" : 0
  }
}



