# task-list-REST
## Topics in information systems and programming languages, Spring 2022, BGU
This project is a simple RESTful API for managing task lists for people. The main goal of the project was to experience Play framework using Java,
and produce a discussion in class about pros and cons of the language+framework, focusing on the questions:
* What makes the language/framework different/interesting?
* What parts did we like/hate?
* Does our system protects the data it stores from being corrupted? how? did the technology stack help?
* How did we map entities from JSON to the data store and back? did our technology stack help?
* Reading entities from the data store - how bad was this? did the language/framework help?
* Performing summation queries on the data store (i.e. active tasks per person) - how bad was it? did the language/frameork help?

## Getting Started
TODO

## High Level System Design
<img width="1432" alt="ציור1" src="https://user-images.githubusercontent.com/63551039/166100013-f354062d-8a91-41eb-bede-ff0e17bc0c9f.png">

* The base server URL is http://server-host:9000/api/.
* The client sends a request to the server (for example: http://localhost:9000/api).
* All request which go to the persons URLs (for example: http://localhost:9000/api/people/some-endpoint), will be handled (using Play routing file) by the
  relevant method in the `PersonsController`.
* Similarly, all requests which go to the tasks URLs (for example: http://localhost:9000/api/tasks/some-endpoint), will be handled (using Play routing file)
  by the relevant method in the `TasksController`.
* Each controller's method transfers the appropriate data (after some validations) from the conroller layer to the service layer. So `PersonsController`
  transfers controll to the `PersonsService`, and `TasksController` transfers controll to the `TasksService`.
* Using Play Ebean plugin, the service layer communicates with an embedded H2 tabular DB (if needed), and returns results (DTOs, booleans, exceptions and so on)
  back to the controller layer, and then an HTTP response is sent back to the client according to the buisness logic.
  
## API Documentation
The entire API documentation can be found [here](https://mbarsinai.com/files/bgu/2022a/miniproj/swagger/#/ "API docs"). A few extra points which aren't mentioned in the above doc:
* For any request which requires an `application/json` media type request body, but has a different media type, a `415` unsupported media type is returned.
* When using the `PATCH` method to update a task, a user may change the `TaskType` completely. However, if one wishes to do so (for exmaple: convert `Chore` to `HomeWork`),
  all of the fields for the new `TaskType` must be part of the request body JSON. Failing to do so will return a `400` bad request.
* Enum values: `Status`, `TaskType`, `Size` can be sent as part of the request without worrying about casing, as the server handles them case-insensitive.

## Low Level System Design
### Controllers & Services
There are 2 types of controllers in the controller layer: `PersonsController` which handles routes related to persons, and `TasksController` which handles routes related to tasks.
Each of these controllers communicates with the corresponding service (`PersonsService`, `TasksService`). When a request reaches a controller's method (see the Routes section for details), a specific server logic is implemented to handle the request with the help of the appropriate service in the service layer,
and return an object of type `Result`. the `Result` type is provieded by Play and consists of all of the HTTP status codes. This makes returning the desired status code according to the server's logic result extremely simple.

Note that some of the methods have the `@BodyParser.of(BodyParser.Json.class)` annotation. These methods receive HTTP request body, so Play offers this handy annotation to control the received media types. In our case, if the media type is not JSON, a `415` status code is automatically returned to the user (even before the method starts running).

For implementation specific documentation, please go to the relveant controller/service and see the JavaDocs written for each method there.

### Routing
In the `conf.routes` file, all the routing is configured. Play uses the definitions in this file to route requests to the controller layer. each line in this file represents a route, and includes 3 parts:
* The route's HTTP method (`GET`, `POST`, `PATCH`, `DELETE`, `PUT`, etc.)
* The route's endpoint (for example: `/api/people`)
* The path to the conroller's method to handle requests sent to this route (for example: `controllers.PersonsController.getAll`).

For routes with path variables, like the get person route, the configuration will look as follows:
`GET     /api/people/:id                         controllers.PersonsController.getPerson(id: String)`
we can see that this route is used for `GET` request to the `/api/people/:id` endpoint, where the `/:id` stands for a path variable. This way Play knows that this part of the actual request URL is the person's id to get.
Finally, the last part tells Play to handle the request using the `getPerson` method in the `PersonsController`, which receives the passed `id` string as a variable.
Obviously, the method's signature must match the signature in the route file.

Note that if there are optional query parameters, Play again makes life easy. For example, in the get user tasks route, where the `status` is an optional query parameter, the route will be:
`GET     /api/people/:id/tasks                   controllers.PersonsController.getPersonTasks(id: String, status: String ?=null)`
Here the `status` query parameter is not mentioned in the endpoint URL, but as part of the method's signature, and we also tell Play that if the parameter wasn't passed, it should put the value `null` in the `status` argument. This way we can check in the controller whether `status == null` and determine the behavior easily.

For routes with request body, like the add person route, the configuration will look as folloes:
`POST    /api/people                             controllers.PersonsController.createPerson(request: Request)`
We can see that the `createPerson` method in the `PersonsController` gets an argument called request of type `Request`, which is a type provided by Play. Play knows to automatically pass the request's body to this method using this configuration, which makes things very simple.

---

### Entities
In the `app.models` package, there are 2 classes which represent the entities of the system. These entities hold the data from the H2 DB as a Java object and are mapped to and from the DB using Play Ebean plugin:
<img width="1000" height="700" alt="ציור2" src="https://user-images.githubusercontent.com/63551039/166109147-1b56c32e-b878-41ed-9dec-7fade694c3e8.png">

Each entity class extends the `Model` class which is part from Play Ebean, and annotated with `javax.persistence` `@Entity` annotation. This makes the class inherit some extremely useful methods to work with the H2 DB,
such as `save()`, `insert()`, `delete()`, `update()` and more. 

* `Person` entity class represents a person saved in the DB. 
  * The `id` field is annotated using `javax.persitence` `@Id` annotation, which tells Play Ebean that this field is the primary key of the persons table.
  * This entity, alongside the person fields `name`, `email`, `favoriteProgrammingLanguage`, also hold a `List<Task>` field of the tasks which the person owns.
    This field is annotated using the `@OneToMany(mappedBy=owner, cascade=CascadeType.ALL)` from `javax.persistence`. This annotation tells Play Ebean that it should "listen" to all actions done to a person in the system,
    and apply it to the many tasks it owns.
    For example: if a person is deleted from the system, using the `person.delete()` Play Ebean method, Ebean knows to also delete all the tasks which the user owns (with the help of the `OneToMany` annotation in the `Task` entity class).
  * Another useful `javax.persistence` annotation used by Play Ebean is the `@Column(nullable=false)` which verifies the the fields cannot be `null` (and if such operation is tried, an exception is thrown), and the `@Column(unique=true)` which is used for verifying that the emails are unique in the system.
    The `unique=true` property is very helpful as it simplifies the verification process of adding a new person to the system (Play Ebean handles the uniqueness verification for us behind the scenes, all we do is catch the exception and return the relevant status code).
  
* `Task` entity class represents a task saved in the DB.
  * The `id` field is annotated using `javax.persitence` `@Id` annotation, which tells Play Ebean that this field is the primary key of the tasks table.
  * The `owner` field which is of type `Person`, is annotated with `javax.persistence` `@ManyToOne` and `@JoinColumn(name="ownerId", nullable=false)` which tells Play Ebean that this field hold a many-to-one relation with the persons table,
    and the foreign key should be the person's id. This annotation alognside the annotation described above for the `Person` class, ensures the relation is maintained by Ebean so we won't have to handle it ourselves.
  * The `type` field indicates the type of task:
    * `type`=`Chore` => the fields `description` and `size` will be populated, while the fields `course`, `dueDate` and `details` will be `null`. This behavior is maintained in the server logic, and ensures no data corruption may occur.
    * `type`=`HomeWork` => the fields `course`, `dueDate` and `details` will be populated, while the fields `description` and `size` will be `null`. This behavior is maintained in the server logic, and ensures no data corruption may occur.


Play Ebean also provides a `Finder` object which is very useful in running find queries with the DB,
and converting the results to the proper entity class.
For example:
`Person` class static `Finder` instance is used when calling the get person details API, using the code `Person.find.byId(id)`. The result will be `null` if no such person exists, or a `Person` instance of the person whose id was given.

The package `app.enums` contains Enums used in the system for `Size`, `TaskType` and `Status`, which are described in the above image.

### DTOs
In the `app.dtos` package, there are multiple DTO classes which are:
<img width="1000" height="700" alt="ציור2" src="https://user-images.githubusercontent.com/63551039/166109548-941003b0-3023-40d2-96ef-bcc9c70dad98.png">
The DTO classes are used for transferring data from the request to the service layer of the app and back. This is done using Play's object-JSON bidirectional mapping capability found in `play.libs.Json`.
It offers methods such as `Json.fromJson(request.body().asJson(), PersonDto.class)` to convert HTTP requests intercepted by Play to a matching DTO/entity class,
or `Json.toJson(personDto)` to map DTOs and entities to JSON and send it in the response easily. Here's a short explenation on each DTO class:
* `TaskDto` - a DTO class which is inherited by the `HomeWorkDto` and `ChoreDto` classes, and holds the `id`, `type`, `ownerId` and `status` fields (which are common to all task types).
* `ChoreDto` - extends the `TaskDto` class with the relevant fields for chore task type: `description` and `size`.
* `HomeWorkDto` -  extends the `TaskDto` class with the relevant fields for homework task type: `course`, `dueDate` and `details`.
* `TaskUpdateDto` - used for the update task API. Because the user doesn't have to specify the task type on an update call (as all fields are OPTIONAL in this request), the server can't expect which fields it will get, so we try to get all of them. Then, using the `type` field, the DTO is mapped to the correct DTO (`ChoreDto` or `HomeWorkDto`) and handled accordingly.
* `PersonDto` - a DTO class to transfer a person's details from and to the server. 

### Database
This app uses an embedded `H2` in-file database to persist the app's data. The following diagram describes the tables and relations:
<img width="1000" height="700" alt="ציור2" src="https://user-images.githubusercontent.com/63551039/166112453-b76017f9-5d46-48de-bd30-7c25fd21c902.png">

As you can see, there are 2 tables in the DB - Persons table and Tasks table, which have a one-to-many relationship. Java's JDBC H2 driver is used to connect the DB to the app, and configuration is done in the `conf.application.conf` file.
The Play Ebean plugin is used for mapping entities to and from the DB as Java objects as described in the Entities section.
Play uses evolutions, which is Play's way to generate sql scripts to build the DB for the first time, simply by observing the entities defined in the system:
* In the `application.conf` file, we defined `ebean.default=["models.*"]` which tells Ebean to look at all the classes under the `models` package and try figuring out which entities are defined there.
* Ebean scans the package, and sees that `Person` and `Task` are defined as `@Entity` and extends the `Model` class. Thus, it creates an evolutions script which automatically runs on system startup for the first time.
* The sql script can be written manually as well. It can be found under the folder `conf.evolutions.default` by the name `1.sql`. Ebean is smart enough to handle all the DB configuration in terms of:
  * Declaring tables to create, including fields for each table, field types, constraints, primary keys and so on.
  * Defining relationships between tables using indexes and and foreign keys.
* This behavior is possible with the smooth integration between `javax.persistence` annotations and Ebean.

---

### Exceptions
In the `app.exceptions` package you can find 2 types of custom exceptions: `InvalidPersonException` and `InvalidTaskException` which are used in the server to indicate something went wrong (expectedly) and deliver an indicative message which oftenly propogates to the server's response.

### Utilities
In the `app.utils` package, many helpful utility classes can be found:
* `Constans` - a class which holds `static` strings which are widely used in the system, such as field names, URLs, patterns and more.
* `MsgGenerator` - a class which holds `static` methods to generate common messages used in the system.
* `PersonConverter` - a class which holds `static` methods to convert `Person` entity instances to `PersonDto` instances, and also a `List<Person>` instances to `List<PersonDto>` instances.
* `TaskConverter` - a class which holds `static` methods to convert `Task` entity instances to DTOs and vice-versa.
* `Validators` - a class which holds `static` methods used for different validations throughout the system. Such validations include email format, date format, all fields are present in the request and more.

### Dependencies
* This project uses Java's project Lombok for making coding easier in terms of implicit getters, setters, constructors and builder design pattern.
* This project uses Apache commons validator to validate email and date formats easily without the need to implement our own methods for that purpose.
