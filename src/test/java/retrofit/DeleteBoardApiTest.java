package retrofit;

import client.TrelloClient;
import model.Board;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.testng.annotations.Test;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;

import static org.testng.AssertJUnit.*;
import static utility.TestUtil.KEY;
import static utility.TestUtil.TOKEN;

public class DeleteBoardApiTest extends RetrofitBaseTest {

    @Test
    public void deleteBoardTest () throws IOException {
        Board board = createBoard("MyBoard9");

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.trello.com/")
                .addConverterFactory(JacksonConverterFactory.create())
                .client(httpClient.build())
                .build();

        TrelloClient trelloClient = retrofit.create(TrelloClient.class);

        Call<Board> call = trelloClient.deleteBoard(board.getId(), KEY, TOKEN);
        Response<Board> response = call.execute();
        System.out.println(response);

        assertTrue(response.isSuccessful());
        assertEquals(200, response.code());

        Call<Board> validatedCall = trelloClient.getBoardById(board.getId(), KEY, TOKEN);
        Response<Board> validateResponse = validatedCall.execute();
        System.out.println(validateResponse);
        assertEquals(404, validateResponse.code());

    }
}
