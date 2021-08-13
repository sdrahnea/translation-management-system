# Translation Management System

Translation Management System helps to manage the projects related to translation business. The main areas which are covered:
1. Project management: edit/update/delete/view all kind of information related projects. Create a sub-projects. Handle the projects or sub-projects statuses and all related information.
2. Translators management: edit/update/delete/view all kind of information related translators. 
3. Client management: edit/update/delete/view all kind of information related clients. 
4. Archive: shows all archived projects. 
5. Database: allow admin only to import clients or translators based on excel / csv files.
6. Users: allow to manage application users. Admin can create the following user's groups: client, translator, manager and admin. Every user's group has a role (access level) and can manage the information related to it's self only. Admin is able to manage any kind of information. 
7. Settings: allow admin only to manage all system variables such as countries, currencies, transaltion areas, etc.
8. Invoices: shows all project which has invoiced status.  
9. Not sent email: shows causes of emails which were not sent. 
10. Applicant: shows all translators which applied or want to join to the system.
11. Statistics: shows all financiar results based on projects. 

## Screenshots
<img src = "/src/main/resources/screenshots/project-list.png">
<img src = "/src/main/resources/screenshots/project-page.png">
<img src = "/src/main/resources/screenshots/project-details-page.png">
<img src = "/src/main/resources/screenshots/client-page.png">
<img src = "/src/main/resources/screenshots/translator-page.png">
<img src = "/src/main/resources/screenshots/archive-list.png">
<img src = "/src/main/resources/screenshots/database-import-page.png">
<img src = "/src/main/resources/screenshots/users-page.png">
<img src = "/src/main/resources/screenshots/settings-translation-are-page.png">
<img src = "/src/main/resources/screenshots/invoices-list.png">
<img src = "/src/main/resources/screenshots/not-sent-email-list.png">
<img src = "/src/main/resources/screenshots/applicant-list.png">
<img src = "/src/main/resources/screenshots/statistics-page.png">

## Summary
* Getting Started (Prerequisites, Installing)
* Running the tests
* Deployment
* Built With
* Do you have any issue?
* Contributing
* Versioning
* Authors
* License
* Donation

## Getting Started

Clone or download a copy of this project.

### Prerequisites

This project requires Java 1.8, Apache Tomcat, MySQL and Maven.

### Database installation

#### 1 H2
No installation is required.
The `spring.datasource.url` is the one required property which should be set. By default, the 
username is `sa` with empty password. Two modes: in memory and file storage. See the `application.properties`
file for more details related configuration.

#### 2 MySQL 

```
CREATE DATABSE tms;
```

Note: in case that you run the application starting with MySQL 8.0.4, please execute the following query:
```
ALTER USER '${USER}'@'localhost' IDENTIFIED WITH mysql_native_password BY '${PASSWORD}';
-- where ${USER} and ${PASSWORD} should be provided. 
```

#### 3 Postgres
Install PostgreSQL. it is required to create a database:

Please, run the following commands if it is the case:
```
createuser -U postgres -s Progress
```

Please, run the following command to import a database (if it is the case):
```
pg_restore -d DATABASE_NAME <  PATH/BACKUP_FILE_NAME.sql
```

To create the JAR file please use the following command:
```
mvn clean package
```

### Installing

After MySQL instalation, it is required to create a dabase:

```
CREATE DATABSE tms;
```

For the first time, when the application will start then all tables and related infortion (project's status, translation status, payment method, education degree, etc) will be created automaticaly. 
Go to downloaded folder and create the build (you should have something similar like the following):
```
SDR:translation-management-system sdrahnea$ mvn clean compile package
[INFO] Scanning for projects...
[WARNING] 
[WARNING] Some problems were encountered while building the effective model for com.tms:translation-management-system:war:2.1.0-SNAPSHOT
[WARNING] 'build.plugins.plugin.(groupId:artifactId)' must be unique but found duplicate declaration of plugin org.apache.maven.plugins:maven-compiler-plugin @ line 248, column 21
[WARNING] 
[WARNING] It is highly recommended to fix these problems because they threaten the stability of your build.
[WARNING] 
[WARNING] For this reason, future Maven versions might no longer support building such malformed projects.
[WARNING] 
[INFO] 
[INFO] ---------------< com.tms:translation-management-system >----------------
[INFO] Building TranslationManagementSystem 2.1.0-SNAPSHOT
[INFO] --------------------------------[ war ]---------------------------------
[INFO] 
[INFO] --- maven-clean-plugin:2.5:clean (default-clean) @ translation-management-system ---
[INFO] Deleting /my-projects/translation-management-system/target
[INFO] 
[INFO] --- maven-dependency-plugin:2.6:copy (default) @ translation-management-system ---
[INFO] 
[INFO] --- maven-resources-plugin:2.6:resources (default-resources) @ translation-management-system ---
[INFO] Using 'UTF-8' encoding to copy filtered resources.
[INFO] Copying 8 resources
[INFO] 
[INFO] --- maven-compiler-plugin:3.1:compile (default-compile) @ translation-management-system ---
[INFO] Changes detected - recompiling the module!
[INFO] Compiling 175 source files to /my-projects/translation-management-system/target/classes
[WARNING] /my-projects/translation-management-system/src/main/java/com/tms/model/entity/dao/EntityDaoImp.java: Some input files use unchecked or unsafe operations.
[WARNING] /my-projects/translation-management-system/src/main/java/com/tms/model/entity/dao/EntityDaoImp.java: Recompile with -Xlint:unchecked for details.
[INFO] 
[INFO] --- maven-dependency-plugin:2.6:copy (default) @ translation-management-system ---
[INFO] 
[INFO] --- maven-resources-plugin:2.6:resources (default-resources) @ translation-management-system ---
[INFO] Using 'UTF-8' encoding to copy filtered resources.
[INFO] Copying 8 resources
[INFO] 
[INFO] --- maven-compiler-plugin:3.1:compile (default-compile) @ translation-management-system ---
[INFO] Nothing to compile - all classes are up to date
[INFO] 
[INFO] --- maven-resources-plugin:2.6:testResources (default-testResources) @ translation-management-system ---
[INFO] Using 'UTF-8' encoding to copy filtered resources.
[INFO] skip non existing resourceDirectory /my-projects/translation-management-system/src/test/resources
[INFO] 
[INFO] --- maven-compiler-plugin:3.1:testCompile (default-testCompile) @ translation-management-system ---
[INFO] Nothing to compile - all classes are up to date
[INFO] 
[INFO] --- maven-surefire-plugin:2.12.4:test (default-test) @ translation-management-system ---
[INFO] No tests to run.
[INFO] 
[INFO] --- maven-war-plugin:2.3:war (default-war) @ translation-management-system ---
[INFO] Packaging webapp
[INFO] Assembling webapp [translation-management-system] in [/my-projects/translation-management-system/target/translation-management-system-2.1.0-SNAPSHOT]
[INFO] Processing war project
[INFO] Copying webapp resources [/my-projects/translation-management-system/src/main/webapp]
[INFO] Webapp assembled in [493 msecs]
[INFO] Building war: /my-projects/translation-management-system/target/translation-management-system-2.1.0-SNAPSHOT.war
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  7.109 s
[INFO] Finished at: 2019-05-01T19:57:43+03:00
[INFO] ------------------------------------------------------------------------
SDR:translation-management-system sdrahnea$ 
```

## Running the tests

This project does not have any kind of tests :).

