package org.cynic.data_query.it;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.cynic.data_query.domain.http.post.CreateStoreItemHttp;
import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class StoreItemApiIT extends BaseIT {

    @Test
    public void givenGoodIdAndGoodFilterWhenGetThenOk() {
        String id = "POST_2";
        String query = "EQUAL(id,\"" + id + "\")";

        RestAssured.given()
            .queryParam("query", query)
            .when()
            .get("/store")
            .then()
            .statusCode(HttpStatus.OK.value())
            .assertThat()
            .body("size()", Is.is(1))
            .body("[0].id", Is.is(id))
            .body("[0].title", Is.is("Title of the second storeItem"))
            .body("[0].content", Is.is(
                "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed."))
            .body("[0].views", Is.is(3))
            .body("[0].timestamp", Is.is(456));

    }

    @Test
    public void givenGoodIdsAndGoodFilterWhenGetThenOk() {
        String query = "AND(NOT(GREATER_THAN(views, 2)),   OR(EQUAL(id, \"POST_2\"),  EQUAL(id,\"POST_3\")))";

        RestAssured.given()
            .queryParam("query", query)
            .when()
            .get("/store")
            .then()
            .statusCode(HttpStatus.OK.value())
            .assertThat()
            .body("size()", Is.is(1))
            .body("[0].id", Is.is("POST_3"))
            .body("[0].title", Is.is("Title of the third storeItem"))
            .body("[0].content", Is.is(
                "In publishing and graphic design, Lorem ipsum is a placeholder text commonly used to demonstrate the visual form of a document or a typeface without relying on meaningful content. Lorem ipsum may be used as a placeholder before the final copy is available."))
            .body("[0].views", Is.is(1))
            .body("[0].timestamp", Is.is(789));
    }

    @Test
    public void givenGoodIdsExclusiveFilterWhenGetThenOk() {
        String query = "AND(EQUAL(id, \"POST_2\"),  EQUAL(id,\"POST_3\"))";

        RestAssured.given()
            .queryParam("query", query)
            .when()
            .get("/store")
            .then()
            .statusCode(HttpStatus.OK.value())
            .assertThat()
            .body("size()", Is.is(0));
    }

    @Test
    public void givenBadGrammarFilterWhenGetThenError() {
        String query = "AND(asdadsd)";

        RestAssured.given()
            .queryParam("query", query)
            .when()
            .get("/store")
            .then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .assertThat()
            .body("size()", Is.is(2))
            .body("code", Is.is("error.antlr.parse"))
            .body("values.size()", Is.is(1))
            .body("values.message", Is.is("token recognition error at: 'a'"));
    }

    @Test
    public void givenEmptyFilterWhenGetThenOk() {
        String query = "";

        RestAssured.given()
            .queryParam("query", query)
            .when()
            .get("/store")
            .then()
            .statusCode(HttpStatus.OK.value())
            .assertThat()
            .body("size()", Is.is(3))
            .body("id", Matchers.containsInAnyOrder("POST_1", "POST_2", "POST_3"))
            .body("views", Matchers.containsInAnyOrder(3, 1, 13))
            .body("timestamp", Matchers.containsInAnyOrder(123, 456, 789))
            .body("title", Matchers.containsInAnyOrder(
                "Title of the first storeItem",
                "Title of the second storeItem",
                "Title of the third storeItem")
            );
    }

    @Test
    public void givenNoFilterWhenGetThenOk() {
        RestAssured.given()
            .when()
            .get("/store")
            .then()
            .statusCode(HttpStatus.OK.value())
            .assertThat()
            .body("size()", Is.is(3))
            .body("id", Matchers.containsInAnyOrder("POST_1", "POST_2", "POST_3"))
            .body("views", Matchers.containsInAnyOrder(3, 1, 13))
            .body("timestamp", Matchers.containsInAnyOrder(123, 456, 789))
            .body("title", Matchers.containsInAnyOrder(
                "Title of the first storeItem",
                "Title of the second storeItem",
                "Title of the third storeItem")
            );
    }

    @Nested
    class DataModification {

        @Test
        public void givenNewPostWhenPostThenOk() {
            CreateStoreItemHttp http = Instancio.create(CreateStoreItemHttp.class);

            RestAssured.given()
                .when()
                .contentType(ContentType.JSON)
                .body(http)
                .post("/store")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("size()", Is.is(1))
                .body("id", Is.is(http.id()));
        }

        @Test
        public void givenExistingPostWhenPostThenOk() {
            CreateStoreItemHttp http = Instancio.of(CreateStoreItemHttp.class)
                .set(Select.field("id"), "POST_1")
                .create();

            RestAssured.given()
                .when()
                .contentType(ContentType.JSON)
                .body(http)
                .post("/store")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("size()", Is.is(1))
                .body("id", Is.is(http.id()));
        }

    }
}
