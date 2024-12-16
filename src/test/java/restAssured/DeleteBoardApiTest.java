package restAssured;

import org.testng.annotations.Test;

public class DeleteBoardApiTest extends RestAssuredBaseTest {
    @Test
    public void deleteBoard() {
        String boardId = createBoard("MyBoard3");
        getBaseRequestSpecification()
                .pathParam("id", boardId)
                .when()
                .delete("/boards/{id}")
                .then()
                .statusCode(200)
                .log().all();

        getBaseRequestSpecification()
                .pathParam("id", boardId)
                .when()
                .get("/boards/{id}")
                .then()
                .statusCode(404)
                .log().all();
    }
}
