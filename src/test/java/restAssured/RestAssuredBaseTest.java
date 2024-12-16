package restAssured;

import com.github.dzieciou.testing.curl.CurlLoggingRestAssuredConfigFactory;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import model.Board;
import utility.TestUtil;

import static io.restassured.RestAssured.given;

public class RestAssuredBaseTest {
    public String createBoard(String boardName) {
        RequestSpecification request = given()
                .baseUri("https://api.trello.com/1")
                .queryParam("name", boardName)
                .queryParam("key", TestUtil.KEY)
                .queryParam("token", TestUtil.TOKEN)
                .contentType(ContentType.JSON);

        Response response = request.post("/boards/");

        Board board1 = response.body().as(Board.class);

        Board board2 = new Board();
        board2.setId(response.body().jsonPath().get("id"));
        board2.setId(response.body().jsonPath().get("id"));
        board2.setId(response.body().jsonPath().get("id"));
        board2.setId(response.body().jsonPath().get("id"));
        board2.setId(response.body().jsonPath().get("id"));



        return board1.getId();
    }

    public RequestSpecification getBaseRequestSpecification() {
        RestAssuredConfig config = CurlLoggingRestAssuredConfigFactory.createConfig();
        return given()
                .config(config)
                .baseUri("https://api.trello.com/1")
                .queryParam("key", TestUtil.KEY)
                .queryParam("token", TestUtil.TOKEN)
                .contentType(ContentType.JSON);
    }

}
