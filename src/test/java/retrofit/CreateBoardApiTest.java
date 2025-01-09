package retrofit;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.Board;
import model.InvalidBoardResponse;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;

import static org.testng.AssertJUnit.*;
import static org.testng.AssertJUnit.assertNotNull;
import static utility.TestUtil.BOARD_NAME;

public class CreateBoardApiTest extends RetrofitBaseTest{

    @BeforeTest
    public void setUp() throws IOException {
        initializeResources();
        trelloClient = buildTrelloClient();
    }

    @Test
    public void when_sendValidRequest_then_boardCreated() throws IOException {
        Call<Board> boardCall = trelloClient.createBoard(BOARD_NAME, getKey(), getToken());
        Response<Board> createBordResponse = boardCall.execute();
        boardId = createBordResponse.body().getId();

        assertTrue(createBordResponse.isSuccessful());
        assertEquals(200, createBordResponse.code());
        assertNotNull(createBordResponse.body());
        assertNotNull(createBordResponse.body().getId());
    }

    @Test
    public void when_createBoardWithInvalidName_then_badRequest() throws IOException {
        Call<Board> boardCall = trelloClient.createBoard("", getKey(), getToken());
        Response<Board> createBordResponse = boardCall.execute();

        InvalidBoardResponse invalidBoardResponse = parseJsonBody(createBordResponse.errorBody().string(), InvalidBoardResponse.class);
        assertEquals("invalid value for name", invalidBoardResponse.getMessage());
        assertEquals("ERROR", invalidBoardResponse.getError());
        assertEquals(400, createBordResponse.code());
    }

    @Test
    public void when_createBoardWithInvalidKey_then_Unauthorized() throws IOException {
        Call<Board> board = trelloClient.createBoard(BOARD_NAME, "124", getToken());
        Response<Board> createBordResponse = board.execute();

        assertEquals(401, createBordResponse.code());
        assertEquals("invalid key", createBordResponse.errorBody().string());
    }
}
