## Hospital Waiting List ##

This project combines and processes multiple data sources (mainly CSV files) into one and stores it in a database.
The combined information is exposed through different REST endpoints.

### What are those data sources? ###

Currently there are two type of CSV files which are fetched from these links:
  * [OP Waiting List By Group Hospital](https://data.gov.ie/dataset/op-waiting-list-by-group-hospital)
  * [IPDC Waiting List By Group Hospital](https://data.gov.ie/dataset/ipdc-waiting-list-by-group-hospital)

These files contain information about the Inpatient, Day Case and Outpatient lists.
The OP Waiting List report shows the total number of patients waiting, across the various time bands, for a first appointment at a consultant-led Outpatient clinic.
The IPDC Waiting List report shows the total number of patients waiting, across the various time bands, for Inpatient and Day case treatment in each Specialty.
More information can be found on the links above.

### How to set up the application? ###

The project is configured with [PostgreSQL](https://www.postgresql.org/) database and the parameters can be changed in the `application.yml.template` (make sure to rename the file to `application.yml`)

Update these parameters to suit your needs:
```
    url: jdbc:postgresql://localhost:5432/database
    username: postgres
    password: password
```

### Development ###

In case you want to open the project in the IntelliJ IDEA, first run: `gradlew cleanIdea idea`. That will create the necessary files for the IDE.

### Building the project ###

The recommended minimum Java version is 1.8.0_144

To build it on windows run the `gradlew clean build` in the `hospital-waiting-list` folder, which will build all three projects (model, business, web).

On linux you might have to set the execution flag on the gradlew file first, otherwise you get a permission denied error. To do that, run: `chmod +x gradlew`

Once the project is built, navigate to `hospital-waiting-list-web/build/libs/` and run:

`java -jar hospital-waiting-list-web-<version>-SNAPSHOT.jar`

That will start up the application and create the necessary tables in the previously defined database.

### How can I access the REST endpoints? ###

You can either check out the controllers, or once the application is up and running, you can navigate to <your-root-url>/hospital-waiting-list/swagger-ui.html where you can find all the endpoints and information about them.

### Code coverage ###

To check the code coverage run the command: `gradlew test jacocoTestReport` or if you already ran the tests, then the `test` can be left out from the command.

You can find the html output file in `${buildDir}/reports/jacoco/html/index.html` and the xml file in `${buildDir}/reports/jacoco/xml/resport.xml` for each projects.