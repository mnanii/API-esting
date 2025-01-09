package restAssured;

import io.restassured.http.ContentType;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import utility.TestUtil;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static utility.TestUtil.BOARD_NAME;
import static utility.TestUtil.TRELLO_URL;

public class GetBoardByIdApiTest extends RestAssuredBaseTest {

    @BeforeTest
    public void callInitializeResources() throws IOException {
        initializeResources();
    }

    @BeforeMethod
    public void setUp(){
        board = createBoard(BOARD_NAME);
        System.out.println("Board has been created.");
    }

    @Test
    public void when_getBoard_then_ValidRequest() {
        getBaseRequestSpecification()
                .pathParam("id", board.getId())
                .when()
                .get("/boards/{id}")
                .then()
                .statusCode(200)
                .body(notNullValue())
                .log().all();
    }

    @Test
    public void when_getBoardWithInvalidId_then_badRequest() {
        getBaseRequestSpecification()
                .pathParam("id", "123")
                .when()
                .get("/boards/{id}")
                .then()
                .statusCode(400)
                .body(equalTo("invalid id"))
                .log().all();
    }

    @Test
    public void when_getBoardWithInvalidKey_then_Unauthorized() {
        given()
                .baseUri(TRELLO_URL)
                .pathParam("id", board.getId())
                .queryParam("key", "123")
                .queryParam("token", properties.getProperty(TestUtil.TOKEN))
                .contentType(ContentType.JSON)
                .when()
                .get("/boards/{id}")
                .then()
                .statusCode(401)
                .body(equalTo("invalid key"))
                .log().all();
    }

    @Test
    public void when_getBoardWithoutKey_then_Unauthorized() {
        given()
                .baseUri(TRELLO_URL)
                .pathParam("id", board.getId())
                .queryParam("key", "")
                .queryParam("token", properties.getProperty(TestUtil.TOKEN))
                .contentType(ContentType.JSON)
                .when()
                .get("/boards/{id}")
                .then()
                .statusCode(401)
                .body(equalTo("invalid app key"))
                .log().all();
    }

    @Test
    public void when_getBoardWithInvalidToken_then_Unauthorized() {
        given()
                .baseUri(TRELLO_URL)
                .pathParam("id", board.getId())
                .queryParam("key", properties.getProperty(TestUtil.KEY))
                .queryParam("token", "123")
                .contentType(ContentType.JSON)
                .when()
                .get("/boards/{id}")
                .then()
                .statusCode(401)
                .body(equalTo("invalid app token"))
                .log().all();
    }

    @Test
    public void when_getBoardWithoutToken_then_Unauthorized() {
        given()
                .baseUri(TRELLO_URL)
                .pathParam("id", board.getId())
                .queryParam("key", properties.getProperty(TestUtil.KEY))
                .queryParam("token", "")
                .contentType(ContentType.JSON)
                .when()
                .get("/boards/{id}")
                .then()
                .statusCode(401)
                .body(equalTo("unauthorized permission requested"))
                .log().all();
    }
}
