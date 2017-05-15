# ShopsAPI

## Synopsis

Shop Rest API Microservice that provide following functionalities:

* Allow storage of shops in memory. Every time a shop is saved, the service calls the Google Maps API. The Google Maps API responds with the longitude and latitude, which allows the shop data to be updated with longitude and latitude.
* Find closest shop service: providing their current longitude and latitude (e.g. URL request params), and gets back the address, longitude and latitude of the shop nearest to them.

## Requirements

Java 1.8 or later.

## Code Example

You can consume the Rest microservice with any Rest client, I suggest to use Google Postman


### Save Shop

Request
```HTML
POST /shop HTTP/1.1
Host: localhost:8080
Content-Type: application/json
Cache-Control: no-cache
Postman-Token: 70ec546f-8961-3816-d085-afef497bd203

{
  "shopName": "Simply Food 2",
  "shopAdress": {
    "number": "323-324",
    "street": "High Holborn",
    "postCode": "WC1V"
  }
}
```

Respose Body code 200 OK
```json
{
  "shopName": "Simply Food 2",
  "shopAdress": {
    "number": "323-324",
    "street": "High Holborn",
    "postCode": "WC1V"
  },
  "geoPosition": {
    "lat": 51.51801520000001,
    "lng": -0.1123096
  },
  "_links": {
    "self": {
      "href": "http://localhost:8080/shop/Simply%20Food%202"
    }
  }
}
```

### Get Shop

GET request to http://localhost:8080/shop?latitude=51.52156840000001&longitude=-0.1077042

RESPONSE
```html
{
  "shopName": "Simply Food 2",
  "shopAdress": {
    "number": "323-324",
    "street": "High Holborn",
    "postCode": "WC1V"
  }
}
```

## Installation
You can add the library to your project with Gradle.


### Gradle
Pending to deploy in mavenCentral. It would look like this:

```groovy
repositories {
    mavenCentral()
}

dependencies {
    compile 'com.db.challenge:shopsAPI:0.0.1'
    ...
}
```

## Building the Project

    # Compile and package the project
    $ ./gradlew jar


## Tests

Integration and unit tests has been created, the solution could be expanded with BDD, but it would required more time. Validation are performed in the controller with JSR-303 using @Validated annotation. The exceptions have been captured with a CustomHanlder.

To run the test:

    # Run the tests
    $  ./gradlew test
 

## Solution expansion
The solution could be expanded with the following features:

* Persistence with JPA layer.
* JBehave BDD testing.



## Extend solution testing

I would deploy the application in a CI server, such us Jenkins on TeamCicty, In the CI server we could create a task/build to launch the unit Test e integration test, we could would be to deploy the service in a VM/Host.

I would also implement BDD test with Jbehave libray(BDD stories), after deploying with docker I would create a trigger/task to launch the BDD end to end tests.


To control the build stability, we could schedule previous tasks, or create a trigger in the CI server, this trigger could be executed for instance every time that a colleague is pulling something into master branch.

Enough testing coverage would make possible to avoid issues and increase the frequency we are releasing to PROD.


## Integrate the solution into a existing collection of solutions
I recommend to deploy of the microsevices in cloud infrastructure, a private one using spring-cloud, or a public, such as Google cloud platform or AWS.

Those could also be integrated with Mesosphere that would be in charge of the service management/maintenance


## Deployment into production

We can deploy in in cloud infrastructure, a creating containers to be run in VM/HostMachin with docker, and a system of cluster management like OpenShift.

## Author
Francisco San Roman
Please for any question you can contcat me at franciscosanroman@gmail.com

## License

This project is licensed under the GNU General Public License - see the [LICENSE.md](LICENSE.md) file for details
