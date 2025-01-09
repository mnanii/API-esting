package restAssured;

import model.BoardUpdateRequest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.hamcrest.Matchers.equalTo;
import static utility.TestUtil.BOARD_NAME;

public class UpdateBoardApiTest extends RestAssuredBaseTest {

    @BeforeTest
    public void callInitializeResources() throws IOException {
        initializeResources();
    }

    @BeforeMethod
    public void setUp() throws URISyntaxException, IOException {
        board = createBoard(BOARD_NAME);
        System.out.println("Board has been created.");
    }

// TODO
    @Test
    public void updateBoardTestApi() {

        BoardUpdateRequest boardUpdateRequest = new BoardUpdateRequest("test4");
        board.setName("test");
        getBaseRequestSpecification()
                .pathParam("id", board.getId())
                .when()
                .body(boardUpdateRequest)
                .put("/boards/{id}")
                .then()
                .statusCode(200)
                .body("desc", equalTo(boardUpdateRequest.getDesc()))
                .log().all();
    }
}
