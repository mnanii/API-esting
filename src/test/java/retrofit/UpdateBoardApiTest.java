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
import model.BoardUpdateRequest;

import java.io.IOException;

import static org.testng.AssertJUnit.*;
import static org.testng.AssertJUnit.assertNotNull;
import static utility.TestUtil.KEY;
import static utility.TestUtil.TOKEN;

public class UpdateBoardApiTest extends RetrofitBaseTest {
    @Test
    public void updateBoardTest() throws IOException {

        Board board = createBoard("MyBoard8");

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

        BoardUpdateRequest updateBoardRequest = new BoardUpdateRequest("test9");

        Call<Board> call = trelloClient.updateBoard(board.getId(), KEY, TOKEN, updateBoardRequest);
        Response<Board> response = call.execute();

        System.out.println(response);

        assertTrue(response.isSuccessful());
        assertEquals(200, response.code());
        assertNotNull(response.body());
        assertEquals(updateBoardRequest.getDesc(), response.body().getDesc());
    }
}
