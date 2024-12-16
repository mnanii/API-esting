package restAssured;

import org.testng.annotations.Test;

public class GetBoardByIdApiTest extends RestAssuredBaseTest {

    @Test
    public void getBoardByIdTestApi() {

        String boardId = createBoard("MyBoardTest");

        getBaseRequestSpecification()
                .pathParam("id", boardId)
                .when()
                .get("/boards/{id}")
                .then()
                .statusCode(200)
                .log().all();
    }
}