## Deployment

Once the build (the WAR file) is ready the application can be run. Copy the WAR file into server (Apache Tomcat, GlassFish Server, etc) directory and start your server.
If was used the default configuration then the application should be available at this url: http://localhost:8081/mytemplate/login.xhtml 
Use the following credentials: username: admin, password: 123.

## Built With

* [Java](https://www.java.com/en/download/) - Java technology allows you to work and play in a secure computing environment. Java allows you to play online games, chat with people around the world, calculate your mortgage interest, and view images in 3D, just to name a few.
* [PrimeFaces](https://www.primefaces.org/) - PrimeFaces is a popular open source framework for JavaServer Faces featuring over 100 components, touch optimized mobilekit, client side validation, theme engine and more.
* [Spring Security](https://spring.io/projects/spring-security) - Spring Security is a powerful and highly customizable authentication and access-control framework. It is the de-facto standard for securing Spring-based applications.
* [Spring Framework](https://spring.io/projects/spring-framework) - The Spring Framework provides a comprehensive programming and configuration model for modern Java-based enterprise applications - on any kind of deployment platform.
* [Apache Tomcat](http://tomcat.apache.org/) - The Apache Tomcat® software is an open source implementation of the Java Servlet, JavaServer Pages, Java Expression Language and Java WebSocket technologies. The Java Servlet, JavaServer Pages, Java Expression Language and Java WebSocket specifications are developed under the Java Community Process.
* [Hibernate](http://hibernate.org/) - Hibernate ORM enables developers to more easily write applications whose data outlives the application process. As an Object/Relational Mapping (ORM) framework, Hibernate is concerned with data persistence as it applies to relational databases (via JDBC).
* [MySQL](https://www.mysql.com/) - MySQL is the world's most popular open source database. Whether you are a fast growing web property, technology ISV or large enterprise, MySQL can cost-effectively help you deliver high performance, scalable database applications.
* [Maven](https://maven.apache.org/) - Apache Maven is a software project management and comprehension tool. Based on the concept of a project object model (POM), Maven can manage a project's build, reporting and documentation from a central piece of information. 

## Do you have any issue?

Please contact via LinkedIn account or drop an email (read [LICENSE.md](LICENSE.md) file) or create an issue into project's space.

## Contributing

Please read [CONTRIBUTING.md](CONTRIBUTING.md) for details on our code of conduct, and the process for submitting pull requests to us.

## Versioning

We use [SemVer](http://semver.org/) for versioning.

## Authors

* **Sergiu Drahnea** - *Initial work* - [LinkedIn](https://www.linkedin.com/in/sergiu-drahnea)

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

## Donation
* [PayPal](https://www.paypal.me/sdrahnea) - any donation is welcomed in case that you was pleased with this work :p
* [EGLD](http://elrond.com/) - Address: `erd1t3t5m8v7862asdh48nq820shsmlmuw9jpm87qw25cvch7djpkapskgq4es`
* [TROY](https://troytrade.com/) - Address: `bnb136ns6lfw4zs5hg4n85vdthaad7hq5m4gtkgf23` and Memo: `100079140`
* [PHB](https://phoenix.global/) - Address: `bnb136ns6lfw4zs5hg4n85vdthaad7hq5m4gtkgf23` and Memo: `100079140`
* [HOT](https://holochain.org/) - Address: `0x1ebfc62e2510f0a0558568223d1d101d0cf074b2`
* [VET](https://www.vechain.org/) - Address: `0x1ebfc62e2510f0a0558568223d1d101d0cf074b2`
* [TRX](https://tron.network/) - Address: `TRe8xSkGqpS73Nhk6bnvW34aiJoRTmZs8N`
* [BTT](https://www.bittorrent.com/token/btt/) - Address: `TRe8xSkGqpS73Nhk6bnvW34aiJoRTmZs8N`

