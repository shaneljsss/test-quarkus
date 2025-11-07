package com.example.command;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;

import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
class CommandResourceTest {

    @Nested
    @DisplayName("Happy path commands")
    class HappyPath {

        @Test
        void cloneRepositoryRequestProducesSuccessPayload() {
            given()
                    .contentType("application/json")
                    .body("""
                            {
                              \"command\": \"CLONE_REPOSITORY\",
                              \"repository\": \"https://example.com/repo.git\",
                              \"options\": {
                                \"depth\": \"1\"
                              }
                            }
                            """)
                    .when()
                    .post("/commands")
                    .then()
                    .statusCode(200)
                    .body("status", equalTo("SUCCESS"))
                    .body("message", containsString("Clone"))
                    .body("payload.repository", equalTo("https://example.com/repo.git"))
                    .body("payload.options.depth", equalTo("1"));
        }

        @Test
        void createBranchRequestProducesExpectedResponse() {
            given()
                    .contentType("application/json")
                    .body("""
                            {
                              \"command\": \"CREATE_BRANCH\",
                              \"repository\": \"https://example.com/repo.git\",
                              \"branch\": \"feature/test\"
                            }
                            """)
                    .when()
                    .post("/commands")
                    .then()
                    .statusCode(200)
                    .body("status", equalTo("SUCCESS"))
                    .body("message", containsString("Branch creation"))
                    .body("payload.branch", equalTo("feature/test"));
        }

        @Test
        void bumpVersionRequestProducesVersionPayload() {
            given()
                    .contentType("application/json")
                    .body("""
                            {
                              \"command\": \"BUMP_VERSION\",
                              \"repository\": \"https://example.com/repo.git\",
                              \"version\": \"1.2.3\"
                            }
                            """)
                    .when()
                    .post("/commands")
                    .then()
                    .statusCode(200)
                    .body("status", equalTo("SUCCESS"))
                    .body("message", containsString("Version bump"))
                    .body("payload.version", equalTo("1.2.3"));
        }
    }

    @Nested
    @DisplayName("Validation")
    class Validation {

        @Test
        void missingRequiredFieldsReturnBadRequest() {
            given()
                    .contentType("application/json")
                    .body(Map.of("command", "CREATE_BRANCH"))
                    .when()
                    .post("/commands")
                    .then()
                    .statusCode(400)
                    .body("status", equalTo("ERROR"))
                    .body("message", containsString("required"))
                    .body("payload", nullValue());
        }

        @Test
        void unknownCommandIsReportedAsError() {
            given()
                    .contentType("application/json")
                    .body("""
                            {
                              \"command\": \"SOMETHING_ELSE\"
                            }
                            """)
                    .when()
                    .post("/commands")
                    .then()
                    .statusCode(400)
                    .body("status", equalTo("ERROR"))
                    .body("message", containsString("Unsupported"));
        }
    }
}
