package retrofit;

import client.TrelloClient;
import model.Board;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import model.BoardUpdateRequest;

import java.io.IOException;

import static org.testng.AssertJUnit.*;
import static org.testng.AssertJUnit.assertNotNull;
import static utility.TestUtil.*;

public class UpdateBoardApiTest extends RetrofitBaseTest {

    @BeforeTest
    public void setUp() throws IOException {
        initializeResources();
        trelloClient = buildTrelloClient();
    }

    @Test
    public void when_updateBoardWithValidName_then_boardUpdated() throws IOException {

        Board board = createBoard(BOARD_NAME);
        BoardUpdateRequest updateBoardRequest = new BoardUpdateRequest("test");

        Call<Board> call = trelloClient.updateBoard(board.getId(), getKey(), getToken(), updateBoardRequest);
        Response<Board> response = call.execute();
        boardId = response.body().getId();

        assertTrue(response.isSuccessful());
        assertEquals(200, response.code());
        assertNotNull(response.body());
        assertEquals(updateBoardRequest.getDesc(), response.body().getDesc());
    }

    @Test
    public void when_updateBoardWithInvalidId_then_badRequest() throws IOException {
        BoardUpdateRequest updateBoardRequest = new BoardUpdateRequest("test");

        Call<Board> boardCall = trelloClient.updateBoard("123", getKey(), getToken(), updateBoardRequest);
        Response<Board> response = boardCall.execute();

        assertEquals("invalid id", response.errorBody().string());
        assertEquals(400, response.code());
    }

    @Test
    public void when_getBoardWithInvalidKey_then_Unauthorized() throws IOException {
        Board board = createBoard(BOARD_NAME);
        BoardUpdateRequest updateBoardRequest = new BoardUpdateRequest("test");

        Call<Board> boardCall = trelloClient.updateBoard(board.getId(), "124", getToken(), updateBoardRequest);
        Response<Board> response = boardCall.execute();

        assertEquals(401, response.code());
        assertEquals("invalid key", response.errorBody().string());
    }
}
