package restAssured;

import io.restassured.http.ContentType;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import utility.TestUtil;
import java.io.IOException;
import java.net.URISyntaxException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static utility.TestUtil.BOARD_NAME;
import static utility.TestUtil.TRELLO_URL;

public class DeleteBoardApiTest extends RestAssuredBaseTest {

    @BeforeTest
    public void callInitializeResources() throws IOException {
        initializeResources();
    }

    @BeforeMethod
    public void setUp() throws URISyntaxException, IOException {
        board = createBoard(BOARD_NAME);
        System.out.println("Board has been created.");
    }

    @Test
    public void when_deleteBoard_then_BordNotFound() {

        getBaseRequestSpecification()
                .pathParam("id", board.getId())
                .when()
                .delete("/boards/{id}")
                .then()
                .statusCode(200)
                .body("_value", equalTo(null))
                .log().all();

    }

    @Test
    public void when_deleteBoardWithInvalidId_then_badRequest() {

        getBaseRequestSpecification()
                .pathParam("id", "123")
                .when()
                .delete("/boards/{id}")
                .then()
                .statusCode(400)
                .body(equalTo("invalid id"))
                .log().all();

    }

    @Test
    public void when_deleteBoardWithInvalidKey_then_Unauthorized() {
        given()
                .baseUri(TRELLO_URL)
                .pathParam("id", board.getId())
                .queryParam("key", "123")
                .queryParam("token", properties.getProperty(TestUtil.TOKEN))
                .contentType(ContentType.JSON)
                .when()
                .delete("/boards/{id}")
                .then()
                .statusCode(401)
                .body(equalTo("invalid key"))
                .log().all();
    }
    @Test
    public void when_deleteBoardWithoutKey_then_Unauthorized() {
        given()
                .baseUri(TRELLO_URL)
                .pathParam("id", board.getId())
                .queryParam("key", "")
                .queryParam("token", properties.getProperty(TestUtil.TOKEN))
                .contentType(ContentType.JSON)
                .when()
                .delete("/boards/{id}")
                .then()
                .statusCode(401)
                .body(equalTo("invalid app key"))
                .log().all();
    }

    @Test
    public void when_deleteBoardWithInvalidToken_then_Unauthorized() {
        given()
                .baseUri(TRELLO_URL)
                .pathParam("id", board.getId())
                .queryParam("key", properties.getProperty(TestUtil.KEY))
                .queryParam("token", "123")
                .contentType(ContentType.JSON)
                .when()
                .delete("/boards/{id}")
                .then()
                .statusCode(401)
                .body(equalTo("invalid app token"))
                .log().all();
    }
    @Test
    public void when_deleteBoardWithoutToken_then_Unauthorized() {
        given()
                .baseUri(TRELLO_URL)
                .pathParam("id", board.getId())
                .queryParam("key", properties.getProperty(TestUtil.KEY))
                .queryParam("token", "")
                .contentType(ContentType.JSON)
                .when()
                .delete("/boards/{id}")
                .then()
                .statusCode(401)
                .body(equalTo("unauthorized permission requested"))
                .log().all();
    }
}
