package restAssured;

import model.BoardUpdateRequest;
import org.testng.annotations.Test;
import static org.hamcrest.Matchers.equalTo;

public class UpdateBoardApiTest extends RestAssuredBaseTest {

    @Test
    public void updateBoardTestApi() {

        BoardUpdateRequest boardUpdateRequest = new BoardUpdateRequest("test4");

        String boardId = createBoard("MyBoard3");
        getBaseRequestSpecification()
                .pathParam("id", boardId)
                .when()
                .body(boardUpdateRequest)
                .put("/boards/{id}")
                .then()
                .statusCode(200)
                .body("desc", equalTo(boardUpdateRequest.getDesc()))
                .log().all();
    }
}
