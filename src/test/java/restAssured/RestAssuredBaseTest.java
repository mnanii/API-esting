package restAssured;

import com.github.dzieciou.testing.curl.CurlLoggingRestAssuredConfigFactory;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import model.Board;
import org.testng.annotations.AfterMethod;
import utility.TestUtil;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static io.restassured.RestAssured.given;
import static utility.TestUtil.TRELLO_URL;

abstract class RestAssuredBaseTest {

    protected String boardId;
    protected Board board;
    protected Properties properties;

    public void initializeResources() throws IOException {
        properties = new Properties();
        FileInputStream fis = new FileInputStream("src/test/resources/config.properties");
        properties.load(fis);
    }

    @AfterMethod
    public void tearDown() {
        if (boardId != null) {
            deleteBoard(boardId);
            System.out.println("Board has been deleted.");
        }
    }

//    protected String createBoard(String boardName) {
//        Response response = getBaseRequestSpecification()
//                .queryParam("name", boardName)
//                .post("/boards/");
//
//        Board board = response.body().as(Board.class);
//        return board.getId();
//    }

    protected Board createBoard(String boardName) {
        Response response = getBaseRequestSpecification()
                .queryParam("name", boardName)
                .post("/boards/");

        return response.body().as(Board.class);
    }

    protected RequestSpecification getBaseRequestSpecification() {
        RestAssuredConfig config = CurlLoggingRestAssuredConfigFactory.createConfig();
        return given()
                .config(config)
                .baseUri(TRELLO_URL)
                .queryParam("key", properties.getProperty(TestUtil.KEY))
                .queryParam("token", properties.getProperty(TestUtil.TOKEN))
                .contentType(ContentType.JSON);
    }

    protected void deleteBoard(String boardId) {
        getBaseRequestSpecification()
                .pathParam("id", boardId)
                .when()
                .delete("/boards/{id}");
    }
}
