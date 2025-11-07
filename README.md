# test-quarkus

A Quarkus application exposing a JSON based command interface. Clients POST command definitions to `/commands` to request
operations such as cloning a repository, creating a branch, or bumping a version. The service returns a structured response
that indicates whether the command was accepted and echoes the relevant payload details.

## Running the tests

The test suite uses `@QuarkusTest` together with RestAssured to exercise the command endpoint with realistic JSON payloads.
Each scenario submits a different command body to ensure the resource routes requests to the correct command handler and
returns the expected JSON response.

```bash
mvn test
```

> **Note:** The evaluation environment used for this repository does not have access to Maven Central. Running the tests locally
> requires internet access the first time so Maven can download the Quarkus platform dependencies.
