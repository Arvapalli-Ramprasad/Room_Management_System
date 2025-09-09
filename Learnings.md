🔹 What is JPA?
===============

JPA (Java Persistence API) is a specification (a set of rules/interfaces) that defines how Java objects (entities) should be mapped to database tables.

👉 Think of JPA as a set of guidelines. It does not do the actual database work — it just says "If you want to map Java objects to tables, here’s the standard way."

Example in your project:
========================

@Entity
@Table(name = "rooms")
public class Room {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

    private String roomNumber;
    private int capacity;
    private int floor;
}


Here:

@Entity, @Id, @GeneratedValue → are JPA annotations.

These just define rules for mapping class → database table.

But JPA alone does not know how to execute SQL — it needs a provider (implementation).

🔹 What is Hibernate?
======================

Hibernate is the most popular implementation of JPA.

👉 Think of Hibernate as the worker that actually does the job of connecting to the database, generating SQL, and executing it, based on the rules defined by JPA.

Hibernate looks at your entity class (Room)

Creates SQL queries (INSERT INTO rooms ...)

Talks to the database (MySQL) through JDBC driver

Returns results as Java objects

@SpringBootApplication → Runs your backend, combining everything.
=

@SpringBootConfiguration → Define custom beans (like password encoder, CORS config).
=
@EnableAutoConfiguration → Automatically sets up DB connections, JPA, DispatcherServlet.
=
@ComponentScan → Finds your controllers (UserController, RoomController, ExpenseController) and services.*/
=

===================================================================================================================================

Something about @Repo layer
==========================

You only write interfaces.

Spring Data (MongoDB/JPA) creates proxy implementations at runtime.

Those proxies internally use MongoTemplate (for MongoDB) or EntityManager (for JPA/Hibernate) to do the real DB work.


Rest and Restful Difference
===========================
REST API → Any API that uses HTTP and exposes resources.

RESTful API → A REST API that strictly follows REST principles (stateless, proper URIs, correct HTTP verbs, standard status codes).