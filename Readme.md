# CarIQ technical challenge

Test over mock Pets API, validating response codes, Json schema and 
certain parameters.

## Installation

```sh
git clone git@github.com:Ivan-Castellanos/CarIQCodeChallenge.git
```

## Usage example

First build the gradle dependencies of the project:
```sh
./gradlew dependencies
```

To execute Dockerized WireMock run this command on the root folder of the project:
```sh
docker run -it --rm \
 -p 8080:8080 \
 --name wiremock \ 
 -v $PWD/src/main/resources/stub:/home/wiremock \
 wiremock/wiremock:2.35.0
```

Then to execute the test suite run on the command line:
```sh
./gradlew test
```

## The test suite

This suite is composed of 4 test

- Post test with assertions over the schema, the headers, and the response code 201 created.
- Put test with assertions over the schema, the headers, and the response code 200 ok.
- Get test with assertions over parameters, headers and the response code 200 ok.
- Get test made to fail, waiting for a code expecting 400 but receiving 200 ok(this was a misconfiguration over the https://petstore.swagger.io as well).
