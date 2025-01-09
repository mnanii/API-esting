package retrofit;

import client.TrelloClient;
import model.Board;
import model.InvalidBoardResponse;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;

import static org.testng.AssertJUnit.*;
import static utility.TestUtil.*;

public class DeleteBoardApiTest extends RetrofitBaseTest {

    @BeforeTest
    public void setUp() throws IOException {
        initializeResources();
        trelloClient = buildTrelloClient();
    }

    @Test
    public void when_deleteBoard_then_BordNotFound () throws IOException {
        Board board = createBoard(BOARD_NAME);

        Call<Board> call = trelloClient.deleteBoard(board.getId(), getKey(), getToken());
        Response<Board> response = call.execute();

        assertTrue(response.isSuccessful());
        assertEquals(200, response.code());

        Call<Board> validatedCall = trelloClient.getBoardById(board.getId(), getKey(), getToken());
        Response<Board> validateResponse = validatedCall.execute();
        assertEquals(404, validateResponse.code());

    }

    @Test
    public void when_deleteBoardWithInvalidId_then_badRequest() throws IOException {
        Call<Board> boardCall = trelloClient.deleteBoard("mmm", getKey(), getToken());
        Response<Board> response = boardCall.execute();

        assertEquals("invalid id", response.errorBody().string());
        assertEquals(400, response.code());
    }

    @Test
    public void when_deleteBoardWithInvalidKey_then_Unauthorized() throws IOException {
        Board board = createBoard(BOARD_NAME);

        Call<Board> boardCall = trelloClient.deleteBoard(board.getId(), "124", getToken());
        Response<Board> response = boardCall.execute();

        assertEquals(401, response.code());
        assertEquals("invalid key", response.errorBody().string());
    }

}
