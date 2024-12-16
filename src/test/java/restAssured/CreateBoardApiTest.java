package restAssured;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import static org.hamcrest.Matchers.notNullValue;


public class CreateBoardApiTest extends RestAssuredBaseTest {

    @Test
    public void createBoardTestApi() {
        Response response = getBaseRequestSpecification()
                .queryParam("name", "MyBoard4")
                .when()
                .post("/boards/");
//TODO
        System.out.println(response.statusCode());
        System.out.println(response.body());

//                .then()
//                .statusCode(200)
//                .body("id", notNullValue())
//                .body("url", notNullValue())
//                .body("prefs", notNullValue())
//                .log().all();
    }
}
