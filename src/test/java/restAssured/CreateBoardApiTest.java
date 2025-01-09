package restAssured;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import model.Board;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import utility.TestUtil;

import java.io.IOException;
import java.net.URISyntaxException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static utility.TestUtil.BOARD_NAME;
import static utility.TestUtil.TRELLO_URL;


public class CreateBoardApiTest extends RestAssuredBaseTest {

    @BeforeTest
    public void callInitializeResources() throws IOException {
        initializeResources();
    }

    @Test
    public void when_sendValidRequest_then_boardCreated() {
        RequestSpecification request = getBaseRequestSpecification()
                .queryParam("name", BOARD_NAME);

        Response response = request.post("/boards/");
        Board board = response.body().as(Board.class);
        boardId = board.getId();
        //Board board = createBoard(BOARD_NAME);
        response
                .then()
                .statusCode(200)
                .body("id", notNullValue())
                .log().all();
    }

    @Test
    public void when_sendRequestWithInvalidName_then_badRequest() {
        RequestSpecification request = getBaseRequestSpecification()
                .queryParam("name", "");

        Response response = request.post("/boards/");
        Board board = response.body().as(Board.class);
        boardId = board.getId();

        response
                .then()
                .statusCode(400)
                .body("message", equalTo("invalid value for name"))
                .body("error", equalTo("ERROR"))
                .log().all();
    }

    @Test
    public void when_createBoardWithInvalidKey_then_Unauthorized() {
        given()
                .baseUri(TRELLO_URL)
                .queryParam("key", "123")
                .queryParam("token", properties.getProperty(TestUtil.TOKEN))
                .contentType(ContentType.JSON)
                .queryParam("name", "name")
                .when()
                .post("/boards/")
                .then()
                .statusCode(401)
                .body(equalTo("invalid key"))
                .log().all();
    }

    @Test
    public void when_createBoardWithoutKey_then_Unauthorized() {
        given()
                .baseUri(TRELLO_URL)
                .queryParam("key", "")
                .queryParam("token", properties.getProperty(TestUtil.TOKEN))
                .contentType(ContentType.JSON)
                .queryParam("name", "name")
                .when()
                .post("/boards/")
                .then()
                .statusCode(401)
                .body(equalTo("invalid app key"))
                .log().all();
    }

    @Test
    public void when_createBoardWithInvalidToken_then_Unauthorized() {
        given()
                .baseUri(TRELLO_URL)
                .queryParam("key", properties.getProperty(TestUtil.KEY))
                .queryParam("token", "123")
                .contentType(ContentType.JSON)
                .queryParam("name", "board")
                .when()
                .post("/boards/")
                .then()
                .statusCode(401)
                .body(equalTo("invalid app token"))
                .log().all();
    }

    @Test
    public void when_createBoardWithoutToken_then_Unauthorized() {
        given()
                .baseUri(TRELLO_URL)
                .queryParam("key", properties.getProperty(TestUtil.KEY))
                .queryParam("token", "")
                .contentType(ContentType.JSON)
                .queryParam("name", "name")
                .when()
                .post("/boards/")
                .then()
                .statusCode(401)
                .body(equalTo("unauthorized permission requested"))
                .log().all();
    }
}

